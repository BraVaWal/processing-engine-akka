package it.polimi.middleware.processingengine.function;

import it.polimi.middleware.processingengine.Message;

import java.util.Collection;

public interface AggregateFunction {

    Message aggregate(String key, Collection<String> values);

}
