package it.polimi.middleware.processingengine;

import it.polimi.middleware.processingengine.operator.Operator;

import java.util.List;

public class WorkerStatusDTO {

    private final String actorRef;
    private final Operator operator;
    private final List<String> downstream;


    public WorkerStatusDTO(String actorRef, Operator operator, List<String> downstream) {
        this.actorRef = actorRef;
        this.operator = operator;
        this.downstream = downstream;
    }


    public String getActorRef() {
        return actorRef;
    }

    public Operator getOperator() {
        return operator;
    }

    public List<String> getDownstream() {
        return downstream;
    }
}
