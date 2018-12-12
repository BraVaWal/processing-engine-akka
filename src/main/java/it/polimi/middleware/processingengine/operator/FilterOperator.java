package it.polimi.middleware.processingengine.operator;

import it.polimi.middleware.processingengine.Message;
import it.polimi.middleware.processingengine.Worker;

import java.util.function.Predicate;

public class FilterOperator extends Operator {

    private final Predicate<Message> predicate;

    public FilterOperator(Worker worker, Predicate<Message> predicate) {
        super(worker);
        this.predicate = predicate;
    }


    @Override
    public void operate(Message message) {
        if (predicate.test(message)) {
            tellWorker(message);
        }
    }
}
