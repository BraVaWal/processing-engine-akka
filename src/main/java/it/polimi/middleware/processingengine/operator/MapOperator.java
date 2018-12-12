package it.polimi.middleware.processingengine.operator;

import it.polimi.middleware.processingengine.Message;
import it.polimi.middleware.processingengine.function.MapFunction;

public class MapOperator extends Operator {

    private final MapFunction mapFunction;

    public MapOperator(MapFunction mapFunction) {
        this.mapFunction = mapFunction;
    }

    @Override
    public void operate(Message message, SendDownStreamListener listener) {
        listener.onSendDownstream(mapFunction.map(message));
    }
}
