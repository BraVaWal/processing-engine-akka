package it.polimi.middleware.processingengine.operator;

import it.polimi.middleware.processingengine.message.OperateMessage;
import it.polimi.middleware.processingengine.function.FlatMapFunction;

import java.util.Collection;

public class FlatMapOperator implements Operator {

    private final FlatMapFunction flatMapFunction;

    public FlatMapOperator(FlatMapFunction flatMapFunction) {
        this.flatMapFunction = flatMapFunction;
    }

    @Override
    public void operate(OperateMessage operateMessage, SendDownStreamListener listener) {
        Collection<OperateMessage> result = flatMapFunction.flatMap(operateMessage);
        for (OperateMessage m : result) {
            listener.onSendDownstream(m);
        }
    }
}
