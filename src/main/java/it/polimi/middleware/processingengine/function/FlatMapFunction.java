package it.polimi.middleware.processingengine.function;

import it.polimi.middleware.processingengine.KeyValuePair;

import java.io.Serializable;
import java.util.Collection;

public interface FlatMapFunction extends Serializable {

    Collection<KeyValuePair> flatMap(KeyValuePair keyValuePair);

}
