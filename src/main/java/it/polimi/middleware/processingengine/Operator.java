package it.polimi.middleware.processingengine;

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
