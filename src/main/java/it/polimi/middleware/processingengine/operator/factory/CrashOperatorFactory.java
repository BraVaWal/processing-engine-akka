package it.polimi.middleware.processingengine.operator.factory;

import it.polimi.middleware.processingengine.operator.CrashOperator;
import it.polimi.middleware.processingengine.operator.Operator;

public class CrashOperatorFactory implements OperatorFactory {
    @Override
    public Operator build() {
        return new CrashOperator();
    }
}
