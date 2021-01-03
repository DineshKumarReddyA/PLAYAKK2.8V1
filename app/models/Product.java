package models;

import javax.persistence.*;

@Entity(name = Product.TABLE_NAME)
@Table(name = Product.TABLE_NAME)
public class Product {
    public static final String TABLE_NAME= "products";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    public String name;

    public  Integer price;

    private String brandId;

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }
}