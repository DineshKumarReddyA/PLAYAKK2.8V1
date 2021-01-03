package v1.rating;

import net.jodah.failsafe.CircuitBreaker;
import net.jodah.failsafe.Failsafe;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * A repository that provides a non-blocking API with a custom execution context
 * and circuit breaker.
 */
@Singleton
public class JPARatingRepository implements RatingRepository {

    private final JPAApi jpaApi;
    private final RatingExecutionContext ec;
    private final CircuitBreaker<Optional<RatingData>> circuitBreaker = new CircuitBreaker<Optional<RatingData>>().withFailureThreshold(1).withSuccessThreshold(3);

    @Inject
    public JPARatingRepository(JPAApi api, RatingExecutionContext ec) {
        this.jpaApi = api;
        this.ec = ec;
    }

    @Override
    public CompletionStage<Stream<RatingData>> list() {
        return supplyAsync(() -> wrap(em -> select(em)), ec);
    }

    @Override
    public CompletionStage<RatingData> create(RatingData postData) {
        return supplyAsync(() -> wrap(em -> insert(em, postData)), ec);
    }

    @Override
    public CompletionStage<Optional<RatingData>> get(Long id) {
        return supplyAsync(() -> wrap(em -> Failsafe.with(circuitBreaker).get(() -> lookup(em, id))), ec);
    }

    @Override
    public CompletionStage<Optional<RatingData>> update(Long id, RatingData postData) {
        return supplyAsync(() -> wrap(em -> Failsafe.with(circuitBreaker).get(() -> modify(em, id, postData))), ec);
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    private Optional<RatingData> lookup(EntityManager em, Long id) throws SQLException {
       // throw new SQLException("Call this to cause the circuit breaker to trip");
        return Optional.ofNullable(em.find(RatingData.class, id));
    }

    private Stream<RatingData> select(EntityManager em) {
        TypedQuery<RatingData> query = em.createQuery("SELECT p FROM RatingData p", RatingData.class);
        return query.getResultList().stream();
    }

    private Optional<RatingData> modify(EntityManager em, Long id, RatingData postData) throws InterruptedException {
        final RatingData data = em.find(RatingData.class, id);
        if (data != null) {
            data.score = postData.score;
            data.productId = postData.productId;
        }
        Thread.sleep(10000L);
        return Optional.ofNullable(data);
    }

    private RatingData insert(EntityManager em, RatingData postData) {
        return em.merge(postData);
    }
}