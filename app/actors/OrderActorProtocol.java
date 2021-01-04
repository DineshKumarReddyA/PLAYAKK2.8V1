package actors;

import models.Order;

public class OrderActorProtocol {
    public static class GetOrder {
        public Long orderId;
        public GetOrder(Long orderId) {
            this.orderId = orderId;
        }
    }

    public static class GetOrderResponse {
        public Order order;
        public GetOrderResponse(Order  order) {
            this.order = order;
        }
    }

    public static class CreateOrder {
        public Order order;
        public CreateOrder(Order order) {
            this.order = order;
        }
    }


    public static class CreateOrderResponse {
        public Order order;
        public CreateOrderResponse(Order order) {
            this.order = order;
        }
    }

}
