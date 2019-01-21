package it.polimi.middleware.processingengine;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import it.polimi.middleware.processingengine.operator.Operator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class MasterWorker extends AbstractActor {

    private Collection<ActorRef> workers = new LinkedList<>();

    public void addOperator(List<ActorRef> downstreamWorkers, Operator operator) {
        ActorRef worker = getContext().actorOf(Worker.props(downstreamWorkers, operator));
        workers.add(worker);
    }

    @Override
    public Receive createReceive() {
        return null;
    }
}
