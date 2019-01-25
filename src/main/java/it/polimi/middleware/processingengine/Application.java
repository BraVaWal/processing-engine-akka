package it.polimi.middleware.processingengine;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import it.polimi.middleware.processingengine.worker.SinkWorker;
import it.polimi.middleware.processingengine.worker.SourceWorker;

import java.io.File;

public class Application {

    public static void main(String[] args) {
        final ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        final File configFile = new File(classLoader.getResource("application.conf").getFile());

        final Config config = ConfigFactory.parseFile(configFile);
        final ActorSystem system = ActorSystem.create("Main", config);

        final ActorRef sourceWorker = system.actorOf(SourceWorker.props());
        final ActorRef sinkWorker = system.actorOf(SinkWorker.props());
        final ActorRef supervisorActor = system.actorOf(SupervisorActor.props(sourceWorker, sinkWorker), "SupervisorActor");

        final ActorRef restServerActor = system.actorOf(RestServerActor.props(supervisorActor));


    }

}
