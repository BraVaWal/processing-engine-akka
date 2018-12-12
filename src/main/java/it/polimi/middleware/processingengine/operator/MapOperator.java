package it.polimi.middleware.processingengine.operator;

import it.polimi.middleware.processingengine.Message;
import it.polimi.middleware.processingengine.Worker;
import it.polimi.middleware.processingengine.function.MapFunction;

public class MapOperator extends Operator {

    private final MapFunction mapFunction;

    public MapOperator(Worker worker, MapFunction mapFunction) {
        super(worker);
        this.mapFunction = mapFunction;
    }

    @Override
    public void operate(Message message) {
        tellWorker(mapFunction.map(message));
    }
}
