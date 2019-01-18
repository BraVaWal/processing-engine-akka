package it.polimi.middleware.processingengine.operator;

import it.polimi.middleware.processingengine.message.OperateMessage;

public interface Operator {

    void operate(OperateMessage operateMessage, SendDownStreamListener listener);

}
