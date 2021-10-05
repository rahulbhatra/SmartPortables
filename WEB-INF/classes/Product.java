import java.util.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;


@WebServlet("/Product")
public class Product extends HttpServlet{
	private Long productId;
	private String name;
	private double price;
	private String image;
	private ProductManufacturers manufacturer;
	private String condition;
	private double discount;
	private String description;
	Map<Integer, Integer> accessories = new HashMap<>();
	private ProductCategory category;
	private double rebate;

	public Product(Long productId, String name, double price, String image, ProductManufacturers manufacturer, String condition,
				   double discount, String description, ProductCategory category, double rebate){
		this.productId = productId;
		this.name=name;
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

	@Override
	public String toString() {
		return "Product{" +
				"id='" + productId + '\'' +
				", name='" + name + '\'' +
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
