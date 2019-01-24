package it.polimi.middleware.processingengine.worker;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.pattern.Patterns;
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

    private Queue<UUID> messagesReceived = new LinkedList<>();
    private OperateMessage lastReceived;

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
    public Receive createReceive() {
        return receiveBuilder()
                .match(OperateMessage.class, this::onOperateMessage)
                .match(AddDownstreamMessage.class, this::onAddDownstreamMessage)
                .match(AskStatusMessage.class, this::onAskStatusMessage)
                .build();
    }

    private void onOperateMessage(OperateMessage operateMessage) {
        if (!messagesReceived.contains(operateMessage.getId())) {
            if (lastReceived != null) {
                throw new IllegalStateException("Still handling older message!!!");
            }
            lastReceived = operateMessage;
            sender().tell(new AcknowledgeMessage(operateMessage.getId()), self());

            operator.operate(operateMessage, this::sendDownstream);
            if (messagesReceived.size() > MAX_LAST_MESSAGES_STORED) {
                messagesReceived.poll();
            }
            messagesReceived.add(operateMessage.getId());
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
        List<String> downstreamAsString = downstreamWorkers.stream().map(ActorRef::toString).collect(Collectors.toList());
        sender().tell(new WorkerStatusMessage(id, operator, downstreamAsString), self());
    }

    private void sendDownstream(OperateMessage operateMessage) {
        int receiver = operateMessage.getKeyValuePair().getKey().hashCode() % downstreamWorkers.size();
        int triesLeft = MAX_SEND_TRIES;
        while (triesLeft > 0) {
            try {
                sendOperateMessage(downstreamWorkers.get(receiver), operateMessage);
                lastReceived = null;
                triesLeft = 0;
            } catch (Exception e) {
                triesLeft--;
                if (triesLeft == 0) {
                    throw new RuntimeException("Max resend exceeded!");
                }
            }
        }

    }

    private AcknowledgeMessage sendOperateMessage(ActorRef receiver, OperateMessage operateMessage) throws Exception {
        final Future<Object> ack = Patterns.ask(receiver, operateMessage, 1000);
        return (AcknowledgeMessage) Await.result(ack, Duration.create(1000, TimeUnit.MILLISECONDS));
    }
}
