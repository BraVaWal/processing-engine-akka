package it.polimi.middleware.processingengine;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import it.polimi.middleware.processingengine.operator.Operator;
import it.polimi.middleware.processingengine.worker.Worker;

import java.util.Collection;
import java.util.LinkedList;

public class SupervisorActor extends AbstractActor {

    private ActorRef source;
    private ActorRef sink;
    private Collection<ActorRef> workers = new LinkedList<>();

    public SupervisorActor(ActorRef source, ActorRef sink) {
        this.source = source;
        this.sink = sink;
    }

    public static Props props(ActorRef source, ActorRef sink) {
        return Props.create(SupervisorActor.class, source, sink);
    }

    public void addOperator(String id, Operator operator) {
        ActorRef worker = getContext().actorOf(Worker.props(id, operator));
        workers.add(worker);
    }

    @Override
    public Receive createReceive() {
        return null;
    }
}
