package it.polimi.middleware.processingengine.function;

import it.polimi.middleware.processingengine.KeyValuePair;

import java.io.Serializable;
import java.util.Collection;

public interface AggregateFunction extends Serializable {

    KeyValuePair aggregate(String key, Collection<String> values);

}
