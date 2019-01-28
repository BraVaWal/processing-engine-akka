package it.polimi.middleware.processingengine.operator;

import it.polimi.middleware.processingengine.message.OperateMessage;

import java.io.Serializable;

public class SplitOperator implements Operator, Serializable {

    @Override
    public void operate(OperateMessage operateMessage, SendDownStreamListener listener) {
        listener.onSendDownstream(operateMessage);
    }
}
