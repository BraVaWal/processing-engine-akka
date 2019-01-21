package it.polimi.middleware.processingengine.message;

import it.polimi.middleware.processingengine.KeyValuePair;

public class OperateMessage {

    private final KeyValuePair keyValuePair;

    public OperateMessage(KeyValuePair pair) {
        this.keyValuePair = pair;
    }

    public KeyValuePair getKeyValuePair() {
        return keyValuePair;
    }
}
