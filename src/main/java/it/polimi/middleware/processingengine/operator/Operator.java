package it.polimi.middleware.processingengine.operator;

import it.polimi.middleware.processingengine.Message;
import it.polimi.middleware.processingengine.Worker;

import java.util.List;

public abstract class Operator {

    private final Worker parent;

    public Operator(Worker parent) {
        this.parent = parent;
    }

    public abstract void operate(Message message);

    protected void tell(Message message) {
        parent.tell(message);
    }

}
