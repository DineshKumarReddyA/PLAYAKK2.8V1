# Play Hello World Web Tutorial for Java

To follow the steps in this tutorial, you will need the correct version of Java and a build tool. You can build Play projects with any Java build tool. Since sbt takes advantage of Play features such as auto-reload, the tutorial describes how to build the project with sbt. 

Prerequisites include:

* Java Software Developer's Kit (SE) 1.8 or higher
* sbt 0.13.15 or higher (we recommend 1.2.3) Note: if you downloaded this project as a zip file from https://developer.lightbend.com, the file includes an sbt distribution for your convenience.

To check your Java version, enter the following in a command window:

`java -version`

To check your sbt version, enter the following in a command window:

`sbt sbtVersion`

If you do not have the required versions, follow these links to obtain them:

* [Java SE](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [sbt](http://www.scala-sbt.org/download.html)

## Build and run the project

This example Play project was created from a seed template. It includes all Play components and an Akka HTTP server. The project is also configured with filters for Cross-Site Request Forgery (CSRF) protection and security headers.

To build and run the project:

1. Use a command window to change into the example project directory, for example: `cd play-java-hello-world-web`

2. Build the project. Enter: `sbt run`. The project builds and starts the embedded HTTP server. Since this downloads libraries and dependencies, the amount of time required depends partly on your connection's speed.

3. After the message `Server started, ...` displays, enter the following URL in a browser: <http://localhost:9000>

The Play application responds: `Welcome to the Hello World Tutorial!`


# AB

sudo apt install apache2-utils

ab -V

Blocking akka thread, Thread.sleep(5000)

ab -n 100 -c 10 http://localhost:9000/async/get-blocking-result
Time taken for tests:   65.183 seconds
Requests per second:    1.53 [#/sec] (mean)
Time per request:       6518.340 [ms] (mean)
Time per request:       651.834 [ms] (mean, across all concurrent requests)

Connection Times (ms)
min  mean[+/-sd] median   max
Connect:        0    0   0.2      0       1
Processing:  5006 5963 1943.9   5024   10027
Waiting:     5006 5962 1943.9   5024   10026
Total:       5006 5963 1943.8   5025   10027

--

Blocking akka get result , BAD Thread.sleep(5000)
/async/executor-hello-world

ab -n 100 -c 10 http://localhost:9000/async/executor-hello-world

Time taken for tests:   56.938 seconds

Requests per second:    1.76 [#/sec] (mean)

Connection Times (ms)
min  mean[+/-sd] median   max
Connect:        0    0   0.2      0       1
Processing:  5003 5037 170.6   5013    6711
Waiting:     5003 5037 170.0   5012    6704
Total:       5003 5038 170.6   5013    6711

---
non blocking akka code, but executor sleep 5000, Good part in akka side

ab -n 100 -c 10 http://localhost:9000/async/async-hello-world

Time taken for tests:   170.091 seconds
Requests per second:    0.59 [#/sec] (mean)
Time per request:       17009.088 [ms] (mean)
Time per request:       1700.909 [ms] (mean, across all concurrent requests)

              min  mean[+/-sd] median   max
Connect:        0    0   0.1      0       1
Processing:  5009 16405 6155.5  15007   30013
Waiting:     5009 16404 6155.5  15007   30011
Total:       5009 16405 6155.5  15007   30013

---

no blocking in akka, futures/executors

ab -n 100 -c 10 http://localhost:9000/async/combine-futures

Time taken for tests:   0.233 seconds
Requests per second:    429.11 [#/sec] (mean)
Time per request:       23.304 [ms] (mean)
Time per request:       2.330 [ms] (mean, across all concurrent requests)

Connection Times (ms)
min  mean[+/-sd] median   max
Connect:        0    0   0.2      0       1
Processing:     5   20  10.0     18      56
Waiting:        5   19   9.9     17      55
Total:          5   20  10.1     18      56


ab -n 1000000 -c 100 http://localhost:9000/async/combine-futures

with combine feature, a lot of print statement

Time taken for tests:   497.775 seconds
Requests per second:    2008.94 [#/sec] (mean)
Time per request:       49.777 [ms] (mean)
Time per request:       0.498 [ms] (mean, across all concurrent requests)
Transfer rate:          678.80 [Kbytes/sec] received

Connection Times (ms)
min  mean[+/-sd] median   max
Connect:        0    0   1.5      0      50
Processing:     2   49  20.9     45     465
Waiting:        1   49  20.7     45     459
Total:          7   50  20.9     46     467

Percentage of the requests served within a certain time (ms)
50%     46
66%     54
75%     59
80%     62
90%     72
95%     83
98%    102
99%    123
100%    467 (longest request)

----

ab -n 100000 -c 100 http://localhost:9000/async/combine-futures-flow

ab -n 1000000 -c 100 http://localhost:9000/async/combine-futures-flow
Concurrency Level:      100
Time taken for tests:   280.730 seconds
Complete requests:      1000000
Failed requests:        0
Total transferred:      346000000 bytes
HTML transferred:       16000000 bytes
Requests per second:    3562.14 [#/sec] (mean)
Time per request:       28.073 [ms] (mean)
Time per request:       0.281 [ms] (mean, across all concurrent requests)
Transfer rate:          1203.62 [Kbytes/sec] received

Connection Times (ms)
min  mean[+/-sd] median   max
Connect:        0    0   0.6      0      39
Processing:     2   28   9.3     26     166
Waiting:        1   28   9.2     25     166
Total:          5   28   9.4     26     166



public CompletionStage<Result> combineFutures() {
System.out.println("Akka thread id " + Thread.currentThread().getId());

        // f(g(x))

        // g(x) - get price
        getProductPrice()
        .thenCompose( price -> {
            return CompletableFuture.supplyAsync( () -> {
                System.out.println("apply discount executor thread id " + Thread.currentThread().getId());

                return price * .80;
            });
        })
        .thenCompose( discountedPrice -> {
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


--
Compose

    applya function , return a result
     take result as input, apply to next function
            return output

                    tkae the output and apply to next function
                        return final output

    take final output, send to client as result

---

Parallel
    no dependencies between jobs, no waiting for other job completion
     do job 1
     do job 2
     do job 3 