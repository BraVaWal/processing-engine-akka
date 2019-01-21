package it.polimi.middleware.processingengine;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.google.gson.Gson;
import it.polimi.middleware.processingengine.message.AddOperatorMessage;
import spark.Request;
import spark.Response;

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
        //Just read that this is not required
        //post("/operator", this::postOperator);
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

//    private Object postOperator(Request request, Response response) {
//        AddOperatorMessage addOperatorMessage = new Gson().fromJson(request.body(), AddOperatorMessage.class);
//        supervisorActor.tell(addOperatorMessage, supervisorActor);
//        return "Operator added";
//    }
    
    private Object postJob(Request request, Response response) {
        return "Input";
    }

}
