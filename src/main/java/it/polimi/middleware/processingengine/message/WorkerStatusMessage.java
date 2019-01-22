package it.polimi.middleware.processingengine.message;

import akka.actor.ActorRef;
import it.polimi.middleware.processingengine.operator.Operator;

import java.util.List;

public class WorkerStatusMessage {

    private final Operator operator;
    private final List<ActorRef> downstream;

    public WorkerStatusMessage(Operator operator, List<ActorRef> downstream) {
        this.operator = operator;
        this.downstream = downstream;
    }

    public Operator getOperator() {
        return operator;
    }

    public List<ActorRef> getDownstream() {
        return downstream;
    }
}
