package it.polimi.middleware.processingengine;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import it.polimi.middleware.processingengine.message.AddOperatorMessage;
import it.polimi.middleware.processingengine.operator.factory.AggregateOperatorFactory;
import it.polimi.middleware.processingengine.operator.factory.CrashOperatorFactory;
import it.polimi.middleware.processingengine.operator.factory.MapOperatorFactory;
import it.polimi.middleware.processingengine.operator.factory.OperatorFactory;
import it.polimi.middleware.processingengine.worker.SinkWorker;
import it.polimi.middleware.processingengine.worker.SourceWorker;

public class Application {

    public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create();

        final ActorRef sourceWorker = system.actorOf(SourceWorker.props());
        final ActorRef sinkWorker = system.actorOf(SinkWorker.props());
        final ActorRef supervisorActor = system.actorOf(SupervisorActor.props(sourceWorker, sinkWorker));

        final ActorRef restServerActor = system.actorOf(RestServerActor.props(supervisorActor));

        final OperatorFactory mapOperatorFactory = new MapOperatorFactory(keyValuePair -> new KeyValuePair(keyValuePair.getKey(),
                keyValuePair.getValue().toUpperCase()));

        final OperatorFactory crashOperatorFactory = new CrashOperatorFactory();

        final OperatorFactory aggregateOperatorFactory = new AggregateOperatorFactory((key, values) -> {
            StringBuilder sb = new StringBuilder();
            values.forEach(sb::append);
            return new KeyValuePair(key, sb.toString());
        }, 3, 3);

        supervisorActor.tell(new AddOperatorMessage("map", "source",
                null, mapOperatorFactory, 1), ActorRef.noSender());
        supervisorActor.tell(new AddOperatorMessage("crash", "map",
                null, crashOperatorFactory, 1), ActorRef.noSender());
        supervisorActor.tell(new AddOperatorMessage("aggregate", "crash",
                "sink", aggregateOperatorFactory, 1), ActorRef.noSender());
    }

}
