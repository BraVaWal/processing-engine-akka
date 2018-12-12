package it.polimi.middleware.processingengine.operator;

import it.polimi.middleware.processingengine.Message;
import it.polimi.middleware.processingengine.Worker;

public abstract class Operator {

    private final Worker worker;

    public Operator(Worker worker) {
        this.worker = worker;
    }

    public abstract void operate(Message message);

    void tellWorker(Message message) {
        worker.sendDownstream(message);
    }

}
