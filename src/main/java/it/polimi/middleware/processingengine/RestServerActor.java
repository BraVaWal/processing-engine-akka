package it.polimi.middleware.processingengine;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import it.polimi.middleware.processingengine.message.AddOperatorMessage;
import spark.Request;
import spark.Response;

import java.util.Arrays;

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
        post("/operator", this::postOperator);
    }

    @Override
    public Receive createReceive() {
        return null;
    }

    private Object getStatus(Request request, Response response) {
        return null;
    }

    private Object getResult(Request request, Response response) {
        return null;
    }

    private Object postOperator(Request request, Response response) {
        supervisorActor.tell(new AddOperatorMessage(
                request.queryParams("id"),
                Arrays.asList(request.queryParamsValues("sources")),
                Arrays.asList(request.queryParamsValues("sinkId")),
                OperatorType.valueOf(request.queryParams("operatorType"))
        ), supervisorActor);
        return "Operator added";
    }
}
