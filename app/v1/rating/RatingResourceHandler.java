package v1.rating;


import com.palominolabs.http.url.UrlBuilder;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http;

import javax.inject.Inject;
import java.nio.charset.CharacterCodingException;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

/**
 * Handles presentation of Rating resources, which map to JSON.
 */
public class RatingResourceHandler {

    private final RatingRepository repository;
    private final HttpExecutionContext ec;

    @Inject
    public RatingResourceHandler(RatingRepository repository, HttpExecutionContext ec) {
        this.repository = repository;
        this.ec = ec;
    }

    public CompletionStage<Stream<RatingResource>> find(Http.Request request) {
        return repository.list().thenApplyAsync(postDataStream -> {
            return postDataStream.map(data -> new RatingResource(data, link(request, data)));
        }, ec.current());
    }

    public CompletionStage<RatingResource> create(Http.Request request, RatingResource resource) {
        final RatingData data = new RatingData(resource.getScore(), resource.getProductId());
        return repository.create(data).thenApplyAsync(savedData -> {
            return new RatingResource(savedData, link(request, savedData));
        }, ec.current());
    }

    public CompletionStage<Optional<RatingResource>> lookup(Http.Request request, String id) {
        return repository.get(Long.parseLong(id)).thenApplyAsync(optionalData -> {
            return optionalData.map(data -> new RatingResource(data, link(request, data)));
        }, ec.current());
    }

    public CompletionStage<Optional<RatingResource>> update(Http.Request request, String id, RatingResource resource) {
        final RatingData data = new RatingData(resource.getScore(),   resource.getProductId());
        return repository.update(Long.parseLong(id), data).thenApplyAsync(optionalData -> {
            return optionalData.map(op -> new RatingResource(op, link(request, op)));
        }, ec.current());
    }

    private String link(Http.Request request, RatingData data) {
        final String[] hostPort = request.host().split(":");
        String host = hostPort[0];
        int port = (hostPort.length == 2) ? Integer.parseInt(hostPort[1]) : -1;
        final String scheme = request.secure() ? "https" : "http";
        try {
            return UrlBuilder.forHost(scheme, host, port)
                    .pathSegments("v1", "ratings", data.id.toString())
                    .toUrlString();
        } catch (CharacterCodingException e) {
            throw new IllegalStateException(e);
        }
    }
}