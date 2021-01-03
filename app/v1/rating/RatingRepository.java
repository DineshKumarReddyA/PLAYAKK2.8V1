package v1.rating;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

public interface RatingRepository {

    CompletionStage<Stream<RatingData>> list();

    CompletionStage<RatingData> create(RatingData postData);

    CompletionStage<Optional<RatingData>> get(Long id);

    CompletionStage<Optional<RatingData>> update(Long id, RatingData postData);
}
