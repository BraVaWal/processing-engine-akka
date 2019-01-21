package it.polimi.middleware.processingengine;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import it.polimi.middleware.processingengine.message.AddOperatorMessage;
import spark.Request;
import spark.Response;

import static spark.Spark.*;

public class RestServerActor extends AbstractActor {

    private final ActorRef supervisorActor;

    public RestServerActor(ActorRef supervisorActor) {
        this.supervisorActor = supervisorActor;
        initEndPoints();
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
                request.queryParams("sourceId"),
                request.queryParams("sinkId"),
                OperatorType.valueOf(request.queryParams("operatorType"))
        ), supervisorActor);
        return "Operator added";
    }
}
