package it.polimi.middleware.processingengine.operator;

import it.polimi.middleware.processingengine.Message;

public class MergeOperator extends Operator {

    public MergeOperator() {

    }

    @Override
    public void operate(Message message, SendDownStreamListener listener) {
        listener.onSendDownstream(message);
    }
}
