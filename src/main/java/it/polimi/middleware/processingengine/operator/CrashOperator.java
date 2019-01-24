package it.polimi.middleware.processingengine.operator;

import it.polimi.middleware.processingengine.message.OperateMessage;

public class CrashOperator implements Operator {

    private static final double crashingChance = 0.5;

    private int crashCount = 0;

    @Override
    public void operate(OperateMessage operateMessage, SendDownStreamListener listener) {
        if (operateMessage.getKeyValuePair().getValue().equalsIgnoreCase("crash")) {
            if (Math.random() < crashingChance) {
                crashCount++;
                throw new RuntimeException("crash");
            }
        }
        listener.onSendDownstream(operateMessage);
    }
}
