package it.polimi.middleware.processingengine.worker;

import akka.actor.AbstractActor;
import akka.actor.ActorInitializationException;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.pattern.Patterns;
import it.polimi.middleware.processingengine.CrashException;
import it.polimi.middleware.processingengine.SendDownstreamException;
import it.polimi.middleware.processingengine.message.*;
import it.polimi.middleware.processingengine.operator.Operator;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Worker extends AbstractActor {

    private static final int MAX_SEND_TRIES = 100;
    private static final int MAX_LAST_MESSAGES_STORED = 100;

    private final String id;
    private final List<ActorRef> downstreamWorkers;
    private final Operator operator;

    private final Queue<UUID> operated = new LinkedList<>();

    public Worker(String id, Operator operator) {
        this(id, operator, new LinkedList<>());
    }

    public Worker(String id, Operator operator, List<ActorRef> downstreamWorkers) {
        this.id = id;
        this.downstreamWorkers = downstreamWorkers;
        this.operator = operator;
    }

    public static Props props(String id, Operator operator) {
        return Props.create(Worker.class, id, operator);
    }

    public static Props props(String id, Operator operator, List<ActorRef> downstreamWorkers) {
        return Props.create(Worker.class, id, operator, downstreamWorkers);
    }

    @Override
    public void postRestart(Throwable reason) throws Exception {
        if (reason instanceof CrashException) {
            downstreamWorkers.addAll(((CrashException) reason).getDownstream());
            operate(((CrashException) reason).getOperateMessage());
        } else if (reason instanceof SendDownstreamException) {
            downstreamWorkers.addAll(((SendDownstreamException) reason).getDownstream());
            sendDownstream(((SendDownstreamException) reason).getOperateMessage());
        } else if (reason instanceof ActorInitializationException) {
            if (reason.getCause() instanceof CrashException) {
                downstreamWorkers.addAll(((CrashException) reason.getCause()).getDownstream());
                operate(((CrashException) reason.getCause()).getOperateMessage());
            } else if (reason.getCause() instanceof SendDownstreamException) {
                downstreamWorkers.addAll(((SendDownstreamException) reason.getCause()).getDownstream());
                sendDownstream(((SendDownstreamException) reason.getCause()).getOperateMessage());
            }
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(OperateMessage.class, this::onOperateMessage)
                .match(AddDownstreamMessage.class, this::onAddDownstreamMessage)
                .match(AskStatusMessage.class, this::onAskStatusMessage)
                .build();
    }

    private void onOperateMessage(OperateMessage operateMessage) {
        if (!operated.contains(operateMessage.getId())) {
            operate(operateMessage);
            sender().tell(new AcknowledgeMessage(operateMessage.getId()), self());
        } else {
            sender().tell(new AcknowledgeMessage(operateMessage.getId()), self());
        }
    }

    private void onAddDownstreamMessage(AddDownstreamMessage addDownstreamMessage) {
        if (!downstreamWorkers.contains(addDownstreamMessage.getDownstreamOperator())) {
            downstreamWorkers.add(addDownstreamMessage.getDownstreamOperator());
        }
    }

    private void onAskStatusMessage(AskStatusMessage askStatusMessage) {
        final List<String> downstreamAsString = downstreamWorkers.stream().map(ActorRef::toString).collect(Collectors.toList());
        sender().tell(new WorkerStatusMessage(id, operator, downstreamAsString, operated), self());
    }

    private void operate(OperateMessage operateMessage) {
        try {
            operator.operate(operateMessage, this::sendDownstream);
            if (operated.size() > MAX_LAST_MESSAGES_STORED) {
                operated.poll();
            }
            operated.add(operateMessage.getId());
        } catch (SendDownstreamException e) {
            throw new SendDownstreamException(e.getOperateMessage(), downstreamWorkers);
        } catch (RuntimeException e) {
            throw new CrashException(operateMessage, downstreamWorkers);
        }
    }

    private void sendDownstream(OperateMessage operateMessage) {
        if (downstreamWorkers.size() == 0) {
            return;
        }
        final int receiver = operateMessage.getKeyValuePair().getKey().hashCode() % downstreamWorkers.size();
        int triesLeft = MAX_SEND_TRIES;
        while (triesLeft > 0) {
            try {
                AcknowledgeMessage ack = sendOperateMessage(downstreamWorkers.get(receiver), operateMessage);
                if (ack.getId().equals(operateMessage.getId())) {
                    triesLeft = 0;
                }
            } catch (Exception e) {
                triesLeft--;
                if (triesLeft == 0) {
                    throw new SendDownstreamException(operateMessage, downstreamWorkers);
                }
            }
        }

    }

    private AcknowledgeMessage sendOperateMessage(ActorRef receiver, OperateMessage operateMessage) throws Exception {
        final Future<Object> ack = Patterns.ask(receiver, operateMessage, 1000);
        return (AcknowledgeMessage) Await.result(ack, Duration.create(1000, TimeUnit.MILLISECONDS));
    }
}
