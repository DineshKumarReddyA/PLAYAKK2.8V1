package dao;

import models.Discount;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public interface DiscountDao {
    //FIXME
    void setDiscount(Discount discount);
    CompletionStage<Discount> getDiscount(String productId);


      void setDiscountSync(Discount discount) ;


     Discount getDiscountSync(String productId);

}
