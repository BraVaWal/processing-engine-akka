package it.polimi.middleware.processingengine;

import akka.actor.*;
import akka.japi.pf.DeciderBuilder;
import it.polimi.middleware.processingengine.message.*;
import it.polimi.middleware.processingengine.worker.Worker;
import scala.concurrent.duration.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClientManagerActor extends AbstractActor {

    private final ActorRef supervisor;
    private final Map<String, ActorRef> workers;

    public ClientManagerActor(ActorRef supervisor) {
        this(supervisor, new HashMap<>());
    }

    public ClientManagerActor(ActorRef supervisor, Map<String, ActorRef> workers) {
        this.supervisor = supervisor;
        this.workers = workers;
    }

    public static Props props(ActorRef supervisor) {
        return Props.create(ClientManagerActor.class, supervisor);
    }

    public static Props props(ActorRef supervisor, Map<String, ActorRef> workers) {
        return Props.create(ClientManagerActor.class, supervisor, workers);
    }

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return new OneForOneStrategy(
                10,
                Duration.create("10 seconds"),
                DeciderBuilder
                        .match(CrashException.class, ex -> SupervisorStrategy.restart())
                        .match(SendDownstreamException.class, ex -> SupervisorStrategy.restart())
                        .match(ActorInitializationException.class, ex -> SupervisorStrategy.restart())
                        .build());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(AddOperatorMessage.class, this::onAddOperator)
                .match(AddSourceLinkMessage.class, this::onAddSourceLink)
                .match(AddDownstreamLinkMessage.class, this::onAddDownstreamLink)
                .match(AskStatusMessage.class, this::onAskStatus)
                .build();
    }

    private void onAddOperator(AddOperatorMessage message) {
        ActorRef newWorker = getContext().actorOf(Worker.props(message.getId(), message.getOperatorFactory().build()));
        if (message.getSourceId() != null) {
            supervisor.tell(new AddSourceLinkMessage(message.getSourceId(), newWorker), self());
        }
        if (message.getDownstreamId() != null) {
            supervisor.tell(new AddDownstreamLinkMessage(newWorker, message.getDownstreamId()), self());
        }
        workers.put(message.getId(), newWorker);
    }

    private void onAddSourceLink(AddSourceLinkMessage message) {
        for (String id : workers.keySet()) {
            if(id.startsWith(message.getSource())) {
                workers.get(id).tell(new AddDownstreamMessage(message.getDestination()), sender());
            }
        }
    }

    private void onAddDownstreamLink(AddDownstreamLinkMessage message) {
        for (String id : workers.keySet()) {
            if(id.startsWith(message.getDestination())) {
                message.getSource().tell(new AddDownstreamMessage(workers.get(id)), self());
            }
        }
    }

    private void onAskStatus(AskStatusMessage message) {
        sender().tell(new ClientStatusMessage(new ArrayList<>(workers.values())), self());
    }

}
