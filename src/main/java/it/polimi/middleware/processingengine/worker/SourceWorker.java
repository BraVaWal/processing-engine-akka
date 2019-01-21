package it.polimi.middleware.processingengine.worker;

import akka.actor.ActorRef;
import it.polimi.middleware.processingengine.operator.SplitOperator;

import java.util.List;

public class SourceWorker extends Worker {
    public SourceWorker(List<ActorRef> downstreamWorkers) {
        super(downstreamWorkers, new SplitOperator());
    }
}
