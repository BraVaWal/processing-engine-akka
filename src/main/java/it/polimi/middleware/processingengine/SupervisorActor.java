package it.polimi.middleware.processingengine;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import it.polimi.middleware.processingengine.message.*;
import it.polimi.middleware.processingengine.worker.SinkWorker;
import it.polimi.middleware.processingengine.worker.SourceWorker;
import it.polimi.middleware.processingengine.worker.Worker;

import java.util.*;

public class SupervisorActor extends AbstractActor {

    private final Map<String, List<ActorRef>> workers = new HashMap<>();

    private final List<String> results = new LinkedList<>();

    private final int nrOfPartitions;

    public SupervisorActor(ActorRef source, ActorRef sink, int nrOfPartitions) {
        this.workers.put(SourceWorker.ID, Collections.singletonList(source));
        this.workers.put(SinkWorker.ID, Collections.singletonList(sink));
        this.nrOfPartitions = nrOfPartitions;
        addDownstreamOperator(sink, self());
    }

    public static Props props(ActorRef source, ActorRef sink, int nrOfPartitions) {
        return Props.create(SupervisorActor.class, source, sink, nrOfPartitions);
    }

    private void addDownstreamOperator(ActorRef sourceWorker, ActorRef downstream) {
        sourceWorker.tell(new AddDownstreamMessage(downstream), self());
    }

    public ActorRef getSource() {
        return workers.get(SourceWorker.ID).get(0);
    }

    public ActorRef getSink() {
        return workers.get(SinkWorker.ID).get(0);
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
        List<ActorRef> sources = workers.getOrDefault(message.getSourceId(), new LinkedList<>());
        List<ActorRef> downstream = workers.getOrDefault(message.getDownstreamId(), new LinkedList<>());
        List<ActorRef> newWorkers = new ArrayList<>(nrOfPartitions);
        for (int i = 0; i < nrOfPartitions; i++) {
            ActorRef worker = getContext().actorOf(Worker.props(message.getId() + "-" + i, message.getOperatorFactory().build(), downstream));
            for (ActorRef source : sources) {
                addDownstreamOperator(source, worker);
            }
            newWorkers.add(worker);
        }
        workers.put(message.getId(), newWorkers);

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
