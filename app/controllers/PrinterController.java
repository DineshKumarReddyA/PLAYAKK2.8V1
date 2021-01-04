package controllers;

import actors.PrinterActor;
import actors.PrinterActorProtocol;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import play.mvc.*;

import javax.inject.Inject;

public class PrinterController extends  Controller {
    ActorRef printerActor;
    @Inject
    public  PrinterController(ActorSystem actorSystem) {
        printerActor = actorSystem.actorOf(PrinterActor.getProps(), "printers");
    }

    public Result onCommand() {
        printerActor.tell(new PrinterActorProtocol.OnCommand(), ActorRef.noSender());
        return ok("on");
    }

    public Result offCommand() {
        printerActor.tell(new PrinterActorProtocol.OffCommand(), ActorRef.noSender());
        return ok("off");
    }

    public Result startSelfTestCommand() {
        printerActor.tell(new PrinterActorProtocol.StartSelfTestCommand(), ActorRef.noSender());
        return ok("startSelf");
    }

    public Result printCommand() {
        printerActor.tell(new PrinterActorProtocol.PrintDocument(), ActorRef.noSender());
        return ok("printing...");
    }

    public Result resumeCommand() {
        printerActor.tell(new PrinterActorProtocol.Resume(), ActorRef.noSender());
        return ok("resuming...");
    }

    public Result printSampleTestCommand() {
        printerActor.tell(new PrinterActorProtocol.PrintSamplePage(), ActorRef.noSender());
        return ok("printSampleTes");
    }


    public Result standbyCommand() {
        printerActor.tell(new PrinterActorProtocol.StandBy(), ActorRef.noSender());
        return ok("standbyCommand");
    }
}
