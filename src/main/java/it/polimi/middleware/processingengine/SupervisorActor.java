package it.polimi.middleware.processingengine;

import akka.actor.*;
import akka.japi.pf.DeciderBuilder;
import akka.remote.RemoteScope;
import it.polimi.middleware.processingengine.message.*;
import it.polimi.middleware.processingengine.worker.SinkWorker;
import it.polimi.middleware.processingengine.worker.SourceWorker;
import scala.concurrent.duration.Duration;

import java.util.*;

public class SupervisorActor extends AbstractActor {

    private final Collection<ActorRef> clientManagers = new LinkedList<>();
    private final ActorRef workerScheduler;

    private final ActorRef source;

    private final List<String> results = new LinkedList<>();

    public SupervisorActor(ActorRef source, ActorRef sink) {
        Map<String, ActorRef> localWorkers = new HashMap<>();
        localWorkers.put(SourceWorker.ID, source);
        localWorkers.put(SinkWorker.ID, sink);
        ActorRef localClientManager = getContext().actorOf(ClientManagerActor.props(self(), localWorkers));
        clientManagers.add(localClientManager);

        workerScheduler = getContext().actorOf(WorkerSchedulerActor.props());
        workerScheduler.tell(new AddClientManagerMessage(localClientManager, 2), self());

        this.source = source;
        localClientManager.tell(new AddSourceLinkMessage(SinkWorker.ID, self()), self());
    }

    public static Props props(ActorRef source, ActorRef sink) {
        return Props.create(SupervisorActor.class, source, sink);
    }

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return new OneForOneStrategy(
                10,
                Duration.create("10 seconds"),
                DeciderBuilder
                        .match(RuntimeException.class, ex -> SupervisorStrategy.restart())
                        .build());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(AddRemoteMessage.class, this::onAddRemote)
                .match(AddOperatorMessage.class, this::onAddOperator)
                .match(AddSourceLinkMessage.class, this::onAddLink)
                .match(AddDownstreamLinkMessage.class, this::onAddLink)
                .match(AddJobMessage.class, this::onAddJob)
                .match(OperateMessage.class, this::onOperate)
                .match(AskStatusMessage.class, this::onAskStatus)
                .match(AskResultMessage.class, this::onAskResult)
                .build();
    }

    private void onAddRemote(AddRemoteMessage message) {
        Address clientManagerAddress = new Address("akka.tcp", message.getRemoteSystem(), message.getHost(), message.getPort());
        ActorRef clientManager = getContext().actorOf(ClientManagerActor.props(self()).withDeploy(new Deploy(new RemoteScope(clientManagerAddress))));
        workerScheduler.tell(new AddClientManagerMessage(clientManager, 0), self());
        clientManagers.add(clientManager);
    }

    private void onAddOperator(AddOperatorMessage message) {
        for (int i = 0; i < message.getPartitions(); i++) {
            workerScheduler.tell(new AddOperatorMessage(message.getId() + "-" + i,
                    message.getSourceId(),
                    message.getDownstreamId(),
                    message.getOperatorFactory(),
                    message.getPartitions()), self());
        }
    }

    private void onAddLink(Object message) {
        for (ActorRef clientManager : clientManagers) {
            clientManager.tell(message, sender());
        }
    }

    private void onAddJob(AddJobMessage message) {
        for (KeyValuePair pair : message.getData()) {
            source.tell(new OperateMessage(pair), self());
        }
    }

    private void onOperate(OperateMessage message) {
        if (message.getKeyValuePair().getKey().equals("Result")) {
            results.add(message.getKeyValuePair().getValue());
            sender().tell(new AcknowledgeMessage(message.getId()), self());
        }
    }

    private void onAskStatus(AskStatusMessage message) {
        sender().tell(new StatusMessage(clientManagers), self());
    }

    private void onAskResult(AskResultMessage message) {
        sender().tell(new ResultMessage(results), self());
    }
}
