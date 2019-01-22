package it.polimi.middleware.processingengine.message;

import akka.actor.ActorRef;
import it.polimi.middleware.processingengine.operator.Operator;

import java.util.List;

public class AddOperatorMessage {

    private final String id;
    private final List<String> sourceIds;
    private final List<String> downstreamIds;
    private final Operator operator;


    public AddOperatorMessage(String id, List<String> sourceIds, List<String> downstreamIds, Operator operator) {
        this.id = id;
        this.sourceIds = sourceIds;
        this.downstreamIds = downstreamIds;
        this.operator = operator;
    }

    public String getId() {
        return id;
    }

    public List<String> getSourceIds() {
        return sourceIds;
    }

    public List<String> getDownstreamIds() {
        return downstreamIds;
    }

    public Operator getOperator() {
        return operator;
    }
}
