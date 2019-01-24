package it.polimi.middleware.processingengine;

import akka.actor.ActorRef;
import it.polimi.middleware.processingengine.message.OperateMessage;

import java.util.Random;

@Deprecated
public class StreamGenerator implements Runnable {

    private final ActorRef downstream;

    private final int keySize;
    private final int valueSize;
    private final double messagesPerSecond;

    public StreamGenerator(ActorRef downstream, int keySize, int valueSize, double messagesPerSecond) {
        this.downstream = downstream;
        this.keySize = keySize;
        this.valueSize = valueSize;
        this.messagesPerSecond = messagesPerSecond;
    }

    @Override
    public void run() {
        while (true) {
            String key = randomString(keySize);
            String value = randomString(valueSize);

            OperateMessage operateMessage = new OperateMessage(new KeyValuePair(key, value));
            downstream.tell(operateMessage, ActorRef.noSender());
            try {
                Thread.sleep((long) (1000 / messagesPerSecond));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private String randomString(int length) {
        int leftLimit = 97;
        int rightLimit = 122;

        Random random = new Random();
        StringBuilder buffer = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }
}
