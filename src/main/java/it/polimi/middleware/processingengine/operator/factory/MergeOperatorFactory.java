package it.polimi.middleware.processingengine.operator.factory;

import it.polimi.middleware.processingengine.operator.MergeOperator;
import it.polimi.middleware.processingengine.operator.Operator;

public class MergeOperatorFactory implements OperatorFactory {

    @Override
    public Operator build() {
        return new MergeOperator();
    }
}
