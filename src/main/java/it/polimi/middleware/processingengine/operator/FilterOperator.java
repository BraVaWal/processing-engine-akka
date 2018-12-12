package it.polimi.middleware.processingengine.operator;

import it.polimi.middleware.processingengine.Message;
import it.polimi.middleware.processingengine.Worker;

import java.util.function.Predicate;

public class FilterOperator extends Operator {

    private final Predicate<Message> predicate;

    public FilterOperator(Worker parent, Predicate<Message> predicate) {
        super(parent);
        this.predicate = predicate;
    }


    @Override
    public void operate(Message message) {
        if (predicate.test(message)) {
            tell(message);
        }
    }
}
