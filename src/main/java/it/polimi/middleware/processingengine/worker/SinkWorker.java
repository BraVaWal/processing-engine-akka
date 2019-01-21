package it.polimi.middleware.processingengine.worker;

import akka.actor.ActorRef;
import akka.actor.Props;
import it.polimi.middleware.processingengine.operator.SinkOperator;

import java.util.List;

public class SinkWorker extends Worker {
    public SinkWorker() {
        super("sink", new SinkOperator());
    }

    public static Props props() {
        return Props.create(SinkWorker.class);
    }
}
