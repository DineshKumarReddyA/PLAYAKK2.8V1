package controllers;

import play.mvc.*;

public class RoutingController extends  Controller  {
    // http://.../routing/hello/java
    public Result hello(String name) {
        return ok("Hello " + name);
    }

    // query?name=java
    public Result query(String name) {
        return ok("Query " + name);
    }

    public Result experience(String name, Integer year) {
        return ok("Expereince " + name + " " + year + " years");
    }

    public Result download(String imagePath) {
        return ok(":Here your file " + imagePath);
    }

    public Result image(String profileId) {
        return ok("Profile id " + profileId);
    }

    public Result imageAlpha(String profileId) {
        return ok("imageAlpha Profile id " + profileId);
    }

    public Result pages(Integer start, Integer end) {
        return ok("pages start = " + start + " end value " + end);
    }


    // optional parameters
    public Result filter(Integer limit) {
        return ok("filter " + limit);
    }

    public Result rawRequest(Http.Request request) {
        return ok("Request " + request.path());
    }


    // POST
    public Result postData(Http.Request request) {
        return ok("Payload " +  request.body().asText());
    }
}
