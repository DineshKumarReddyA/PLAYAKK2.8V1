package v1.rating;

import akka.actor.ActorSystem;
import play.libs.concurrent.CustomExecutionContext;

import javax.inject.Inject;

/**
 * Custom execution context wired to "rating.repository" thread pool
 */
public class RatingExecutionContext extends CustomExecutionContext {

    @Inject
    public RatingExecutionContext(ActorSystem actorSystem) {
        super(actorSystem, "rating.repository");
    }
}