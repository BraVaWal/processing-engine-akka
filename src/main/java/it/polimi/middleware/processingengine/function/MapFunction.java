package it.polimi.middleware.processingengine.function;

import it.polimi.middleware.processingengine.Message;

public interface MapFunction {

    Message map(Message message);

}
