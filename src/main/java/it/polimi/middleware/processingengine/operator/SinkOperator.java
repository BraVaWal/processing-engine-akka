package it.polimi.middleware.processingengine.operator;

import it.polimi.middleware.processingengine.KeyValuePair;
import it.polimi.middleware.processingengine.message.OperateMessage;

import java.io.Serializable;
import java.time.Instant;

public class SinkOperator implements Operator, Serializable {

    @Override
    public void operate(OperateMessage operateMessage, SendDownStreamListener listener) {
        final Instant instant = Instant.now();
        final String result = "--- Received: " + operateMessage + " at " + instant.toString() + " ---";
        System.out.println(result);
        listener.onSendDownstream(new OperateMessage(new KeyValuePair("Result", result)));
    }
}
