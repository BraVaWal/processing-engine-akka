package it.polimi.middleware.processingengine.message;

import akka.actor.ActorRef;
import it.polimi.middleware.processingengine.KeyValuePair;

import java.util.Collection;
import java.util.List;

public class StatusMessage {

    private final Collection<ActorRef> actors;

    public StatusMessage(Collection<ActorRef> actors) {
        this.actors = actors;
    }

    public Collection<ActorRef> getActors() {
        return actors;
    }
}
