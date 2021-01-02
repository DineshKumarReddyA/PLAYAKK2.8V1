package components;
import javax.inject.Singleton;
import javax.inject.Inject;

import play.inject.ApplicationLifecycle;
import java.util.concurrent.CompletableFuture;

// only one instance per application
// Single instance of stock is shared between as many controllers
@Singleton
public class Stock {

    // how to cleanup the objects such as release db connection, deallocate memory or update zookeeper/etcd...
    // on graceful termination of program
        // Ctrl +C [Signal/Interput]
        // Windows/Linux services systemd   my-program-service stop | restart
    // system shutdown - services are basically called stop
    @Inject
    public Stock(ApplicationLifecycle lifecycle) {
        System.out.println("***Stock created");

        lifecycle.addStopHook( () -> {
            System.out.println("***Stock is stopping, resource cleanup done");
            return CompletableFuture.completedFuture(null);
        });
    }

    public int getStock(String id) {
        return 200;
    }
}
