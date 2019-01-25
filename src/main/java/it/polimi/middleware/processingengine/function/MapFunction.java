package it.polimi.middleware.processingengine.function;

import it.polimi.middleware.processingengine.KeyValuePair;

import java.io.Serializable;

public interface MapFunction extends Serializable {

    KeyValuePair map(KeyValuePair keyValuePair);

}
