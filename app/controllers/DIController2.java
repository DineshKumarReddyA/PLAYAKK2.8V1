package controllers;

import components.Inventory;
import components.Stock;
import play.mvc.*;
import javax.inject.Inject;

public class DIController2 extends  Controller {
    // not a singleton, for every controller instance, new inventory object created, not shared with other controller
    protected  Inventory inventory;
    protected Stock stock; // singleton

    @Inject
    public DIController2(Inventory inventory, Stock stock) {
        System.out.println("***DIController2 created");
        this.inventory = inventory;
        this.stock = stock;
    }

    public Result getStock(String id) {
        return ok(" Stock " + inventory.getStock(id));
    }
}
