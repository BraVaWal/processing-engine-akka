package it.polimi.middleware.processingengine.function;

import it.polimi.middleware.processingengine.KeyValuePair;
import it.polimi.middleware.processingengine.message.OperateMessage;

public interface MapFunction {

    KeyValuePair map(KeyValuePair keyValuePair);

}
