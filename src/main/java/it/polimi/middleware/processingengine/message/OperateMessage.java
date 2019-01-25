package it.polimi.middleware.processingengine.message;

import it.polimi.middleware.processingengine.KeyValuePair;

import java.io.Serializable;
import java.util.UUID;

public class OperateMessage implements Serializable {

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
