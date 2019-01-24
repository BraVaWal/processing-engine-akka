package it.polimi.middleware.processingengine.message;

import it.polimi.middleware.processingengine.operator.factory.OperatorFactory;

public class AddOperatorMessage {

    private final String id;
    private final String sourceId;
    private final String downstreamId;
    private final OperatorFactory operatorFactory;
    private final int partitions;


    public AddOperatorMessage(String id, String sourceId, String downstreamId, OperatorFactory operatorFactory, int partitions) {
        this.id = id;
        this.sourceId = sourceId;
        this.downstreamId = downstreamId;
        this.operatorFactory = operatorFactory;
        this.partitions = partitions;
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

    public int getPartitions() {
        return partitions;
    }
}
