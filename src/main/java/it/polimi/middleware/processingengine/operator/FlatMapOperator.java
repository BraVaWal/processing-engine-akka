package it.polimi.middleware.processingengine.operator;

import it.polimi.middleware.processingengine.message.Message;
import it.polimi.middleware.processingengine.function.FlatMapFunction;

import java.util.Collection;

public class FlatMapOperator implements Operator {

    private final FlatMapFunction flatMapFunction;

    public FlatMapOperator(FlatMapFunction flatMapFunction) {
        this.flatMapFunction = flatMapFunction;
    }

    @Override
    public void operate(Message message, SendDownStreamListener listener) {
        Collection<Message> result = flatMapFunction.flatMap(message);
        for (Message m : result) {
            listener.onSendDownstream(m);
        }
    }
}
