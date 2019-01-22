package it.polimi.middleware.processingengine;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import it.polimi.middleware.processingengine.message.AddOperatorMessage;
import it.polimi.middleware.processingengine.operator.AggregateOperator;
import it.polimi.middleware.processingengine.operator.MapOperator;
import it.polimi.middleware.processingengine.worker.SinkWorker;
import it.polimi.middleware.processingengine.worker.SourceWorker;

import java.util.Collections;

public class Application {

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create();

        ActorRef sourceWorker = system.actorOf(SourceWorker.props());
        ActorRef sinkWorker = system.actorOf(SinkWorker.props());
        ActorRef supervisorActor = system.actorOf(SupervisorActor.props(sourceWorker, sinkWorker));

        ActorRef restServerActor = system.actorOf(RestServerActor.props(supervisorActor));

        supervisorActor.tell(new AddOperatorMessage("map", Collections.singletonList("source"),
                Collections.emptyList(), new MapOperator(keyValuePair -> new KeyValuePair(keyValuePair.getKey(),
                keyValuePair.getValue().toUpperCase()))), ActorRef.noSender());
        supervisorActor.tell(new AddOperatorMessage("aggregate", Collections.singletonList("map"),
                Collections.singletonList("sink"), new AggregateOperator((key, values) -> {
            StringBuilder sb = new StringBuilder();
            values.forEach(sb::append);
            return new KeyValuePair(key, sb.toString());
        }, 3, 3)), ActorRef.noSender());
    }

}
