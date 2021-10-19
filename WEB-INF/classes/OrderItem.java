import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;


@WebServlet("/OrderItem")

/*
	OrderItem class contains class variables name,price,image,retailer.

	OrderItem  class has a constructor with Arguments name,price,image,retailer.

	OrderItem  class contains getters and setters for name,price,image,retailer.
*/

public class OrderItem extends HttpServlet {
    private Long productId;
    private String name;
    private double price;
    private String image;
    private ProductManufacturers productManufacturers;
    private boolean warrantyIncluded;
    private double discount;
    private double rebate;

    public OrderItem(Long productId, String name, double price, String image, ProductManufacturers productManufacturers, boolean warrantyIncluded, double discount
            , double rebate) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.image = image;
        this.productManufacturers = productManufacturers;
        this.warrantyIncluded = warrantyIncluded;
        this.discount = discount;
        this.rebate = rebate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ProductManufacturers getProductManufacturers() {
        return productManufacturers;
    }

    public void setProductManufacturers(ProductManufacturers productManufacturers) {
        this.productManufacturers = productManufacturers;
    }

    public boolean isWarrantyIncluded() {
        return warrantyIncluded;
    }

    public void setWarrantyIncluded(boolean warrantyIncluded) {
        this.warrantyIncluded = warrantyIncluded;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getRebate() {
        return rebate;
    }

    public void setRebate(double rebate) {
        this.rebate = rebate;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
