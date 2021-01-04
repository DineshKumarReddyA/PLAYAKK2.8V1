package modules;


import actors.*;
import com.google.inject.AbstractModule;
        import play.libs.akka.AkkaGuiceSupport;

public class ActorModule extends AbstractModule implements AkkaGuiceSupport {
    @Override
    protected void configure() {
        bindActor(OrderActor.class, "order-actor");
        bindActor(ConfiguredActor.class, "configured-actor");
        // bindActor(ParentActor.class, "parent-actor");
        //  bindActorFactory(ConfiguredChildActor.class, HelloActorProtocol.Factory.class);
    }
}