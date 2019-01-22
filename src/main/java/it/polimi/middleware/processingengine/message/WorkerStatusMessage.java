package it.polimi.middleware.processingengine.message;

import it.polimi.middleware.processingengine.operator.Operator;

import java.util.List;

public class WorkerStatusMessage {

    private final String id;
    private final Operator operator;
    private final List<String> downstream;

    public WorkerStatusMessage(String id, Operator operator, List<String> downstream) {
        this.id = id;
        this.operator = operator;
        this.downstream = downstream;
    }

    public Operator getOperator() {
        return operator;
    }

    public List<String> getDownstream() {
        return downstream;
    }
}
