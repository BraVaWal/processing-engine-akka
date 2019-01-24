# Processing Engine using Akka

## Exactly-once compution

In order to make sure all jobs are computed once and only once, there is a system in place to ensure exactly-once computation for each job. This system consists of at least once message delivery and ignoring of duplicate messages.

### At least once delivery of messages

The [Workers](../master/src/main/java/it/polimi/middleware/processingengine/worker/Worker.java) (actors which have a certain operator) send an [`OperateMessage`](../master/src/main/java/it/polimi/middleware/processingengine/message/OperateMessage.java) to the downstream workers. This OperateMessage consists of an ID and the key-value pair to be operated by the next operator. When a worker sends this message downstream it asks for an acknowledgement via the Akka ask pattern:

```java
final Future<Object> ack = Patterns.ask(receiver, operateMessage, 1000);
AcknowledgeMessage message = (AcknowledgeMessage) Await.result(ack, Duration.create(1000, TimeUnit.MILLISECONDS));
```

If the `Future` does not succeed it tries again, until it succeeds or reaches a maximum number of retries. If it succeeds the sender receives an [`AcknowledgeMessage`](../master/src/main/java/it/polimi/middleware/processingengine/message/AcknowledgeMessage.java). This message contains the ID of the message it acknowledges. The sender then checks wether or not this acknowledge corresponds to the right message and if it does, knows the message is delivered at least once.

### Ensure only once execution of job

The workers also have a system in place to ensure when the same message is received twice, it does not compute it twice. Each worker has the following fields:

```java
public class Worker extends AbstractActor {
    private final Queue<UUID> operated = new LinkedList<>();
    private OperateMessage lastReceivedNotOperated = null;
    private OperateMessage lastReceivedOperated = null;
}
```

The `operated` field is queue containing the IDs of all received and operated messages. When receiving an `OperateMessage` it checks whether or not it has already processed that message and ignores it if so. It might be that the worker fails during the processing of the message. This can happen during the operator operating the message or during sending the result to the next operator. Regardless of the type of failure, the [`SupervisorActor`](../master/src/main/java/it/polimi/middleware/processingengine/SupervisorActor.java) restarts the worker using the Akka `SupervisorStrategy`:

```java
@Override
public SupervisorStrategy supervisorStrategy() {
    return new OneForOneStrategy(
            10,
            Duration.create("10 seconds"),
            DeciderBuilder
                    .match(RuntimeException.class, ex -> SupervisorStrategy.restart())
                    .build());
}
```

#### Failure during operating

When a message is received and has not been processed, the `lastReceivedNotOperated` field gets assigned the message received. Then when during operating the operator the worker fails, the the worker gets restarted and calls the `onPostRestart` method. This method checks if the `lastReceivedNotOperated` field is `null` and tries again to operate this message:

```java
@Override
public void postRestart(Throwable reason) throws Exception {
    if (lastReceivedNotOperated != null) {
        operate(lastReceivedNotOperated);
    }
}
```

#### Failure during sending downstream

It is also possible the worker can also fail during sending the result of the operator downstream. Because it can happen between sending and receiving an acknowledgement, we must make sure after restart the same message is send again. Otherwise the worker downstream does not know this regards the same computation. To ensure this before sending, the `lastReceivedOperated` field is assigned the result of the operator. When the worker is restarted after a failure and the `postRestart` method is called again, the worker checks if this field is `null` and resends the message if not:

```java
@Override
public void postRestart(Throwable reason) throws Exception {
    if (lastReceivedNotOperated != null) {
        operate(lastReceivedNotOperated);
    } else if (lastReceivedOperated != null) {
        sendDownstream(lastReceivedOperated);
    }
}
```
