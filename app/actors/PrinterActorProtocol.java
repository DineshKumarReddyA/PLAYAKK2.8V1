package actors;

import akka.actor.AbstractActor;
import akka.actor.Props;

public class PrinterActorProtocol {
    public static class Resume {}

    public static class PrintDocument {}
    public static class OnCommand {}
    public static class OffCommand {}
    public static class StartSelfTestCommand {}
    public static class SelfTest {}
    public static class PrintSamplePage {}

    public static class StandBy {}

}