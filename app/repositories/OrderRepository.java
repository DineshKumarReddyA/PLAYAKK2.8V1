package repositories;

import com.google.inject.ImplementedBy;
import models.Order;
import models.Product;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

/**
 * This interface provides a non-blocking API for possibly blocking operations.
 */
@ImplementedBy(JPAOrderRepository.class)
public interface OrderRepository {
    CompletionStage<Order> add(Order order);
    CompletionStage<Stream<Order>> list();
    CompletionStage<Optional<Order>> get(Long id);
    // assignment update existing and delete existing one
}