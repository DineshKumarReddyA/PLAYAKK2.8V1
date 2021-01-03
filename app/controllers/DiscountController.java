package controllers;
import com.fasterxml.jackson.databind.JsonNode;
import dao.DiscountDao;
import models.Discount;
import models.Email;
import play.libs.Json;
import play.mvc.*;

import javax.inject.Inject;

public class DiscountController extends  Controller {
    DiscountDao discountDao;

    @Inject
    public DiscountController(DiscountDao discountDao) {
        this.discountDao = discountDao;
    }

    /*

    http://localhost:9000/discount/create-discount

    {
    "productId": "12234",
    "couponCode": "Apply20",
    "discount": 20
}

     */
    @BodyParser.Of(BodyParser.Json.class)
    public Result createDiscount(Http.Request request) {
        JsonNode emailNode = request.body().asJson(); // application/json type
        Discount discount = Json.fromJson(emailNode, Discount.class); // JSON to POJO
        System.out.println("Discoutn is " + discount);
        discountDao.setDiscountSync(discount);
        return ok("Done");
    }


    // http://localhost:9000/discount/create-discount/12234

    public Result getDiscount(String productId) {
        Discount discount =  discountDao.getDiscountSync(productId);
        return ok(Json.toJson(discount));
    }



}
