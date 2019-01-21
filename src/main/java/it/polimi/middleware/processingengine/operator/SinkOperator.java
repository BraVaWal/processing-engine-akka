package it.polimi.middleware.processingengine.operator;

import it.polimi.middleware.processingengine.message.OperateMessage;

import java.time.Instant;

public class SinkOperator implements Operator {

    @Override
    public void operate(OperateMessage operateMessage, SendDownStreamListener listener) {
        Instant instant = Instant.now();
        System.out.println("--- Received: " + operateMessage + " at " + instant.toString() + " ---");
    }
}
