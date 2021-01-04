package actors;

import akka.actor.Actor;

public class HelloActorProtocol {
    public static class SayHello {
        public final String name;

        public SayHello(String name) {
            this.name = name;
        }
    }

    public static class GetConfig {}

    public interface Factory {
        public Actor create(String key);
    }

    public static class GetChild {
        public String key;
    }


}
