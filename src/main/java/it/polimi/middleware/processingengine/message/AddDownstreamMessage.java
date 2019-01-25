package it.polimi.middleware.processingengine.message;

import akka.actor.ActorRef;

import java.io.Serializable;

public class AddDownstreamMessage implements Serializable {

    private final ActorRef downstreamOperator;

    public AddDownstreamMessage(ActorRef downstreamOperator) {
        this.downstreamOperator = downstreamOperator;
    }


    public ActorRef getDownstreamOperator() {
        return downstreamOperator;
    }
}
