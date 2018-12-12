package it.polimi.middleware.processingengine.operator;

import it.polimi.middleware.processingengine.Message;

import java.util.function.Predicate;

public class FilterOperator implements Operator {

    private final Predicate<Message> predicate;

    public FilterOperator(Predicate<Message> predicate) {
        this.predicate = predicate;
    }


    @Override
    public void operate(Message message, SendDownStreamListener listener) {
        if (predicate.test(message)) {
            listener.onSendDownstream(message);
        }
    }
}
