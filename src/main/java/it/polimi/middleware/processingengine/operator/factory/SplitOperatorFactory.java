package it.polimi.middleware.processingengine.operator.factory;

import it.polimi.middleware.processingengine.operator.Operator;
import it.polimi.middleware.processingengine.operator.SplitOperator;

public class SplitOperatorFactory implements OperatorFactory {
    @Override
    public Operator build() {
        return new SplitOperator();
    }
}
