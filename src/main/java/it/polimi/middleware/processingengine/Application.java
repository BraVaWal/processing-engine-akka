package it.polimi.middleware.processingengine;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import it.polimi.middleware.processingengine.worker.SinkWorker;
import it.polimi.middleware.processingengine.worker.SourceWorker;

public class Application {

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create();

        ActorRef sourceWorker = system.actorOf(SourceWorker.props());
        ActorRef sinkWorker = system.actorOf(SinkWorker.props());
        ActorRef supervisorActor = system.actorOf(SupervisorActor.props(sourceWorker, sinkWorker));

        ActorRef restServerActor = system.actorOf(RestServerActor.props(supervisorActor));


    }

}
