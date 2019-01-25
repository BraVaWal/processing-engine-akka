package it.polimi.middleware.processingengine.operator.factory;

import it.polimi.middleware.processingengine.operator.Operator;
import it.polimi.middleware.processingengine.operator.SplitOperator;

import java.io.Serializable;

public class SplitOperatorFactory implements OperatorFactory, Serializable {
    @Override
    public Operator build() {
        return new SplitOperator();
    }
}
