package it.polimi.middleware.processingengine.operator;

import it.polimi.middleware.processingengine.message.OperateMessage;

public class MergeOperator implements Operator {

    public MergeOperator() {

    }

    @Override
    public void operate(OperateMessage operateMessage, SendDownStreamListener listener) {
        listener.onSendDownstream(operateMessage);
    }
}
