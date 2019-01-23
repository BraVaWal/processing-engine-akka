package it.polimi.middleware.processingengine.message;

import akka.actor.ActorRef;
import it.polimi.middleware.processingengine.operator.Operator;
import it.polimi.middleware.processingengine.operator.factory.OperatorFactory;

import java.util.List;

public class AddOperatorMessage {

    private final String id;
    private final String sourceId;
    private final String downstreamId;
    private final OperatorFactory operatorFactory;


    public AddOperatorMessage(String id, String sourceId, String downstreamId, OperatorFactory operatorFactory) {
        this.id = id;
        this.sourceId = sourceId;
        this.downstreamId = downstreamId;
        this.operatorFactory = operatorFactory;
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

    public OperatorFactory getOperatorFactory() {
        return operatorFactory;
    }
}
