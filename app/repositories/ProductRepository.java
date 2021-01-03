package repositories;

import com.google.inject.ImplementedBy;
import models.Product;

import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

/**
 * This interface provides a non-blocking API for possibly blocking operations.
 */
@ImplementedBy(JPAProductRepository.class)
public interface ProductRepository {
    CompletionStage<Product> add(Product product);
    CompletionStage<Stream<Product>> list();
    // assignment update existing and delete existing one
}