package it.polimi.middleware.processingengine.function;

import it.polimi.middleware.processingengine.message.OperateMessage;

import java.util.Collection;

public interface AggregateFunction {

    OperateMessage aggregate(String key, Collection<String> values);

}
