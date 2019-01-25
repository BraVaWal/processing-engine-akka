package it.polimi.middleware.processingengine.operator.factory;

import it.polimi.middleware.processingengine.operator.Operator;
import it.polimi.middleware.processingengine.operator.SinkOperator;

import java.io.Serializable;

public class SinkOperatorFactory implements OperatorFactory, Serializable {
    @Override
    public Operator build() {
        return new SinkOperator();
    }
}
