package it.polimi.middleware.processingengine.message;

import akka.actor.ActorRef;

import java.util.Collection;

public class StatusMessage {

    private final Collection<ActorRef> actors;

    public StatusMessage(Collection<ActorRef> actors) {
        this.actors = actors;
    }

    public Collection<ActorRef> getActors() {
        return actors;
    }
}
