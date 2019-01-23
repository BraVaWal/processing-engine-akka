package it.polimi.middleware.processingengine.operator;

import it.polimi.middleware.processingengine.message.OperateMessage;

public class CrashOperator implements Operator {

    @Override
    public void operate(OperateMessage operateMessage, SendDownStreamListener listener) {
        if (operateMessage.getKeyValuePair().getValue().equals("crash")) {
            throw new RuntimeException("crash");
        }
        listener.onSendDownstream(operateMessage);
    }
}
