package it.polimi.middleware.processingengine.operator;

import it.polimi.middleware.processingengine.message.OperateMessage;

public class SplitOperator implements Operator {

    @Override
    public void operate(OperateMessage operateMessage, SendDownStreamListener listener) {
        listener.onSendDownstream(operateMessage);
    }
}
