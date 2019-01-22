package it.polimi.middleware.processingengine;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import it.polimi.middleware.processingengine.message.AddOperatorMessage;
import it.polimi.middleware.processingengine.operator.AggregateOperator;
import it.polimi.middleware.processingengine.operator.MapOperator;
import it.polimi.middleware.processingengine.worker.SinkWorker;
import it.polimi.middleware.processingengine.worker.SourceWorker;

public class Application {

    public static final int NR_OF_PARTITIONS = 4;

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create();

        ActorRef sourceWorker = system.actorOf(SourceWorker.props());
        ActorRef sinkWorker = system.actorOf(SinkWorker.props());
        ActorRef supervisorActor = system.actorOf(SupervisorActor.props(sourceWorker, sinkWorker, 4));

        ActorRef restServerActor = system.actorOf(RestServerActor.props(supervisorActor));

        supervisorActor.tell(new AddOperatorMessage("map", "source",
                null, new MapOperator(keyValuePair -> new KeyValuePair(keyValuePair.getKey(),
                keyValuePair.getValue().toUpperCase()))), ActorRef.noSender());
        supervisorActor.tell(new AddOperatorMessage("aggregate", "map",
                "sink", new AggregateOperator((key, values) -> {
            StringBuilder sb = new StringBuilder();
            values.forEach(sb::append);
            return new KeyValuePair(key, sb.toString());
        }, 3, 3)), ActorRef.noSender());
    }

}
