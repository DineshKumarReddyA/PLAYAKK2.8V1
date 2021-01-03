package models;

public class Discount {
    private String productId;
    private String couponCode;
    private Integer discount = 0;

    public Discount() {
    }

    public Discount(String productId, String couponCode, Integer discount) {
        this.productId = productId;
        this.couponCode = couponCode;
        this.discount = discount;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }
}
