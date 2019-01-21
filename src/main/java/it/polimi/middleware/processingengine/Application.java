package it.polimi.middleware.processingengine;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import it.polimi.middleware.processingengine.message.AddOperatorMessage;
import it.polimi.middleware.processingengine.message.OperateMessage;
import it.polimi.middleware.processingengine.operator.MapOperator;
import it.polimi.middleware.processingengine.worker.SinkWorker;
import it.polimi.middleware.processingengine.worker.SourceWorker;

import java.util.Arrays;
import java.util.Collections;

public class Application {

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create();

        ActorRef sourceWorker = system.actorOf(SourceWorker.props());
        ActorRef sinkWorker = system.actorOf(SinkWorker.props());
        ActorRef supervisorActor = system.actorOf(SupervisorActor.props(sourceWorker, sinkWorker));

        ActorRef restServerActor = system.actorOf(RestServerActor.props(supervisorActor));

        supervisorActor.tell(new AddOperatorMessage(Collections.singletonList(sourceWorker),
                Collections.singletonList(sinkWorker),
                new MapOperator(keyValuePair -> new KeyValuePair(keyValuePair.getKey(), keyValuePair.getValue().toUpperCase()))),
                ActorRef.noSender());
    }

}
