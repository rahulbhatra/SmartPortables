import java.util.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;


@WebServlet("/Product")
public class Product extends HttpServlet{
	private Long productId;
	private String productName;
	private double price;
	private String image;
	private ProductManufacturers manufacturer;
	private String condition;
	private double discount;
	private String description;
	Map<Integer, Integer> accessories = new HashMap<>();
	private ProductCategory category;
	private double rebate;
	private Integer count = 10;

	public Product(Long productId, String name, double price, String image, ProductManufacturers manufacturer, String condition,
				   double discount, String description, ProductCategory category, double rebate){
		this.productId = productId;
		this.productName =name;
		this.price=price;
		this.image=image;
		this.manufacturer = manufacturer;
		this.condition=condition;
		this.discount = discount;
		this.description = description;
		this.category = category;
		this.rebate = rebate;
	}

	Map<Integer,Integer> getAccessories() {
		return accessories;
	}

	public Product(){}

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
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public ProductManufacturers getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(ProductManufacturers manufacturer) {
		this.manufacturer = manufacturer;
	}

	public void setAccessories( HashMap<Integer,Integer> accessories) {
		this.accessories = accessories;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ProductCategory getCategory() {
		return category;
	}

	public void setCategory(ProductCategory category) {
		this.category = category;
	}

	public double getRebate() {
		return rebate;
	}

	public void setRebate(double rebate) {
		this.rebate = rebate;
	}


	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "Product{" +
				"id='" + productId + '\'' +
				", name='" + productName + '\'' +
				", price=" + price +
				", image='" + image + '\'' +
				", manufacturer=" + manufacturer +
				", condition='" + condition + '\'' +
				", discount=" + discount +
				", description='" + description + '\'' +
				", accessories=" + accessories +
				", category='" + category + '\'' +
				", rebate=" + rebate +
				'}';
	}
}
