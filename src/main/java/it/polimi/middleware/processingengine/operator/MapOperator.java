package it.polimi.middleware.processingengine.operator;

import it.polimi.middleware.processingengine.function.MapFunction;
import it.polimi.middleware.processingengine.message.OperateMessage;

import java.io.Serializable;

public class MapOperator implements Operator, Serializable {

    private final MapFunction mapFunction;

    public MapOperator(MapFunction mapFunction) {
        this.mapFunction = mapFunction;
    }

    @Override
    public void operate(OperateMessage operateMessage, SendDownStreamListener listener) {
        listener.onSendDownstream(new OperateMessage(mapFunction.map(operateMessage.getKeyValuePair())));
    }
}
