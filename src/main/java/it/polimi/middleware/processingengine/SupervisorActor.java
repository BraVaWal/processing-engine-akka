package it.polimi.middleware.processingengine;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import it.polimi.middleware.processingengine.message.*;
import it.polimi.middleware.processingengine.worker.SinkWorker;
import it.polimi.middleware.processingengine.worker.SourceWorker;
import it.polimi.middleware.processingengine.worker.Worker;

import java.util.*;
import java.util.stream.Collectors;

public class SupervisorActor extends AbstractActor {

    private final Map<String, ActorRef> workers = new HashMap<>();

    private final List<String> results = new LinkedList<>();

    public SupervisorActor(ActorRef source, ActorRef sink) {
        workers.put(SourceWorker.ID, source);
        workers.put(SinkWorker.ID, sink);
        addDownstreamOperator(sink, self());
    }

    public static Props props(ActorRef source, ActorRef sink) {
        return Props.create(SupervisorActor.class, source, sink);
    }

    private void addDownstreamOperator(ActorRef sourceWorker, ActorRef downstream) {
        sourceWorker.tell(new AddDownstreamMessage(downstream), self());
    }

    public ActorRef getSource() {
        return workers.get(SourceWorker.ID);
    }

    public ActorRef getSink() {
        return workers.get(SinkWorker.ID);
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
            getSource().tell(new OperateMessage(pair), self());
        }
    }

    private void onAddOperator(AddOperatorMessage message) {
        List<ActorRef> sources = message.getSourceIds().stream().map(workers::get).collect(Collectors.toList());
        List<ActorRef> downstream = message.getDownstreamIds().stream().map(workers::get).collect(Collectors.toList());
        ActorRef worker = getContext().actorOf(Worker.props(message.getId(), message.getOperator(), downstream));
        for (ActorRef s : sources) {
            addDownstreamOperator(s, worker);
        }
        workers.put(message.getId(), worker);
    }

    private void onOperate(OperateMessage message) {
        if (getSender().equals(getSink()) && message.getKeyValuePair().getKey().equals("Result")) {
            results.add(message.getKeyValuePair().getValue());
        }
    }

    private void onAskStatus(AskStatusMessage message) {
        sender().tell(new StatusMessage(workers.values()), self());
    }

    private void onAskResult(AskResultMessage message) {
        sender().tell(new ResultMessage(results), self());
    }
}
