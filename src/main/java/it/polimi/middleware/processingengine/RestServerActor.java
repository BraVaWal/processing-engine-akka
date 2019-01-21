package it.polimi.middleware.processingengine;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.middleware.processingengine.message.AddJobMessage;
import spark.Request;
import spark.Response;

import java.lang.reflect.Type;
import java.util.ArrayList;

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

    private Object getStatus(Request request, Response response) {
        return "Status";
    }

    private Object getResult(Request request, Response response) {
        return "Result";
    }

    private Object postJob(Request request, Response response) {
        Type listType = new TypeToken<ArrayList<KeyValuePair>>() {
        }.getType();
        ArrayList<KeyValuePair> data = new Gson().fromJson(request.body(), listType);
        supervisorActor.tell(new AddJobMessage(data), self());
        return data;
    }

}
