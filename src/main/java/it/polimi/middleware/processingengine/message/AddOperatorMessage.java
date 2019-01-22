package it.polimi.middleware.processingengine.message;

import akka.actor.ActorRef;
import it.polimi.middleware.processingengine.operator.Operator;

import java.util.List;

public class AddOperatorMessage {

    private final List<ActorRef> sources;
    private final List<ActorRef> downstream;
    private final Operator operator;


    public AddOperatorMessage(List<ActorRef> sources, List<ActorRef> downstream, Operator operator) {
        this.sources = sources;
        this.downstream = downstream;
        this.operator = operator;
    }

    public List<ActorRef> getSources() {
        return sources;
    }

    public List<ActorRef> getDownstream() {
        return downstream;
    }

    public Operator getOperator() {
        return operator;
    }
}
