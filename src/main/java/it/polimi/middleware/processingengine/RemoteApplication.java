package it.polimi.middleware.processingengine;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import it.polimi.middleware.processingengine.function.AggregateFunction;
import it.polimi.middleware.processingengine.function.MapFunction;
import it.polimi.middleware.processingengine.message.AddOperatorMessage;
import it.polimi.middleware.processingengine.message.AddRemoteMessage;
import it.polimi.middleware.processingengine.operator.factory.AggregateOperatorFactory;
import it.polimi.middleware.processingengine.operator.factory.CrashOperatorFactory;
import it.polimi.middleware.processingengine.operator.factory.MapOperatorFactory;
import it.polimi.middleware.processingengine.operator.factory.OperatorFactory;

import java.io.File;
import java.io.Serializable;

public class RemoteApplication {

    public static final String LOCAL_SYSTEM_NAME = "Remote";
    public static final String LOCAL_IP = "127.0.0.1";
    public static final int LOCAL_PORT = 2553;

    public static final String REMOTE_SYSTEM_NAME = "Main";
    public static final String REMOTE_IP = "127.0.0.1";
    public static final int REMOTE_PORT = 2552;

    public static void main(String[] args) {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        File configFile = new File(classLoader.getResource("application-remote.conf").getFile());

        final Config config = ConfigFactory.parseFile(configFile);
        final ActorSystem system = ActorSystem.create(LOCAL_SYSTEM_NAME, config);

        ActorSelection supervisorActor = system.actorSelection("akka.tcp://" + REMOTE_SYSTEM_NAME + "@" + REMOTE_IP + ":" + REMOTE_PORT + "/user/SupervisorActor");

        supervisorActor.tell(new AddRemoteMessage(LOCAL_SYSTEM_NAME, LOCAL_IP, LOCAL_PORT), ActorRef.noSender());

        final OperatorFactory mapOperatorFactory = new MapOperatorFactory((MapFunction & Serializable) keyValuePair -> new KeyValuePair(keyValuePair.getKey(),
                keyValuePair.getValue().toUpperCase()));

        final OperatorFactory crashOperatorFactory = new CrashOperatorFactory();

        final OperatorFactory aggregateOperatorFactory = new AggregateOperatorFactory((AggregateFunction & Serializable) (key, values) -> {
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
