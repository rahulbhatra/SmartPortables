import java.io.*;


public class CustomerOrder implements Serializable{
	private Long customerOrderId;
	private Long transactionId;
	private Long productId;
	private Long userId;
	private String orderName;
	private Double orderPrice;
	private Double warrantyPrice;

	public CustomerOrder(Long customerOrderId, Long transactionId, Long productId, Long userId, String orderName, Double orderPrice, Double warrantyPrice) {
		this.customerOrderId = customerOrderId;
		this.transactionId = transactionId;
		this.userId = userId;
		this.orderName = orderName;
		this.orderPrice = orderPrice;
		this.warrantyPrice = warrantyPrice;
		this.productId = productId;
	}

	public Long getCustomerOrderId() {
		return customerOrderId;
	}

	public void setCustomerOrderId(Long customerOrderId) {
		this.customerOrderId = customerOrderId;
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public Double getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(Double orderPrice) {
		this.orderPrice = orderPrice;
	}

	public Double getWarrantyPrice() {
		return warrantyPrice;
	}

	public void setWarrantyPrice(Double warrantyPrice) {
		this.warrantyPrice = warrantyPrice;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}
}
