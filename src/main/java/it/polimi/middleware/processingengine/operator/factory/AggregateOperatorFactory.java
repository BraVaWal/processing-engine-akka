package it.polimi.middleware.processingengine.operator.factory;

import it.polimi.middleware.processingengine.function.AggregateFunction;
import it.polimi.middleware.processingengine.operator.AggregateOperator;
import it.polimi.middleware.processingengine.operator.Operator;

import java.io.Serializable;

public class AggregateOperatorFactory implements OperatorFactory, Serializable {

    private final AggregateFunction function;
    private final int windowSize;
    private final int windowSlide;

    public AggregateOperatorFactory(AggregateFunction function, int windowSize, int windowSlide) {
        this.function = function;
        this.windowSize = windowSize;
        this.windowSlide = windowSlide;
    }

    @Override
    public Operator build() {
        return new AggregateOperator(function, windowSize, windowSlide);
    }
}
