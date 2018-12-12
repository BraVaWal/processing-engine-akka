package it.polimi.middleware.processingengine.operator;

import it.polimi.middleware.processingengine.Message;

public interface Operator {

    void operate(Message message, SendDownStreamListener listener);

}
