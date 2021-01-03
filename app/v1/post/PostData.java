package v1.post;

import javax.persistence.*;

/**
 * Data returned from the database
 */
@Entity
@Table(name = "posts")
public class PostData {

    public PostData() {
    }

    public PostData(String title, String body, String productId) {
        this.title = title;
        this.body = body;
        this.productId = productId;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Long id;
    public String title;
    public String body;
    public String productId;
}