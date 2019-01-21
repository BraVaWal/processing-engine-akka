package it.polimi.middleware.processingengine.worker;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import it.polimi.middleware.processingengine.message.AddDownstreamMessage;
import it.polimi.middleware.processingengine.message.OperateMessage;
import it.polimi.middleware.processingengine.operator.Operator;

import java.util.LinkedList;
import java.util.List;

public class Worker extends AbstractActor {

    private final String id;

    private final List<ActorRef> downstreamWorkers;

    private final Operator operator;

    public Worker(String id, Operator operator) {
        this.id = id;
        this.downstreamWorkers = new LinkedList<>();
        this.operator = operator;
    }

    public static Props props(String id, Operator operator) {
        return Props.create(Worker.class, id, operator);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(OperateMessage.class, this::onOperateMessage)
                .match(AddDownstreamMessage.class, this::onAddDownstreamMessage)
                .build();
    }

    public void addDownstreamOperator(ActorRef downstreamOperator) {
        downstreamWorkers.add(downstreamOperator);
    }

    private void onOperateMessage(OperateMessage operateMessage) {
        operator.operate(operateMessage, this::sendDownstream);
    }

    private void onAddDownstreamMessage(AddDownstreamMessage addDownstreamMessage) {
        downstreamWorkers.add(addDownstreamMessage.getDownstreamOperator());
    }

    private void sendDownstream(OperateMessage operateMessage) {
        int receiver = operateMessage.getKey().hashCode() % downstreamWorkers.size();
        downstreamWorkers.get(receiver).tell(operateMessage, self());
    }
}
