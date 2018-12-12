package it.polimi.middleware.processingengine.operator;

import it.polimi.middleware.processingengine.Message;

public class SplitOperator implements Operator {

    @Override
    public void operate(Message message, SendDownStreamListener listener) {
        listener.onSendDownstream(message);
    }
}
