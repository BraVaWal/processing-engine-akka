package it.polimi.middleware.processingengine.worker;

import akka.actor.ActorRef;
import akka.actor.Props;
import it.polimi.middleware.processingengine.operator.SplitOperator;

import java.util.List;

public class SourceWorker extends Worker {
    public SourceWorker() {
        super("source", new SplitOperator());
    }

    public static Props props() {
        return Props.create(SourceWorker.class);
    }
}
