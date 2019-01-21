package it.polimi.middleware.processingengine;

public class OperatorDTO {

    private final String id;
    private final String sourceId;
    private final String sinkId;
    private final OperatorType operatorType;

    public OperatorDTO(String id, String sourceId, String sinkId, OperatorType operatorType) {
        this.id = id;
        this.sourceId = sourceId;
        this.sinkId = sinkId;
        this.operatorType = operatorType;
    }


    public String getId() {
        return id;
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getSinkId() {
        return sinkId;
    }

    public OperatorType getOperatorType() {
        return operatorType;
    }
}
