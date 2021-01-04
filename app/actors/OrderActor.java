package actors;

import akka.actor.AbstractActor;
import akka.actor.Props;

import akka.actor.*;
import models.Order;
import repositories.OrderRepository;

import javax.inject.Inject;

import static play.libs.Json.toJson;

public class OrderActor extends AbstractActor {

    OrderRepository orderRepository;

    @Inject
    public  OrderActor(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // metadata for actor creation
    public static Props getProps() {
        return Props.create((OrderActor.class));
    }

    // pattern matching, check incoming message, map to some instance method/process
    // when a message landed in the mailbox, the dispatcher will pick the message
    // then call onReceive, which will again againt matches
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                // for OrderActorProtocol.GetAllOrders, controller
                .match(OrderActorProtocol.GetOrder.class, getOrder -> {
                    Long orderId = getOrder.orderId;
                    ActorRef senderRef =  sender();
                    orderRepository.get(orderId)
                                    // then Accept means, the last statement, no more composition/nesting
                                    .thenAccept(o -> {

                                        Order order = null;

                                        if (o.isPresent()) {
                                            order = o.get();
                                        }

                                        OrderActorProtocol.GetOrderResponse response = new  OrderActorProtocol.GetOrderResponse(order);
                                        senderRef.tell(response, self());
                                    });
                })
                .match(OrderActorProtocol.CreateOrder.class, createOrder -> {
                    Order order = createOrder.order;
                    // FIXME: DAO

                    // return current sender for current request
                    ActorRef senderRef =  sender(); // Visiblity, closure
                    orderRepository.add(order)
                                // then Accept means, the last statement, no more composition/nesting
                                .thenAccept(o -> {
                                    OrderActorProtocol.CreateOrderResponse response = new OrderActorProtocol.CreateOrderResponse(o);
                                    // when we receive the response from db,
                                    // chances that other request is in parallel execution
                                    // sender() shall give different requested sender
                                    // sender().tell(response, self());
                                    senderRef.tell(response, self());
                                } );
                })
                .matchAny( obj -> System.out.println("Unknown message") )
                .build();
    }
}