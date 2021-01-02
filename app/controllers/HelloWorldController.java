package controllers;

import play.mvc.*;

public class HelloWorldController extends  Controller {
    public Result index() {
        // 200 OK
        return ok("<h2>Welcome to Play").as("text/html");
    }

    public Result notFoundPage() {
        // return notFound(); // 404 Not Found
        return notFound("<h2>The you requested is not found</h2>").as("text/html");
    }

    // 401 - client error, client send data not complete
    public Result badRequestError() {
        return badRequest("invalid data");
    }

    // 500 internal server error
    public Result internalError() {
        return internalServerError("Oops Crash..");
    }

    public Result customStatusText() {
        return status(403, "Forbidden no JWT token");
    }

    public Result about() {
        return ok("<h2>About page</h2>").as("text/html");
    }

    // permanent
    public Result aboutUs() {
        return redirect("/hello-world/about");
    }

    public Result paymentPaypal() {
        return ok("Paypal page");
    }

    public Result payment() {
        // temp direct to paypal gateway
        return temporaryRedirect("/hello-world/payment-paypal");
    }



}
