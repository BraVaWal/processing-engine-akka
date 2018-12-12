package it.polimi.middleware.processingengine.operator;

import it.polimi.middleware.processingengine.Message;

public class SinkOperator implements Operator {

    @Override
    public void operate(Message message, SendDownStreamListener listener) {
        System.out.println("--- Message received: [" + message.getKey() + ", " + message.getValue() + "] ---");
    }
}
