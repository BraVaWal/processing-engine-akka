package it.polimi.middleware.processingengine;

public class OperatorCrashException extends RuntimeException {

    public OperatorCrashException() {
        super("Operator crash");
    }

}
