package it.polimi.middleware.processingengine;

import it.polimi.middleware.processingengine.operator.AggregateOperator;
import it.polimi.middleware.processingengine.operator.Operator;
import it.polimi.middleware.processingengine.operator.SplitOperator;

public enum OperatorType {
    AGGREGATE, FILTER, FLATMAP, MAP, MERGE, SPLIT;

    //TODO: Implement correct operator implementation
    public Operator getOperator() {
        return new SplitOperator();
    }
}
