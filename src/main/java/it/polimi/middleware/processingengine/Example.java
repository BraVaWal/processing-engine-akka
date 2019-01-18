package it.polimi.middleware.processingengine;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import it.polimi.middleware.processingengine.message.OperateMessage;
import it.polimi.middleware.processingengine.operator.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Example {

    public static void main(String[] args) {
        String requestedWord = args[0].toLowerCase();

        final ActorSystem system = ActorSystem.create("System");

        ActorRef sink = createWorker(system, new ArrayList<>(), new SinkOperator());
        ActorRef map = createWorker(system, Collections.singletonList(sink), new MapOperator(message -> {
            StringBuilder sb = new StringBuilder(message.getValue());
            int start = message.getValue().indexOf(requestedWord);
            int end = start + 4 + requestedWord.length();
            sb.insert(start, "--->");
            sb.insert(end, "<---");
            return new OperateMessage(message.getKey(), sb.toString());
        }));


        ActorRef merge = createWorker(system, Collections.singletonList(map), new MergeOperator());
        ActorRef filter1 = createWorker(system, Collections.singletonList(merge), new FilterOperator(message -> message.getValue().contains(requestedWord)));
        ActorRef filter2 = createWorker(system, Collections.singletonList(merge), new FilterOperator(message -> message.getValue().contains(requestedWord)));
        ActorRef filter3 = createWorker(system, Collections.singletonList(merge), new FilterOperator(message -> message.getValue().contains(requestedWord)));
        ActorRef filter4 = createWorker(system, Collections.singletonList(merge), new FilterOperator(message -> message.getValue().contains(requestedWord)));
        ActorRef filter5 = createWorker(system, Collections.singletonList(merge), new FilterOperator(message -> message.getValue().contains(requestedWord)));
        ActorRef filter6 = createWorker(system, Collections.singletonList(merge), new FilterOperator(message -> message.getValue().contains(requestedWord)));
        ActorRef filter7 = createWorker(system, Collections.singletonList(merge), new FilterOperator(message -> message.getValue().contains(requestedWord)));
        ActorRef filter8 = createWorker(system, Collections.singletonList(merge), new FilterOperator(message -> message.getValue().contains(requestedWord)));

        StreamGenerator generator1 = new StreamGenerator(filter1, 1, 32, 1000);
        StreamGenerator generator2 = new StreamGenerator(filter2, 1, 32, 1000);
        StreamGenerator generator3 = new StreamGenerator(filter3, 1, 32, 1000);
        StreamGenerator generator4 = new StreamGenerator(filter4, 1, 32, 1000);
        StreamGenerator generator5 = new StreamGenerator(filter5, 1, 32, 1000);
        StreamGenerator generator6 = new StreamGenerator(filter6, 1, 32, 1000);
        StreamGenerator generator7 = new StreamGenerator(filter7, 1, 32, 1000);
        StreamGenerator generator8 = new StreamGenerator(filter8, 1, 32, 1000);

        new Thread(generator1, "Generator1").start();
        new Thread(generator2, "Generator2").start();
        new Thread(generator3, "Generator3").start();
        new Thread(generator4, "Generator4").start();
        new Thread(generator5, "Generator5").start();
        new Thread(generator6, "Generator6").start();
        new Thread(generator7, "Generator7").start();
        new Thread(generator8, "Generator8").start();
    }

    private static ActorRef createWorker(ActorSystem system, List<ActorRef> downstreamWorkers, Operator operator) {
        return system.actorOf(Worker.props(downstreamWorkers, operator));
    }

}
