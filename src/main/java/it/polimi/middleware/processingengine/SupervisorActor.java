package it.polimi.middleware.processingengine;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import it.polimi.middleware.processingengine.message.*;
import it.polimi.middleware.processingengine.worker.Worker;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class SupervisorActor extends AbstractActor {

    private ActorRef source;
    private ActorRef sink;
    private Collection<ActorRef> workers = new LinkedList<>();
    private List<String> results = new LinkedList<>();

    public SupervisorActor(ActorRef source, ActorRef sink) {
        this.source = source;
        this.sink = sink;
        addDownstreamOperator(sink, self());
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
                .match(OperateMessage.class, this::onOperate)
                .match(AskStatusMessage.class, this::onAskStatus)
                .match(AskResultMessage.class, this::onAskResult)
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
        workers.add(worker);
    }

    private void onOperate(OperateMessage message) {
        if (getSender().equals(sink) && message.getKeyValuePair().getKey().equals("Result")) {
            results.add(message.getKeyValuePair().getValue());
        }
    }

    private void onAskStatus(AskStatusMessage message) {
        sender().tell(new StatusMessage(workers), self());
    }

    private void onAskResult(AskResultMessage message) {
        sender().tell(new ResultMessage(results), self());
    }
}
