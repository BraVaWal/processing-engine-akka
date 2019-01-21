package it.polimi.middleware.processingengine;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import it.polimi.middleware.processingengine.message.AddDownstreamMessage;
import it.polimi.middleware.processingengine.message.AddOperatorMessage;
import it.polimi.middleware.processingengine.operator.Operator;
import it.polimi.middleware.processingengine.worker.Worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupervisorActor extends AbstractActor {

    private Map<String, ActorRef> workers = new HashMap<>();

    public SupervisorActor(ActorRef source, ActorRef sink) {
        workers.put("source", source);
        workers.put("sink", sink);
    }

    public static Props props(ActorRef source, ActorRef sink) {
        return Props.create(SupervisorActor.class, source, sink);
    }

    private void addDownstreamOperator(ActorRef source, ActorRef downstream) {
        source.tell(new AddDownstreamMessage(downstream), self());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(AddOperatorMessage.class, this::onAddOperatorMessage)
                .build();
    }

    private void onAddOperatorMessage(AddOperatorMessage message) {
        List<ActorRef> sources = getMultipleWorkers(message.getSources());
        List<ActorRef> downstream = getMultipleWorkers(message.getDownstream());

        Operator operator = message.getOperatorType().getOperator();
        ActorRef worker = getContext().actorOf(Worker.props(message.getId(), operator, downstream));
        workers.put(message.getId(), worker);

        for (ActorRef s : sources) {
            addDownstreamOperator(s, worker);
        }
    }

    private List<ActorRef> getMultipleWorkers(List<String> keys) {
        List<ActorRef> result = new ArrayList<>(keys.size());
        for (String s : keys) {
            if (workers.containsKey(s)) {
                result.add(workers.get(s));
            }
        }
        return result;
    }
}
