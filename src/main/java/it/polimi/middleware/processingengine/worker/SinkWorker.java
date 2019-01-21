package it.polimi.middleware.processingengine.worker;

import akka.actor.ActorRef;
import it.polimi.middleware.processingengine.operator.SinkOperator;

import java.util.List;

public class SinkWorker extends Worker {
    public SinkWorker(List<ActorRef> downstreamWorkers) {
        super(downstreamWorkers, new SinkOperator());
    }
}
