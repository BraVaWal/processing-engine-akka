package it.polimi.middleware.processingengine.function;

import it.polimi.middleware.processingengine.KeyValuePair;

import java.util.Collection;

public interface AggregateFunction {

    KeyValuePair aggregate(String key, Collection<String> values);

}
