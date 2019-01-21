package it.polimi.middleware.processingengine.message;

import it.polimi.middleware.processingengine.OperatorType;

import java.util.Collection;
import java.util.List;

public class AddOperatorMessage {

    private final String id;
    private final List<String> sources;
    private final List<String> downstream;
    private final OperatorType operatorType;

    public AddOperatorMessage(String id, List<String> sources, List<String> downstream, OperatorType operatorType) {
        this.id = id;
        this.sources = sources;
        this.downstream = downstream;
        this.operatorType = operatorType;
    }

    public String getId() {
        return id;
    }

    public OperatorType getOperatorType() {
        return operatorType;
    }

    public List<String> getSources() {
        return sources;
    }

    public List<String> getDownstream() {
        return downstream;
    }
}
