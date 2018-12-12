package it.polimi.middleware.processingengine;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import it.polimi.middleware.processingengine.operator.Operator;

import java.util.List;

public class Worker extends AbstractActor {
    
    private final List<ActorRef> downstreamWorkers;

    private Operator operator;

    public Worker(List<ActorRef> downstreamWorkers) {
        this.downstreamWorkers = downstreamWorkers;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Message.class, operator::operate)
                .build();
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public void sendDownstream(Message message) {
        int receiver = message.getKey().hashCode() % downstreamWorkers.size();
        downstreamWorkers.get(receiver).tell(message, self());
    }
}
