package repositories;


import models.Product;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * Provide JPA operations running inside of a thread pool sized to the connection pool
 */
public class JPAProductRepository implements ProductRepository {

    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public JPAProductRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }

    @Override
    public CompletionStage<Product> add(Product product) {
        return supplyAsync(() -> wrap(em -> insert(em, product)), executionContext);
    }

    @Override
    public CompletionStage<Stream<Product>> list() {
        return supplyAsync(() -> wrap(em -> list(em)), executionContext);
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    private Product insert(EntityManager em, Product product) {
        em.persist(product);
        return product;
    }

    private Stream<Product> list(EntityManager em) {
        List<Product> products = em.createQuery("select p from products p", Product.class).getResultList();
        return products.stream();
    }
}