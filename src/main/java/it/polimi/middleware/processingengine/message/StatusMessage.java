package it.polimi.middleware.processingengine.message;

import akka.actor.ActorRef;

import java.util.Collection;

public class StatusMessage {

    private final ActorRef source;
    private final ActorRef sink;
    private final Collection<ActorRef> actors;

    public StatusMessage(ActorRef source, ActorRef sink, Collection<ActorRef> actors) {
        this.source = source;
        this.sink = sink;
        this.actors = actors;
    }

    public ActorRef getSource() {
        return source;
    }

    public ActorRef getSink() {
        return sink;
    }

    public Collection<ActorRef> getActors() {
        return actors;
    }
}
