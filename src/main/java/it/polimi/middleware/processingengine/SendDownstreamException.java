package it.polimi.middleware.processingengine;

import akka.actor.ActorRef;
import it.polimi.middleware.processingengine.message.OperateMessage;

import java.util.List;

public class SendDownstreamException extends RuntimeException {

    private final OperateMessage operateMessage;
    private final List<ActorRef> downstream;

    public SendDownstreamException(OperateMessage operateMessage, List<ActorRef> downstream) {
        this.operateMessage = operateMessage;
        this.downstream = downstream;
    }

    public OperateMessage getOperateMessage() {
        return operateMessage;
    }

    public List<ActorRef> getDownstream() {
        return downstream;
    }
}
