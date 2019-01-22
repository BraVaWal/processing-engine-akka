package it.polimi.middleware.processingengine;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.pattern.Patterns;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.middleware.processingengine.message.*;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import spark.Request;
import spark.Response;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;

public class RestServerActor extends AbstractActor {

    private final ActorRef supervisorActor;

    public RestServerActor(ActorRef supervisorActor) {
        this.supervisorActor = supervisorActor;
        initEndPoints();
    }

    public static Props props(ActorRef supervisorActor) {
        return Props.create(RestServerActor.class, supervisorActor);
    }

    private void initEndPoints() {
        get("/status", this::getStatus);
        get("/result", this::getResult);
        post("/job", this::postJob);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().build();
    }

    private Object getStatus(Request request, Response response) throws Exception {
        final Future<Object> reply = Patterns.ask(supervisorActor, new AskStatusMessage(), 1000);

        StatusMessage message = (StatusMessage) Await.result(reply, Duration.Inf());
        List<WorkerStatusMessage> workerStatusMessages = new ArrayList<>(message.getWorkers().size());

        for (ActorRef worker : message.getWorkers()) {
            workerStatusMessages.add(askWorkerStatus(worker));
        }

        return new Gson().toJson(workerStatusMessages);
    }

    private WorkerStatusMessage askWorkerStatus(ActorRef worker) throws Exception {
        final Future<Object> workerReply = Patterns.ask(worker, new AskStatusMessage(), 1000);
        return (WorkerStatusMessage) Await.result(workerReply, Duration.Inf());
    }

    private Object getResult(Request request, Response response) throws Exception {
        final Future<Object> reply = Patterns.ask(supervisorActor, new AskResultMessage(), 1000);
        ResultMessage message = (ResultMessage) Await.result(reply, Duration.Inf());
        return new Gson().toJson(message.getResults());

    }

    private Object postJob(Request request, Response response) {
        Type listType = new TypeToken<ArrayList<KeyValuePair>>() {
        }.getType();
        ArrayList<KeyValuePair> data = new Gson().fromJson(request.body(), listType);
        supervisorActor.tell(new AddJobMessage(data), self());
        return data;
    }

}
