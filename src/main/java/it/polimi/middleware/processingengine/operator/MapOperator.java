package it.polimi.middleware.processingengine.operator;

import it.polimi.middleware.processingengine.Message;
import it.polimi.middleware.processingengine.Worker;
import it.polimi.middleware.processingengine.function.MapFunction;

public class MapOperator extends Operator {

    private final MapFunction mapFunction;

    public MapOperator(Worker parent, MapFunction mapFunction) {
        super(parent);
        this.mapFunction = mapFunction;
    }

    @Override
    public void operate(Message message) {
        tell(mapFunction.map(message));
    }
}
