package it.polimi.middleware.processingengine.operator;

import it.polimi.middleware.processingengine.Message;
import it.polimi.middleware.processingengine.Worker;

public class SplitOperator extends Operator {

    public SplitOperator(Worker worker) {
        super(worker);
    }

    @Override
    public void operate(Message message) {
        tellWorker(message);
    }
}
