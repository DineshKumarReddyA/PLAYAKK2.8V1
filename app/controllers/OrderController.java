package controllers;

import actors.HelloActor;
import actors.HelloActorProtocol;
import actors.HelloProtocol;
import actors.OrderActorProtocol;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import models.Order;
import models.Product;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.*;
import scala.compat.java8.FutureConverters;
import akka.actor.*;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.concurrent.CompletionStage;
import static akka.pattern.Patterns.ask;
import static play.libs.Json.toJson;
import play.data.FormFactory;

public class OrderController extends  Controller {

    ActorRef orderActor;
    ActorSystem system;

    private final FormFactory formFactory;

    @Inject
    public OrderController(ActorSystem system,
                           @Named("order-actor") ActorRef orderActor,
                           FormFactory formFactory
                           ) {
         this.formFactory = formFactory;
         this.orderActor = orderActor;
         this.system = system;
    }

    public CompletionStage<Result> getOrder(Long orderId) {
        OrderActorProtocol.GetOrder order = new OrderActorProtocol.GetOrder(orderId);
        CompletionStage<Object> responseFuture =  FutureConverters.toJava(ask(orderActor,  order, 1000));

        return responseFuture
                .thenApplyAsync(res -> {
                    OrderActorProtocol.GetOrderResponse response = (OrderActorProtocol.GetOrderResponse) res;

                    if (response.order == null) {
                        return notFound("Resource not found");
                    }

                    return ok(Json.toJson(response.order));
                });

    }

    public CompletionStage<Result> createOrder(final Http.Request request) {
        Order order = formFactory.form(Order.class).bindFromRequest(request).get();

        OrderActorProtocol.CreateOrder createOrder = new OrderActorProtocol.CreateOrder(order);
        CompletionStage<Object> responseFuture =  FutureConverters.toJava(ask(orderActor,  createOrder, 1000));

        return responseFuture
                .thenApplyAsync(res -> {
                    OrderActorProtocol.CreateOrderResponse response = (OrderActorProtocol.CreateOrderResponse) res;

                    if (response.order == null) {
                        return notFound("Resource not found");
                    }

                    return ok(Json.toJson(response.order));
                });

    }
}
