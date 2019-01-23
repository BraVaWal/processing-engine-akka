package it.polimi.middleware.processingengine.operator.factory;

import it.polimi.middleware.processingengine.function.FlatMapFunction;
import it.polimi.middleware.processingengine.operator.FlatMapOperator;
import it.polimi.middleware.processingengine.operator.Operator;

public class FlatMapOperatorFactory implements OperatorFactory {

    private final FlatMapFunction function;

    public FlatMapOperatorFactory(FlatMapFunction function) {
        this.function = function;
    }

    @Override
    public Operator build() {
        return new FlatMapOperator(function);
    }
}
