package it.polimi.middleware.processingengine.function;

import it.polimi.middleware.processingengine.message.Message;

import java.util.Collection;

public interface FlatMapFunction {

    Collection<Message> flatMap(Message message);

}
