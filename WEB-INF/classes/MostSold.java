public class MostSold {
    Long productId;
    String productName;
    String count;


    public MostSold(Long productId, String count) {

        this.productId = productId;
        this.count = count;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}