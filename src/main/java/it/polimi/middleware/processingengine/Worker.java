package it.polimi.middleware.processingengine;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import it.polimi.middleware.processingengine.operator.Operator;

import java.util.List;

public class Worker extends AbstractActor {

    private final List<ActorRef> downstreamWorkers;

    private final Operator operator;

    public Worker(List<ActorRef> downstreamWorkers, Operator operator) {
        this.downstreamWorkers = downstreamWorkers;
        this.operator = operator;
    }

    public static Props props(List<ActorRef> downstreamWorkers) {
        return Props.create(Worker.class, downstreamWorkers);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Message.class, message -> operator.operate(message, this::sendDownstream))
                .build();
    }

    public void sendDownstream(Message message) {
        int receiver = message.getKey().hashCode() % downstreamWorkers.size();
        downstreamWorkers.get(receiver).tell(message, self());
    }
}
