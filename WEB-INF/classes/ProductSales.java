public class ProductSales {
    private Long productId;
    private String productName;
    private double price;
    private Integer productSold = 0;
    private double totalSales = 0;

    public ProductSales(Long productId, String productName, double price, Integer productSold, double totalSales) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.productSold = productSold;
        this.totalSales = totalSales;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getProductSold() {
        return productSold;
    }

    public void setProductSold(Integer productSold) {
        this.productSold = productSold;
    }

    public double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
    }

    @Override
    public String toString() {
        return "ProductSales{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", productSold=" + productSold +
                ", totalSales=" + totalSales +
                '}';
    }
}
