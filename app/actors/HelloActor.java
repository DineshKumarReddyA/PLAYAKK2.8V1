package actors;

import akka.actor.*;

public class HelloActor extends  AbstractActor {
    // metadata for actor creation
    public static Props getProps() {
        return Props.create((HelloActor.class));
    }

    // pattern matching, check incoming message, map to some instance method/process
    // when a message landed in the mailbox, the dispatcher will pick the message
    // then call onReceive, which will again againt matches
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(HelloProtocol.Greeting.class, greeting -> {
                    String reply = "Hello " + greeting.message;
                    HelloProtocol.GreetingResponse response = new HelloProtocol.GreetingResponse();
                    response.message = reply;
                    sender().tell(response, self());
                })
                .matchAny( obj -> System.out.println("Unknown message") )
                .build();
    }
}