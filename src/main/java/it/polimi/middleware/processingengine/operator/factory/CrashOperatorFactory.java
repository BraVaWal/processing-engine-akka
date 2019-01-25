package it.polimi.middleware.processingengine.operator.factory;

import it.polimi.middleware.processingengine.operator.CrashOperator;
import it.polimi.middleware.processingengine.operator.Operator;

import java.io.Serializable;

public class CrashOperatorFactory implements OperatorFactory, Serializable {
    @Override
    public Operator build() {
        return new CrashOperator();
    }
}
