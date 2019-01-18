package it.polimi.middleware.processingengine.operator;

import it.polimi.middleware.processingengine.message.OperateMessage;

import java.util.function.Predicate;

public class FilterOperator implements Operator {

    private final Predicate<OperateMessage> predicate;

    public FilterOperator(Predicate<OperateMessage> predicate) {
        this.predicate = predicate;
    }


    @Override
    public void operate(OperateMessage operateMessage, SendDownStreamListener listener) {
        if (predicate.test(operateMessage)) {
            listener.onSendDownstream(operateMessage);
        }
    }
}
