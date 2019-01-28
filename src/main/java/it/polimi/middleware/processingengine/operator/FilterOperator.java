package it.polimi.middleware.processingengine.operator;

import it.polimi.middleware.processingengine.KeyValuePair;
import it.polimi.middleware.processingengine.message.OperateMessage;

import java.io.Serializable;
import java.util.function.Predicate;

public class FilterOperator implements Operator, Serializable {

    private final Predicate<KeyValuePair> predicate;

    public FilterOperator(Predicate<KeyValuePair> predicate) {
        this.predicate = predicate;
    }

    @Override
    public void operate(OperateMessage operateMessage, SendDownStreamListener listener) {
        if (predicate.test(operateMessage.getKeyValuePair())) {
            listener.onSendDownstream(operateMessage);
        }
    }
}
