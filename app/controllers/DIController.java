package controllers;

import components.Inventory;
import components.Stock;
import play.mvc.*;
import javax.inject.Inject;

public class DIController extends  Controller {
    protected  Inventory inventory; // not singleton

    protected Stock stock; // singleton

    @Inject
    public DIController(Inventory inventory, Stock stock) {
        System.out.println("***DIController created");
        this.inventory = inventory;
        this.stock = stock;
    }

    public Result getStock(String id) {
        return ok(" Stock " + inventory.getStock(id));
    }

}
