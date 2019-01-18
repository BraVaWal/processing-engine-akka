package it.polimi.middleware.processingengine.operator;

import it.polimi.middleware.processingengine.message.OperateMessage;
import it.polimi.middleware.processingengine.function.MapFunction;

public class MapOperator implements Operator {

    private final MapFunction mapFunction;

    public MapOperator(MapFunction mapFunction) {
        this.mapFunction = mapFunction;
    }

    @Override
    public void operate(OperateMessage operateMessage, SendDownStreamListener listener) {
        listener.onSendDownstream(mapFunction.map(operateMessage));
    }
}
