package it.polimi.middleware.processingengine.operator.factory;

import it.polimi.middleware.processingengine.KeyValuePair;
import it.polimi.middleware.processingengine.operator.FilterOperator;
import it.polimi.middleware.processingengine.operator.Operator;

import java.util.function.Predicate;

public class FilterOperatorFactory implements OperatorFactory {

    private final Predicate<KeyValuePair> predicate;

    public FilterOperatorFactory(Predicate<KeyValuePair> predicate) {
        this.predicate = predicate;
    }

    @Override
    public Operator build() {
        return new FilterOperator(predicate);
    }
}
