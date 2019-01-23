package it.polimi.middleware.processingengine.operator.factory;

import it.polimi.middleware.processingengine.operator.Operator;
import it.polimi.middleware.processingengine.operator.SinkOperator;

public class SinkOperatorFactory implements OperatorFactory {
    @Override
    public Operator build() {
        return new SinkOperator();
    }
}
