package v1.rating;

import javax.persistence.*;

/**
 * Data returned from the database
 */
@Entity
@Table(name = "ratings")
public class RatingData {

    public RatingData() {
    }

    public RatingData(Integer score, String productId) {
        this.score = score;
        this.productId = productId;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Long id;
    public Integer score;
    public String productId;
}