package it.polimi.middleware.processingengine.message;

import akka.actor.ActorRef;

import java.io.Serializable;

public class AddClientManagerMessage implements Serializable {

    private final ActorRef clientManager;
    private final int nrOfWorkers;

    public AddClientManagerMessage(ActorRef clientManager, int nrOfWorkers) {
        this.clientManager = clientManager;
        this.nrOfWorkers = nrOfWorkers;
    }

    public ActorRef getClientManager() {
        return clientManager;
    }

    public int getNrOfWorkers() {
        return nrOfWorkers;
    }
}
