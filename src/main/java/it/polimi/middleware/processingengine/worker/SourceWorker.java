package it.polimi.middleware.processingengine.worker;

import akka.actor.Props;
import it.polimi.middleware.processingengine.operator.SplitOperator;

public class SourceWorker extends Worker {
    public SourceWorker() {
        super(new SplitOperator());
    }

    public static Props props() {
        return Props.create(SourceWorker.class);
    }
}
