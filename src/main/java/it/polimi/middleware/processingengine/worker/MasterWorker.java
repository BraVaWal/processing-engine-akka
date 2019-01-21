package it.polimi.middleware.processingengine.worker;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.dsl.Creators;
import it.polimi.middleware.processingengine.operator.Operator;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class MasterWorker extends AbstractActor {

    private ActorRef source;
    private ActorRef sink;
    private Collection<ActorRef> workers = new LinkedList<>();

    public MasterWorker(ActorRef source, ActorRef sink) {
        this.source = source;
        this.sink = sink;
    }

    public static Props props(ActorRef source, ActorRef sink) {
        return Props.create(MasterWorker.class, source, sink);
    }

    public void addOperator(Operator operator) {
        ActorRef worker = getContext().actorOf(Worker.props(operator));
        workers.add(worker);
    }

    @Override
    public Receive createReceive() {
        return null;
    }
}
