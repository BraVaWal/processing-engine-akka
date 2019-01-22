package it.polimi.middleware.processingengine.worker;

import akka.actor.Props;
import it.polimi.middleware.processingengine.operator.SinkOperator;

public class SinkWorker extends Worker {

    public static final String ID = "sink";

    public SinkWorker() {
        super(ID, new SinkOperator());
    }

    public static Props props() {
        return Props.create(SinkWorker.class);
    }
}
