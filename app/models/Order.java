package models;

import javax.persistence.*;

@Entity(name = Order.TABLE_NAME)
@Table(name = Order.TABLE_NAME)
public class Order {
    public static final String TABLE_NAME= "orders";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    public  Integer amount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}