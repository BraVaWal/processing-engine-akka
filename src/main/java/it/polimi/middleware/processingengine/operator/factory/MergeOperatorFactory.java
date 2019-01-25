package it.polimi.middleware.processingengine.operator.factory;

import it.polimi.middleware.processingengine.operator.MergeOperator;
import it.polimi.middleware.processingengine.operator.Operator;

import java.io.Serializable;

public class MergeOperatorFactory implements OperatorFactory, Serializable {

    @Override
    public Operator build() {
        return new MergeOperator();
    }
}
