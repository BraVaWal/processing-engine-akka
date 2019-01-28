package it.polimi.middleware.processingengine.operator;

import it.polimi.middleware.processingengine.KeyValuePair;
import it.polimi.middleware.processingengine.function.AggregateFunction;
import it.polimi.middleware.processingengine.message.OperateMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AggregateOperator implements Operator, Serializable {

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
        final KeyValuePair pair = operateMessage.getKeyValuePair();
        addToWindow(pair);
        if (windowIsFull(pair.getKey())) {
            final OperateMessage result = new OperateMessage(aggregateFunction.aggregate(pair.getKey(), getWindow(pair.getKey())));
            listener.onSendDownstream(result);
            slideWindow(pair.getKey());
        }
    }

    private void addToWindow(KeyValuePair operateMessage) {
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
