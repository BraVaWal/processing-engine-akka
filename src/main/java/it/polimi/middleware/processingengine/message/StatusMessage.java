package it.polimi.middleware.processingengine.message;

import akka.actor.ActorRef;

import java.util.Collection;
import java.util.List;

public class StatusMessage {

    private final Collection<List<ActorRef>> workers;

    public StatusMessage(Collection<List<ActorRef>> workers) {
        this.workers = workers;
    }

    public Collection<List<ActorRef>> getWorkers() {
        return workers;
    }
}
