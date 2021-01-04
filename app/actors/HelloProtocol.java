package actors;

public class HelloProtocol {
    // instance
    public static class Greeting {
        public String message;
    }

    public static class GreetingResponse {
        public String message;
    }

    // used as class while matching
    public static class NameRequest {
    }

}
