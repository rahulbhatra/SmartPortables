import java.io.*;
import java.util.Date;


/* 
	Review class contains class variables username,productname,reviewtext,reviewdate,reviewrating

	Review class has a constructor with Arguments username,productname,reviewtext,reviewdate,reviewrating
	  
	Review class contains getters and setters for username,productname,reviewtext,reviewdate,reviewrating
*/

public class Review implements Serializable {
    private Long productId;
    private String productName;
    private String productCategory;
    private double productPrice;
    private String productManufacturer;
    private boolean productIsOnSale;

    private Long userId;
    private String userName;
    private double userAge;
    private String userGender;
    private String userOccupation;

    private Long storeLocationId;
    private String storeStreetAddress;
    private String storeCity;
    private String storeState;
    private String storeZipCode;

    private Integer reviewRating;
    private String reviewDate;
    private String reviewText;
    private String retailerZipCode;
    private String retailerCity;

    public Review(Long productId, String productName, String productCategory, double productPrice,
                  String productManufacturer, boolean productIsOnSale, Long userId, String userName, double userAge,
                  String userGender, String userOccupation, Long storeLocationId,
                  String storeStreetAddress, String storeCity, String storeState, String storeZipCode,
                  Integer reviewRating, String reviewDate, String reviewText, String retailerZipCode, String retailerCity) {
        this.productId = productId;
        this.productName = productName;
        this.productCategory = productCategory;
        this.productPrice = productPrice;
        this.productManufacturer = productManufacturer;
        this.productIsOnSale = productIsOnSale;
        this.userId = userId;
        this.userName = userName;
        this.userAge = userAge;
        this.userGender = userGender;
        this.userOccupation = userOccupation;
        this.storeLocationId = storeLocationId;
        this.storeStreetAddress = storeStreetAddress;
        this.storeCity = storeCity;
        this.storeState = storeState;
        this.storeZipCode = storeZipCode;
        this.reviewRating = reviewRating;
        this.reviewDate = reviewDate;
        this.reviewText = reviewText;
        this.retailerZipCode = retailerZipCode;
        this.retailerCity = retailerCity;
    }

    public Review(Long productId, int reviewRating, String retailerZipCode) {
        this.productId = productId;
        this.reviewRating = reviewRating;
        this.retailerZipCode = retailerZipCode;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public double getUserAge() {
        return userAge;
    }

    public void setUserAge(double userAge) {
        this.userAge = userAge;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserOccupation() {
        return userOccupation;
    }

    public void setUserOccupation(String userOccupation) {
        this.userOccupation = userOccupation;
    }

    public Long getStoreLocationId() {
        return storeLocationId;
    }

    public void setStoreLocationId(Long storeLocationId) {
        this.storeLocationId = storeLocationId;
    }

    public String getStoreStreetAddress() {
        return storeStreetAddress;
    }

    public void setStoreStreetAddress(String storeStreetAddress) {
        this.storeStreetAddress = storeStreetAddress;
    }

    public String getStoreCity() {
        return storeCity;
    }

    public void setStoreCity(String storeCity) {
        this.storeCity = storeCity;
    }

    public String getStoreState() {
        return storeState;
    }

    public void setStoreState(String storeState) {
        this.storeState = storeState;
    }

    public String getStoreZipCode() {
        return storeZipCode;
    }

    public void setStoreZipCode(String storeZipCode) {
        this.storeZipCode = storeZipCode;
    }

    public Integer getReviewRating() {
        return reviewRating;
    }

    public void setReviewRating(Integer reviewRating) {
        this.reviewRating = reviewRating;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public String getRetailerZipCode() {
        return retailerZipCode;
    }

    public void setRetailerZipCode(String retailerZipCode) {
        this.retailerZipCode = retailerZipCode;
    }

    public String getRetailerCity() {
        return retailerCity;
    }

    public void setRetailerCity(String retailerCity) {
        this.retailerCity = retailerCity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductManufacturer() {
        return productManufacturer;
    }

    public void setProductManufacturer(String productManufacturer) {
        this.productManufacturer = productManufacturer;
    }

    public boolean isProductIsOnSale() {
        return productIsOnSale;
    }

    public void setProductIsOnSale(boolean productIsOnSale) {
        this.productIsOnSale = productIsOnSale;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
