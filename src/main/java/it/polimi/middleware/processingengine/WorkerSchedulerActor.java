package it.polimi.middleware.processingengine;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import it.polimi.middleware.processingengine.message.AddClientManagerMessage;
import it.polimi.middleware.processingengine.message.AddOperatorMessage;
import it.polimi.middleware.processingengine.message.AddRemoteMessage;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class WorkerSchedulerActor extends AbstractActor {

    private Map<ActorRef, Integer> remoteActors = new HashMap<>();

    public static Props props() {
        return Props.create(WorkerSchedulerActor.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(AddClientManagerMessage.class, this::onAddClientManager)
                .match(AddOperatorMessage.class, this::onAddOperator)
                .build();
    }

    private void onAddClientManager(AddClientManagerMessage message) {
        remoteActors.put(message.getClientManager(), message.getNrOfWorkers());
    }

    private void onAddOperator(AddOperatorMessage message) {
        ActorRef remote = getRemote();
        remote.tell(message, self());
        remoteActors.put(remote, remoteActors.get(remote) + 1);
    }

    private ActorRef getRemote() {
        Map.Entry<ActorRef, Integer> minimumUsedRemote = Collections.min(remoteActors.entrySet(), Comparator.comparing(Map.Entry::getValue));
        return minimumUsedRemote.getKey();
    }
}
