package controllers;

import javax.inject.Inject;
import javax.inject.Named;

import actors.HelloActor;
import actors.HelloActorProtocol;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import play.mvc.*;
import scala.compat.java8.FutureConverters;
import static akka.pattern.Patterns.ask;

import java.util.concurrent.CompletionStage;

public class ActorController extends  Controller {
    final ActorRef helloActor;



    private ActorRef configuredActor;

    private ActorRef parentActor;


    @Inject
    public ActorController(ActorSystem system,
                            @Named("configured-actor") ActorRef configuredActor
                           ) {
        helloActor = system.actorOf(HelloActor.getProps());
        this.configuredActor = configuredActor;
        this.parentActor = parentActor;
    }

    public CompletionStage<Result> sayHello(String name) {
        return FutureConverters.toJava(ask(helloActor, new HelloActorProtocol.SayHello(name), 1000))
                .thenApply(response -> ok((String) response));
    }

    public CompletionStage<Result> getConfig() {
        return FutureConverters.toJava(
                ask(configuredActor, new HelloActorProtocol.GetConfig(), 1000))
                .thenApply(response -> ok((String) response));
    }
}
