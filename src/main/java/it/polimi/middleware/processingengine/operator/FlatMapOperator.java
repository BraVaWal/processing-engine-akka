package it.polimi.middleware.processingengine.operator;

import it.polimi.middleware.processingengine.KeyValuePair;
import it.polimi.middleware.processingengine.function.FlatMapFunction;
import it.polimi.middleware.processingengine.message.OperateMessage;

import java.io.Serializable;
import java.util.Collection;

public class FlatMapOperator implements Operator, Serializable {

    private final FlatMapFunction flatMapFunction;

    public FlatMapOperator(FlatMapFunction flatMapFunction) {
        this.flatMapFunction = flatMapFunction;
    }

    @Override
    public void operate(OperateMessage operateMessage, SendDownStreamListener listener) {
        final Collection<KeyValuePair> result = flatMapFunction.flatMap(operateMessage.getKeyValuePair());
        for (KeyValuePair m : result) {
            listener.onSendDownstream(new OperateMessage(m));
        }
    }
}
