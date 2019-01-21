package it.polimi.middleware.processingengine.function;

import it.polimi.middleware.processingengine.KeyValuePair;
import it.polimi.middleware.processingengine.message.OperateMessage;

import java.util.Collection;

public interface FlatMapFunction {

    Collection<KeyValuePair> flatMap(KeyValuePair keyValuePair);

}
