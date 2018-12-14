package it.polimi.middleware.processingengine.message;

public class OperateMessage {

    private final String key;
    private final String value;

    public OperateMessage(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
