package it.polimi.middleware.processingengine.function;

import it.polimi.middleware.processingengine.message.Message;

public interface MapFunction {

    Message map(Message message);

}
