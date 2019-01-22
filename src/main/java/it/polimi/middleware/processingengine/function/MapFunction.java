package it.polimi.middleware.processingengine.function;

import it.polimi.middleware.processingengine.KeyValuePair;

public interface MapFunction {

    KeyValuePair map(KeyValuePair keyValuePair);

}
