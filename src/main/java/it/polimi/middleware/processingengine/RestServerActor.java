package it.polimi.middleware.processingengine;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;

public class RestServerActor extends AbstractActor {

    private final ActorRef supervisorActor;

    public RestServerActor(ActorRef supervisorActor) {
        this.supervisorActor = supervisorActor;
    }


    @Override
    public Receive createReceive() {
        return null;
    }
}
