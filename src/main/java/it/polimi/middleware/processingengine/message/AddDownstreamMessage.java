package it.polimi.middleware.processingengine.message;

import akka.actor.ActorRef;

public class AddDownstreamMessage {

    private final ActorRef downstreamOperator;

    public AddDownstreamMessage(ActorRef downstreamOperator) {
        this.downstreamOperator = downstreamOperator;
    }


    public ActorRef getDownstreamOperator() {
        return downstreamOperator;
    }
}
