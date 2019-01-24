package it.polimi.middleware.processingengine.message;

import java.util.UUID;

public class AcknowledgeMessage {

    private final UUID id;

    public AcknowledgeMessage(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
