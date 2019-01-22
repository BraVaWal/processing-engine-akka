package it.polimi.middleware.processingengine.message;

import it.polimi.middleware.processingengine.KeyValuePair;

import java.util.Collection;

public class AddJobMessage {

    private final Collection<KeyValuePair> data;

    public AddJobMessage(Collection<KeyValuePair> data) {
        this.data = data;
    }

    public Collection<KeyValuePair> getData() {
        return data;
    }
}
