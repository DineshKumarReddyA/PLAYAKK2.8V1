package controllers;

import play.mvc.*;
import java.util.concurrent.CompletableFuture;
import  java.util.concurrent.CompletionStage;
import java.util.concurrent.Executors; // thread pool/workers pool
import java.util.concurrent.Future;

public class AsyncController extends  Controller {

    public String getResultSync() {
        // BAD, just for example
        // not good for play/akka programming
        System.out.println("Getting Result start now");
        try {
            Thread.sleep(5000);
            System.out.println("Got result sync...");
        }catch (Exception ex) {}

        return "<h2>Job Done</h2>";
    }

    //BAD part
    public Result getBlockingResult() {
        System.out.println("calling blocking code ...");

        String result = getResultSync(); // hold on thread for 5 second
        System.out.println("calling blocking code done...responding to client");
        return ok(result).as("text/html");
    }

    // Future<String> means defered call, the result shall be availabel later, handler for the result
    private Future<String> getResultAsync() throws Exception {
        // Create a handle that can be used to obtain/share the result

        // Akka Dispatcher/Fork/thread.. Start
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        //Executors is a task manager, it accept tasks and execute using thread pool

        System.out.println("calling unblocking code start..");
        System.out.println("Akka thread id " + Thread.currentThread().getId());

        // Akka Dispatcher/Fork/thread.. end

        // JVM thread, not akka threads
        // Executors.newCachedThreadPool().submit( () -> taskFunc)
        Executors.newCachedThreadPool()
            .submit( () -> {
                // Executhor Thread pool Start
            System.out.println("Executor thread id " + Thread.currentThread().getId());
            System.out.println("task running inside executor..");
            // will not block akka thread, but block the executor thread
            try {
                Thread.sleep(5000);
            }catch(Exception ex) {}

            //completableFuture is visible here/ closure concept
            // the result can be received usign future.get or unblcoking future get
            completableFuture.complete("Hello World"); // result after computing/io
            // return the result with future
             // Executhor Thread pool end
        });

        // Akka Dispatcher/Fork/thread.. Start
        System.out.println("calling unblocking code end..");
        return completableFuture; // we return the future as we don't have result immediately, caller has use future handle for result
        // Akka Dispatcher/Fork/thread.. end
    }

    public Result executorHelloWorld() throws  Exception {
        System.out.println("-----------------------------------");
        System.out.println("calling async code ...");
        Future<String> future = getResultAsync(); // doesn't wait
        System.out.println("calling async code done");

        // now we need result "Hello World"
        // we get result in two mode, wither blocking or unblocking nature
        // BAD, blocking code
        // Using blocking future.get() again bad for play/akka architecture
        System.out.println("waiting for result start ..." + System.currentTimeMillis());

        String result = future.get(); // shall get "Hello World"
        System.out.println("waiting for result done ..."  + System.currentTimeMillis());

        return ok(result).as("text/html");
    }

    // CompletableFuture
    public CompletionStage<Result> completableFutureExample() {
        System.out.println("-----------------------------------");
        // supplyAsync takes your async function, lambda, and execute them in excutor thread
         System.out.println("Akka thread id " + Thread.currentThread().getId());

        System.out.println("Submitting task begin");

        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync( () -> {
            System.out.println("executor thread id " + Thread.currentThread().getId());

            try {
                Thread.sleep(5000);
            }catch(Exception ex) {}

            return "Hello World";
        });

        System.out.println("Submitting task submitted");

        return completableFuture.thenApplyAsync( result -> {
            System.out.println("Got the result.." + result);
            return ok(result);
        });
    }

    // getProductPrice - async

    // applyProductDiscount - async

    // apply tax
    private CompletableFuture<Double> getProductPrice() {
        CompletableFuture<Double> completableFuture = CompletableFuture.supplyAsync( () -> {
            System.out.println("getProductPrice executor thread id " + Thread.currentThread().getId());

            System.out.println("Got price 100");
            return 100.0;
        } );

        return completableFuture;
    }

    public CompletionStage<Result> combineFutures() {
        System.out.println("Akka thread id " + Thread.currentThread().getId());

        // f(g(x))

        // g(x) - get price
        CompletableFuture<Double> completablePriceFuture = getProductPrice();
        // apply discount f(g(x)) - f is discount
        CompletableFuture<Double> completableDiscountFuture = completablePriceFuture.thenCompose( price -> {
            return CompletableFuture.supplyAsync( () -> {
                System.out.println("apply discount executor thread id " + Thread.currentThread().getId());

                return price * .80;
            });
        });

        // apply tax  k(f(g(x)), k is tax function
        CompletableFuture<Double> completableDiscountTaxFuture = completableDiscountFuture.thenCompose( discountedPrice -> {
            return CompletableFuture.supplyAsync( () -> {
                System.out.println("apply tax executor thread id " + Thread.currentThread().getId());

                return discountedPrice * 1.05; // 5% GET added
            });
        });

        return completableDiscountTaxFuture.thenApplyAsync( result -> {
            System.out.println("Got the discount.." + result);
            return ok("Grand Total " + result);
        });
    }

//
//    // GOOD part, unblocking get result
//    // CompletionStage<Result>  result is future driven, async callback
//    public CompletionStage<Result> executorAsyncHelloWorld() throws  Exception {
//        System.out.println("-----------------------------------");
//        System.out.println("calling async code ...");
//        Future<String> future = getResultAsync(); // doesn't wait
//        System.out.println("calling async code done");
//
//    }
}
