package it.polimi.middleware.processingengine.operator;

import it.polimi.middleware.processingengine.Message;
import it.polimi.middleware.processingengine.Worker;

public class MergeOperator extends Operator{

    public MergeOperator(Worker parent) {
        super(parent);
    }

    @Override
    public void operate(Message message) {
        tell(message);
    }
}
