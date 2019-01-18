package it.polimi.middleware.processingengine;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import it.polimi.middleware.processingengine.message.AddDownstreamMessage;
import it.polimi.middleware.processingengine.message.OperateMessage;
import it.polimi.middleware.processingengine.operator.Operator;

import java.util.List;

public class Worker extends AbstractActor {

    private final List<ActorRef> downstreamWorkers;

    private final Operator operator;

    public Worker(List<ActorRef> downstreamWorkers, Operator operator) {
        this.downstreamWorkers = downstreamWorkers;
        this.operator = operator;
    }

    public static Props props(List<ActorRef> downstreamWorkers, Operator operator) {
        return Props.create(Worker.class, downstreamWorkers, operator);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(OperateMessage.class, this::onOperateMessage)
                .match(AddDownstreamMessage.class, this::onAddDownstreamMessage)
                .build();
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
