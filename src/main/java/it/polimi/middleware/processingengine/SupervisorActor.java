package it.polimi.middleware.processingengine;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import it.polimi.middleware.processingengine.message.AddDownstreamMessage;
import it.polimi.middleware.processingengine.message.AddJobMessage;
import it.polimi.middleware.processingengine.message.AddOperatorMessage;
import it.polimi.middleware.processingengine.message.OperateMessage;
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

    private void addDownstreamOperator(ActorRef sourceWorker, ActorRef downstream) {
        sourceWorker.tell(new AddDownstreamMessage(downstream), self());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(AddOperatorMessage.class, this::onAddOperator)
                .match(AddJobMessage.class, this::onAddJob)
                .build();
    }

    private void onAddJob(AddJobMessage message) {
        for (KeyValuePair pair : message.getData()) {
            source.tell(new OperateMessage(pair), self());
        }
    }

    private void onAddOperator(AddOperatorMessage message) {
        ActorRef worker = getContext().actorOf(Worker.props(message.getOperator(), message.getDownstream()));
        for (ActorRef s : message.getSources()) {
            addDownstreamOperator(s, worker);
        }
    }
}
