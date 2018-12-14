package it.polimi.middleware.processingengine.operator;

import it.polimi.middleware.processingengine.message.Message;

public class MergeOperator implements Operator {

    public MergeOperator() {

    }

    @Override
    public void operate(Message message, SendDownStreamListener listener) {
        listener.onSendDownstream(message);
    }
}
