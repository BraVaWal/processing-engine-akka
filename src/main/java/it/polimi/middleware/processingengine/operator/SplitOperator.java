package it.polimi.middleware.processingengine.operator;

import it.polimi.middleware.processingengine.Message;
import it.polimi.middleware.processingengine.Worker;

public class SplitOperator extends Operator {

    public SplitOperator(Worker parent) {
        super(parent);
    }

    @Override
    public void operate(Message message) {
        tell(message);
    }
}
