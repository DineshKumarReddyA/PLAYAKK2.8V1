package components;

import play.inject.ApplicationLifecycle;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

public class Inventory {

    @Inject
    public Inventory(ApplicationLifecycle lifecycle) {
        System.out.println("***Inventory created");

        lifecycle.addStopHook( () -> {
            System.out.println("***Inventory is stopping, resource cleanup done");
            return CompletableFuture.completedFuture(null);
        });
    }

    public int getStock(String id) {
        return 100;
    }
}
