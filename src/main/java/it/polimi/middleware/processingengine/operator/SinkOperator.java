package it.polimi.middleware.processingengine.operator;

import it.polimi.middleware.processingengine.message.OperateMessage;

public class SinkOperator implements Operator {

    @Override
    public void operate(OperateMessage operateMessage, SendDownStreamListener listener) {
        System.out.println("--- OperateMessage received: " + operateMessage + " ---");
    }
}
