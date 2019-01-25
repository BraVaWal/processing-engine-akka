package it.polimi.middleware.processingengine.message;

import java.io.Serializable;
import java.util.UUID;

public class AcknowledgeMessage implements Serializable {

    private final UUID id;

    public AcknowledgeMessage(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
