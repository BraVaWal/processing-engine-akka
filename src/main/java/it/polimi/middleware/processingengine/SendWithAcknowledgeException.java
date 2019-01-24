package it.polimi.middleware.processingengine;

public class SendWithAcknowledgeException extends RuntimeException {

    public SendWithAcknowledgeException(String message) {
        super(message);
    }
}
