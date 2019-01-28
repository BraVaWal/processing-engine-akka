package it.polimi.middleware.processingengine.message;

import akka.actor.ActorRef;

import java.io.Serializable;
import java.util.Collection;

public class ClientStatusMessage implements Serializable {

    private final Collection<ActorRef> workers;

    public ClientStatusMessage(Collection<ActorRef> workers) {
        this.workers = workers;
    }

    public Collection<ActorRef> getWorkers() {
        return workers;
    }
}
