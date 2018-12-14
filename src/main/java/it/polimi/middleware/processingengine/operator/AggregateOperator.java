package it.polimi.middleware.processingengine.operator;

import it.polimi.middleware.processingengine.message.Message;
import it.polimi.middleware.processingengine.function.AggregateFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AggregateOperator implements Operator {

    private final AggregateFunction aggregateFunction;

    private final int windowSize;
    private final int windowSlide;

    private final Map<String, List<String>> windows;

    public AggregateOperator(AggregateFunction aggregateFunction, int windowSize, int windowSlide) {
        this.aggregateFunction = aggregateFunction;
        this.windowSize = windowSize;
        this.windowSlide = windowSlide;
        this.windows = new HashMap<>();
    }

    @Override
    public void operate(Message message, SendDownStreamListener listener) {
        addToWindow(message);
        if (windowIsFull(message.getKey())) {
            Message result = aggregateFunction.aggregate(message.getKey(), getWindow(message.getKey()));
            listener.onSendDownstream(result);
            slideWindow(message.getKey());
        }
    }

    private void addToWindow(Message message) {
        getWindow(message.getKey()).add(message.getValue());
    }

    private List<String> getWindow(String key) {
        return windows.computeIfAbsent(key, k -> new ArrayList<>(windowSize));
    }

    private boolean windowIsFull(String key) {
        return getWindow(key).size() == windowSize;
    }

    private void slideWindow(String key) {
        windows.put(key, getWindow(key).subList(windowSlide, windowSize));
    }
}
