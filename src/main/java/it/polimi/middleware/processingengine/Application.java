package it.polimi.middleware.processingengine;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import it.polimi.middleware.processingengine.function.AggregateFunction;
import it.polimi.middleware.processingengine.message.AddOperatorMessage;
import it.polimi.middleware.processingengine.operator.AggregateOperator;
import it.polimi.middleware.processingengine.operator.MapOperator;
import it.polimi.middleware.processingengine.operator.factory.AggregateOperatorFactory;
import it.polimi.middleware.processingengine.operator.factory.CrashOperatorFactory;
import it.polimi.middleware.processingengine.operator.factory.MapOperatorFactory;
import it.polimi.middleware.processingengine.operator.factory.OperatorFactory;
import it.polimi.middleware.processingengine.worker.SinkWorker;
import it.polimi.middleware.processingengine.worker.SourceWorker;

import java.util.Collection;

public class Application {

    public static final int NR_OF_PARTITIONS = 1;

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create();

        ActorRef sourceWorker = system.actorOf(SourceWorker.props());
        ActorRef sinkWorker = system.actorOf(SinkWorker.props());
        ActorRef supervisorActor = system.actorOf(SupervisorActor.props(sourceWorker, sinkWorker, NR_OF_PARTITIONS));

        ActorRef restServerActor = system.actorOf(RestServerActor.props(supervisorActor));

        OperatorFactory mapOperatorFactory = new MapOperatorFactory(keyValuePair -> new KeyValuePair(keyValuePair.getKey(),
                keyValuePair.getValue().toUpperCase()));

        OperatorFactory crashOperatorFactory = new CrashOperatorFactory();

        OperatorFactory aggregateOperatorFactory = new AggregateOperatorFactory((key, values) -> {
            StringBuilder sb = new StringBuilder();
            values.forEach(sb::append);
            return new KeyValuePair(key, sb.toString());
        }, 3, 3);

        supervisorActor.tell(new AddOperatorMessage("map", "source",
                null, mapOperatorFactory), ActorRef.noSender());
        supervisorActor.tell(new AddOperatorMessage("crash", "map",
                null, crashOperatorFactory), ActorRef.noSender());
        supervisorActor.tell(new AddOperatorMessage("aggregate", "crash",
                "sink", aggregateOperatorFactory), ActorRef.noSender());
    }

}
