package it.polimi.middleware.processingengine.operator;

import it.polimi.middleware.processingengine.message.OperateMessage;

public interface SendDownStreamListener {

    void onSendDownstream(OperateMessage operateMessage);

}
