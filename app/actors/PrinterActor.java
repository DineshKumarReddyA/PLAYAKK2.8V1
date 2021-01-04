package actors;

import akka.actor.*;

public class PrinterActor extends  AbstractActorWithStash {
    // metadata for actor creation
    public static Props getProps() {
        return Props.create((PrinterActor.class));
    }


    public PrinterActor() {

    }

    public Receive createNormalReceive() {

        return receiveBuilder()
                .match(PrinterActorProtocol.PrintDocument.class, document -> {
                    System.out.println("PRining.....");
                })
                .match(PrinterActorProtocol.Resume.class, greeting -> {
                    System.out.println("**--resume(createSelfTestModeReceive()");
                    unstashAll();
                })
                .match(PrinterActorProtocol.OffCommand.class, command -> {
                    System.out.println("Printer off.....");
                })
                .match(PrinterActorProtocol.StandBy.class, command -> {
                    System.out.println("standby .....");
                    context().unbecome(); // go back to previous top in the stack // main one
                })
                .matchAny( obj -> System.out.println("createNormalReceive Unknown message " + obj.getClass()) )
                .build();
    }


    public Receive createSelfTestModeReceive() {
        return receiveBuilder()
                .match(PrinterActorProtocol.PrintSamplePage.class, document -> {
                    System.out.println("PrintSamplePage.....");
                })
                .match(PrinterActorProtocol.SelfTest.class, command -> {
                    System.out.println("Printer off.....");
                })
                .match(PrinterActorProtocol.StandBy.class, command -> {
                    System.out.println("standby .....");
                    context().unbecome(); // go back to previous top in the stack // main one
                })
                .matchAny( msg -> stash()) // move to buffer
               // .matchAny( obj -> System.out.println("createSelfTestModeReceive Unknown message" + obj.getClass()) )
                .build();
    }


    // pattern matching, check incoming message, map to some instance method/process
    // when a message landed in the mailbox, the dispatcher will pick the message
    // then call onReceive, which will again againt matches
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(PrinterActorProtocol.OnCommand.class, greeting -> {
                    System.out.println("**--become(createNormalReceive())");
                    getContext().become(createNormalReceive());
                })
                .match(PrinterActorProtocol.StartSelfTestCommand.class, greeting -> {
                    System.out.println("**--become(createSelfTestModeReceive()");
                    getContext().become(createSelfTestModeReceive());
                })


                .matchAny( obj -> System.out.println("PritnerActor Unknown message " + obj.getClass()) )
                .build();
    }
}