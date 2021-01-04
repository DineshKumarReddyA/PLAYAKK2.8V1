package repositories;


import models.Order;
import models.Product;
import net.jodah.failsafe.CircuitBreaker;
import net.jodah.failsafe.Failsafe;
import play.db.jpa.JPAApi;
import v1.post.PostData;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * Provide JPA operations running inside of a thread pool sized to the connection pool
 */
public class JPAOrderRepository implements OrderRepository {

    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;
    private final CircuitBreaker<Optional<Order>> circuitBreaker = new CircuitBreaker<Optional<Order>>().withFailureThreshold(1).withSuccessThreshold(3);

    @Inject
    public JPAOrderRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }

    @Override
    public CompletionStage<Order> add(Order order) {
        return supplyAsync(() -> wrap(em -> insert(em, order)), executionContext);
    }

    @Override
    public CompletionStage<Stream<Order>> list() {
        return supplyAsync(() -> wrap(em -> list(em)), executionContext);
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    private Order insert(EntityManager em, Order order) {
        em.persist(order);
        return order;
    }

    private Stream<Order> list(EntityManager em) {
        List<Order> orders = em.createQuery("select p from orders p", Order.class).getResultList();
        return orders.stream();
    }

    public CompletionStage<Optional<Order>> get(Long id) {
        return supplyAsync(() -> wrap(em -> Failsafe.with(circuitBreaker).get(() -> lookup(em, id))), executionContext);
    }


    private Optional<Order> lookup(EntityManager em, Long id) throws SQLException {
        // throw new SQLException("Call this to cause the circuit breaker to trip");
        return Optional.ofNullable(em.find(Order.class, id));
    }
}