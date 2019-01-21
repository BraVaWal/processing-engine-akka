package it.polimi.middleware.processingengine.function;

import it.polimi.middleware.processingengine.KeyValuePair;

import java.util.Collection;

public interface FlatMapFunction {

    Collection<KeyValuePair> flatMap(KeyValuePair keyValuePair);

}
