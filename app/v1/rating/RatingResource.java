package v1.rating;


/**
 * Resource for the API.  This is a presentation class for frontend work.
 */
public class RatingResource {
    private String id;
    private String link;
    private Integer score;
    private String productId;

    public RatingResource() {
    }

    public RatingResource(String id, String link, Integer score,  String productId) {
        this.id = id;
        this.link = link;
        this.score = score;
        this.productId = productId;
    }

    public RatingResource(RatingData data, String link) {
        this.id = data.id.toString();
        this.link = link;
        this.score = data.score;
        this.productId = data.productId;

    }

    public String getId() {
        return id;
    }

    public String getLink() {
        return link;
    }

    public Integer getScore() {
        return score;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}