import java.sql.*;
import java.util.*;

public class MySqlDataStoreUtilities {
    static Connection conn = null;
    static String message;

    public static String getConnection() {

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/exampledatabase", "root", "Innovation@9275");
            message = "Successfull";
            return message;
        } catch (SQLException e) {
            message = "unsuccessful";
            return message;
        } catch (Exception e) {
            message = e.getMessage();
            return message;
        }
    }

    public static void insertProducts() {
        getConnection();
        ProductDetailsSQL.insertProducts(conn);
    }

    public static Map<Long, Product> getProductsByCategory(ProductCategory productCategory) {
        getConnection();
        return ProductDetailsSQL.getProductsByCategory(conn, productCategory);
    }

    public static Product getProduct(Long productId) {
        getConnection();
        return ProductDetailsSQL.getProduct(conn, productId);
    }

    public static String addProducts(Product product) {
        getConnection();
        return ProductDetailsSQL.addProducts(conn, product);
    }

    public static String updateProducts(Long productId, String productName, double productPrice, String productImage, String productManufacturer, String productCondition, double productDiscount) {
        getConnection();
        return ProductDetailsSQL.updateProducts(conn, productId, productName, productPrice, productImage, productManufacturer, productCondition, productDiscount);
    }

    public static String deleteProducts(Long productId) {
        getConnection();
        return ProductDetailsSQL.deleteProducts(conn, productId);
    }


    /*
    *
    * Customer Order Crud Operations
    *
    * */
    public static void deleteCustomerOrder(Long customerOrderId) {
        try {

            getConnection();
            String deleteOrderQuery = "Delete from customerOrders where customerOrderId=?";
            PreparedStatement pst = conn.prepareStatement(deleteOrderQuery);
            pst.setLong(1, customerOrderId);
            pst.executeUpdate();
        } catch (Exception e) {

        }
    }

    public static void insertCustomerOrder(CustomerOrder customerOrder) {
        try {

            getConnection();

            String insertIntoCustomerOrderQuery = "INSERT INTO customerOrders(" +
                    "transactionId, userId, orderName, orderPrice, warrantyPrice) "
                    + "VALUES (?,?,?,?,?);";

            System.out.println(insertIntoCustomerOrderQuery);

            PreparedStatement pst = conn.prepareStatement(insertIntoCustomerOrderQuery);
            //set the parameter for each column and execute the prepared statement
            pst.setLong(1, customerOrder.getTransactionId());
            pst.setLong(2, customerOrder.getUserId());
            pst.setString(3, customerOrder.getOrderName());
            pst.setDouble(4, customerOrder.getOrderPrice());
            pst.setDouble(5, customerOrder.getWarrantyPrice());
            pst.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static CustomerOrder getCustomerOrder(Long customerOrderId) {
        getConnection();
        return CustomerOrderSQL.getCustomerOrder(conn, customerOrderId);
    }

    public static HashMap<Integer, ArrayList<CustomerOrder>> selectCustomerOrders() {
        getConnection();
        return CustomerOrderSQL.selectCustomerOrders(conn);
    }

    public static List<CustomerOrder> getCustomerOrdersByUserId(Long userId) {
        getConnection();
        return CustomerOrderSQL.getCustomerOrderByUserId(conn, userId);
    }


    public static void insertUser(String userName, String password, String rePassword, String userType) {
        getConnection();
        RegistrationSQL.insertUser(conn, userName, password, rePassword, userType);
    }

    public static Map<String, User> selectUser() {
        getConnection();
        return RegistrationSQL.selectUser(conn);
    }

    public static User getUser(Long userId) {
        getConnection();
        return RegistrationSQL.getUser(conn, userId);
    }

    public static List<StoreLocation> getStoreLocations() {
        getConnection();
        List<StoreLocation> storeLocations = new ArrayList<>();
        try {
            String query = "select * from StoreLocations;";
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                StoreLocation storeLocation = new StoreLocation(
                        rs.getInt("storeLocationId"),
                        rs.getString("streetAddress"),
                        rs.getString("city"),
                        rs.getString("state"),
                        rs.getString("zipCode")
                );
                storeLocations.add(storeLocation);

            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return storeLocations;
    }

    public static StoreLocation getStoreLocation(Long storeLocationId) {
        getConnection();
        try {
            String query = "select * from StoreLocations where storeLocationId=?;";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setLong(1, storeLocationId);
            ResultSet rs = pst.executeQuery();
            StoreLocation storeLocation = null;
            while (rs.next()) {

                storeLocation = new StoreLocation(
                        rs.getInt("storeLocationId"),
                        rs.getString("streetAddress"),
                        rs.getString("city"),
                        rs.getString("state"),
                        rs.getString("zipCode")
                );

            }
            return storeLocation;
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return null;
    }


    public static Transaction getTransaction(Long transactionId) {
        getConnection();
        return TransactionSQL.getTransaction(conn, transactionId);
    }

    public static Transaction addTransaction(Transaction transaction) {
        getConnection();
        return TransactionSQL.addTransaction(conn, transaction);
    }

    public static void updateTransaction(Transaction transaction) {
        getConnection();
        TransactionSQL.updateTransaction(conn, transaction);
    }


}	