package controllers;

import actors.HelloActor;
import actors.HelloActorProtocol;
import actors.HelloProtocol;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import play.mvc.*;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import static akka.pattern.Patterns.ask;
import scala.compat.java8.FutureConverters;

public class HelloActorController extends  Controller {
    ActorSystem system;
    ActorRef helloActor;
    @Inject
    public HelloActorController(ActorSystem system) {
        this.system = system;

        // create actor in actor system
        // akka://play/user/helloActor
        this.helloActor = system.actorOf(HelloActor.getProps(), "helloActor");
    }

    public CompletionStage<Result> sayHello(String message) {
        // convert Scala future and stuffs to Java compatible

        HelloProtocol.Greeting greeting = new HelloProtocol.Greeting();
        greeting.message = message;

      CompletionStage<Object> responseFuture =  FutureConverters.toJava(ask(helloActor,  greeting, 1000));

        return responseFuture
                .thenApplyAsync(response -> ok( ((HelloProtocol.GreetingResponse) response).message ));
    }

    public CompletionStage<Result> timeoutMessage(String message) {
        // convert Scala future and stuffs to Java compatible

        // asking, but no response, timeout
        CompletionStage<Object> responseFuture =  FutureConverters.toJava(ask(helloActor,  message, 1000));

        // if .handle is not there, the error shall be thrown as exception, handled by Global error handler
        // Global error handler can be overridden
        return responseFuture
                .thenApplyAsync(response -> ok( response.toString()  ))
                 .handle((s, t) -> status(500, "Server error, sorry"))
                ;
    }
}
