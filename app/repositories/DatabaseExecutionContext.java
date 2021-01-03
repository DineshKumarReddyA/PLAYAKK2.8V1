package repositories;


import akka.actor.ActorSystem;
import play.libs.concurrent.CustomExecutionContext;

import javax.inject.Inject;

/**
 * Custom execution context wired to "database.dispatcher" thread pool
 */
public class DatabaseExecutionContext extends CustomExecutionContext {
    @Inject
    public DatabaseExecutionContext(ActorSystem actorSystem) {
        // database.dispatcher from conf file
        super(actorSystem, "database.dispatcher"); // database.dispatcher  is threading model/queue size config .....

    }
}