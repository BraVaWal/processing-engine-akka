package it.polimi.middleware.processingengine.message;

import akka.actor.ActorRef;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class StatusMessage implements Serializable {

    private final Collection<ActorRef> clientManagers;

    public StatusMessage(Collection<ActorRef> clientManagers) {
        this.clientManagers = clientManagers;
    }

    public Collection<ActorRef> getClientManagers() {
        return clientManagers;
    }
}
