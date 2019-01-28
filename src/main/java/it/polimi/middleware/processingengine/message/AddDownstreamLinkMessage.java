package it.polimi.middleware.processingengine.message;

import akka.actor.ActorRef;

import java.io.Serializable;

public class AddDownstreamLinkMessage implements Serializable {

    private final ActorRef source;
    private final String destination;

    public AddDownstreamLinkMessage(ActorRef source, String destination) {
        this.source = source;
        this.destination = destination;
    }

    public ActorRef getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }
}
