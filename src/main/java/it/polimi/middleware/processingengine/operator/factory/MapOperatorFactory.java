package it.polimi.middleware.processingengine.operator.factory;

import it.polimi.middleware.processingengine.function.MapFunction;
import it.polimi.middleware.processingengine.operator.MapOperator;
import it.polimi.middleware.processingengine.operator.Operator;

import java.io.Serializable;

public class MapOperatorFactory implements OperatorFactory, Serializable {

    private final MapFunction function;

    public MapOperatorFactory(MapFunction function) {
        this.function = function;
    }

    @Override
    public Operator build() {
        return new MapOperator(function);
    }
}
