package it.polimi.middleware.processingengine.message;

import akka.actor.ActorRef;
import it.polimi.middleware.processingengine.operator.Operator;

import java.util.List;

public class AddOperatorMessage {

    private final String id;
    private final String sourceId;
    private final String downstreamId;
    private final Operator operator;


    public AddOperatorMessage(String id, String sourceId, String downstreamId, Operator operator) {
        this.id = id;
        this.sourceId = sourceId;
        this.downstreamId = downstreamId;
        this.operator = operator;
    }

    public String getId() {
        return id;
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getDownstreamId() {
        return downstreamId;
    }

    public Operator getOperator() {
        return operator;
    }
}
