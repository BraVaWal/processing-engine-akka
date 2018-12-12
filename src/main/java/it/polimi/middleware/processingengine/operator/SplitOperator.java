package it.polimi.middleware.processingengine.operator;

import it.polimi.middleware.processingengine.Message;

public class SplitOperator extends Operator {

    @Override
    public void operate(Message message, SendDownStreamListener listener) {
        listener.onSendDownstream(message);
    }
}
