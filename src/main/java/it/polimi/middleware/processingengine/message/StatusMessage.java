package it.polimi.middleware.processingengine.message;

import akka.actor.ActorRef;

import java.util.Collection;

public class StatusMessage {

    private final Collection<ActorRef> workers;

    public StatusMessage(Collection<ActorRef> workers) {
        this.workers = workers;
    }

    public Collection<ActorRef> getWorkers() {
        return workers;
    }
}
