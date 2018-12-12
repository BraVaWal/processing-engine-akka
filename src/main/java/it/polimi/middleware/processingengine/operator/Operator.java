package it.polimi.middleware.processingengine.operator;

import it.polimi.middleware.processingengine.Message;

public abstract class Operator {

    public abstract void operate(Message message, SendDownStreamListener listener);

}
