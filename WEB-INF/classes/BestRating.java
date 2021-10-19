public class BestRating {
    Long productId;
    String rating;

    public BestRating(Long productId, String rating) {
        this.productId = productId;
        this.rating = rating;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}