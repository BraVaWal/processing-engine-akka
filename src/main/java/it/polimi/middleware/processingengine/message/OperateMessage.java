package it.polimi.middleware.processingengine.message;

import it.polimi.middleware.processingengine.KeyValuePair;

import java.util.UUID;

public class OperateMessage {

    private final UUID id = UUID.randomUUID();
    private final KeyValuePair keyValuePair;

    public OperateMessage(KeyValuePair pair) {
        this.keyValuePair = pair;
    }

    public UUID getId() {
        return id;
    }

    public KeyValuePair getKeyValuePair() {
        return keyValuePair;
    }

    @Override
    public String toString() {
        return "OperateMessage{" +
                "keyValuePair=" + keyValuePair +
                '}';
    }
}
