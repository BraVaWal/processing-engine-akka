package it.polimi.middleware.processingengine.message;

import it.polimi.middleware.processingengine.operator.Operator;

import java.io.Serializable;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

public class WorkerStatusMessage implements Serializable {

    private final String id;
    private final Operator operator;
    private final List<String> downstream;
    private final Queue<UUID> operated;
    private final OperateMessage lastReceivedNotOperated;
    private final OperateMessage lastReceivedOperated;

    public WorkerStatusMessage(String id, Operator operator, List<String> downstream, Queue<UUID> operated, OperateMessage lastReceivedNotOperated, OperateMessage lastReceivedOperated) {
        this.id = id;
        this.operator = operator;
        this.downstream = downstream;
        this.operated = operated;
        this.lastReceivedNotOperated = lastReceivedNotOperated;
        this.lastReceivedOperated = lastReceivedOperated;
    }

    public String getId() {
        return id;
    }

    public Operator getOperator() {
        return operator;
    }

    public List<String> getDownstream() {
        return downstream;
    }

    public Queue<UUID> getOperated() {
        return operated;
    }

    public OperateMessage getLastReceivedNotOperated() {
        return lastReceivedNotOperated;
    }

    public OperateMessage getLastReceivedOperated() {
        return lastReceivedOperated;
    }
}
