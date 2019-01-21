package it.polimi.middleware.processingengine.operator;

import it.polimi.middleware.processingengine.function.AggregateFunction;
import it.polimi.middleware.processingengine.message.OperateMessage;

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
    public void operate(OperateMessage operateMessage, SendDownStreamListener listener) {
        addToWindow(operateMessage);
        if (windowIsFull(operateMessage.getKey())) {
            OperateMessage result = aggregateFunction.aggregate(operateMessage.getKey(), getWindow(operateMessage.getKey()));
            listener.onSendDownstream(result);
            slideWindow(operateMessage.getKey());
        }
    }

    private void addToWindow(OperateMessage operateMessage) {
        getWindow(operateMessage.getKey()).add(operateMessage.getValue());
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
