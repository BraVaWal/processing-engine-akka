package it.polimi.middleware.processingengine.operator;

import it.polimi.middleware.processingengine.Message;

public interface SendDownStreamListener {

    void onSendDownstream(Message message);

}
