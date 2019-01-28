package it.polimi.middleware.processingengine.message;

import akka.actor.ActorRef;

import java.io.Serializable;

public class AddSourceLinkMessage implements Serializable {

    private final String source;
    private final ActorRef destination;

    public AddSourceLinkMessage(String source, ActorRef destination) {
        this.source = source;
        this.destination = destination;
    }

    public String getSource() {
        return source;
    }

    public ActorRef getDestination() {
        return destination;
    }
}
