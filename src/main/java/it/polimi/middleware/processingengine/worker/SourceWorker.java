package it.polimi.middleware.processingengine.worker;

import akka.actor.Props;
import it.polimi.middleware.processingengine.operator.SplitOperator;

public class SourceWorker extends Worker {

    public static final String ID = "source";

    public SourceWorker() {
        super(ID, new SplitOperator());
    }

    public static Props props() {
        return Props.create(SourceWorker.class);
    }
}
