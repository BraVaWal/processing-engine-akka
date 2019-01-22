package it.polimi.middleware.processingengine.worker;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import it.polimi.middleware.processingengine.message.AddDownstreamMessage;
import it.polimi.middleware.processingengine.message.AskStatusMessage;
import it.polimi.middleware.processingengine.message.OperateMessage;
import it.polimi.middleware.processingengine.message.WorkerStatusMessage;
import it.polimi.middleware.processingengine.operator.Operator;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Worker extends AbstractActor {

    private final List<ActorRef> downstreamWorkers;

    private final Operator operator;

    public Worker(Operator operator) {
        this(operator, new LinkedList<>());
    }

    public Worker(Operator operator, List<ActorRef> downstreamWorkers) {
        this.downstreamWorkers = downstreamWorkers;
        this.operator = operator;
    }

    public static Props props(Operator operator) {
        return Props.create(Worker.class, operator);
    }

    public static Props props(Operator operator, List<ActorRef> downstreamWorkers) {
        return Props.create(Worker.class, operator, downstreamWorkers);
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
        operator.operate(operateMessage, this::sendDownstream);
    }

    private void onAddDownstreamMessage(AddDownstreamMessage addDownstreamMessage) {
        downstreamWorkers.add(addDownstreamMessage.getDownstreamOperator());
    }

    private void onAskStatusMessage(AskStatusMessage askStatusMessage) {
        List<String> downstreamAsString = downstreamWorkers.stream().map(ActorRef::toString).collect(Collectors.toList());
        sender().tell(new WorkerStatusMessage(self().toString(), operator, downstreamAsString), self());
    }

    private void sendDownstream(OperateMessage operateMessage) {
        int receiver = operateMessage.getKeyValuePair().getKey().hashCode() % downstreamWorkers.size();
        downstreamWorkers.get(receiver).tell(operateMessage, self());
    }
}
