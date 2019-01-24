package it.polimi.middleware.processingengine;

import akka.actor.*;
import akka.japi.pf.DeciderBuilder;
import it.polimi.middleware.processingengine.message.*;
import it.polimi.middleware.processingengine.worker.SinkWorker;
import it.polimi.middleware.processingengine.worker.SourceWorker;
import it.polimi.middleware.processingengine.worker.Worker;
import scala.concurrent.duration.Duration;

import java.util.*;

public class SupervisorActor extends AbstractActor {

    private final Map<String, List<ActorRef>> workers = new HashMap<>();

    private final List<String> results = new LinkedList<>();

    public SupervisorActor(ActorRef source, ActorRef sink) {
        this.workers.put(SourceWorker.ID, Collections.singletonList(source));
        this.workers.put(SinkWorker.ID, Collections.singletonList(sink));
        addDownstreamOperator(sink, self());
    }

    public static Props props(ActorRef source, ActorRef sink) {
        return Props.create(SupervisorActor.class, source, sink);
    }

    public ActorRef getSource() {
        return workers.get(SourceWorker.ID).get(0);
    }

    public ActorRef getSink() {
        return workers.get(SinkWorker.ID).get(0);
    }

    private void addDownstreamOperator(ActorRef sourceWorker, ActorRef downstream) {
        sourceWorker.tell(new AddDownstreamMessage(downstream), self());
    }

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return new OneForOneStrategy(//
                10, //
                Duration.create("10 seconds"), //
                DeciderBuilder //
                        .match(RuntimeException.class, ex -> SupervisorStrategy.restart()) //
                        .build());
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
        List<ActorRef> newWorkers = new ArrayList<>(message.getPartitions());
        for (int i = 0; i < message.getPartitions(); i++) {
            ActorRef worker = getContext().actorOf(Worker.props(message.getId() + "-" + i, message.getOperatorFactory().build(), downstream));
            for (ActorRef source : sources) {
                addDownstreamOperator(source, worker);
            }
            newWorkers.add(worker);
        }
        workers.put(message.getId(), newWorkers);

    }

    private void onOperate(OperateMessage message) {
        if (message.getKeyValuePair().getKey().equals("Result")) {
            results.add(message.getKeyValuePair().getValue());
            sender().tell(new AcknowledgeMessage(message.getId()), self());
        }
    }

    private void onAskStatus(AskStatusMessage message) {
        sender().tell(new StatusMessage(workers.values()), self());
    }

    private void onAskResult(AskResultMessage message) {
        sender().tell(new ResultMessage(results), self());
    }
}
