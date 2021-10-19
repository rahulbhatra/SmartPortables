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

    public static List<Product> getProducts() {
        getConnection();
        return ProductDetailsSQL.getProducts(conn);
    }

    public static Product addProducts(Product product) {
        getConnection();
        return ProductDetailsSQL.addProducts(conn, product);
    }

    public static String updateProducts(Product product) {
        getConnection();
        return ProductDetailsSQL.updateProducts(conn, product);
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
        getConnection();
        CustomerOrderSQL.insertCustomerOrder(conn, customerOrder);
    }

    public static CustomerOrder getCustomerOrder(Long customerOrderId) {
        getConnection();
        return CustomerOrderSQL.getCustomerOrder(conn, customerOrderId);
    }

    public static List<CustomerOrder> getCustomerOrders() {
        getConnection();
        return CustomerOrderSQL.getCustomerOrders(conn);
    }

    public static HashMap<Integer, ArrayList<CustomerOrder>> selectCustomerOrders() {
        getConnection();
        return CustomerOrderSQL.selectCustomerOrders(conn);
    }

    public static List<CustomerOrder> getCustomerOrdersByUserId(Long userId) {
        getConnection();
        return CustomerOrderSQL.getCustomerOrderByUserId(conn, userId);
    }


    public static void insertUser(String userName, String password, String rePassword, String userType,
                                  Integer userAge,
                                  String userGender, String userOccupation) {
        getConnection();
        RegistrationSQL.insertUser(conn, userName, password, rePassword, userType, userAge, userGender, userOccupation);
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
                        rs.getLong("storeLocationId"),
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
                        rs.getLong("storeLocationId"),
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

    public static List<Transaction> getTransactions() {
        getConnection();
        return TransactionSQL.getTransactions(conn);
    }

    public static Transaction addTransaction(Transaction transaction) {
        getConnection();
        return TransactionSQL.addTransaction(conn, transaction);
    }

    public static void updateTransaction(Transaction transaction) {
        getConnection();
        TransactionSQL.updateTransaction(conn, transaction);
    }

    public static Customer getCustomerByUserId(Long userId) {
        getConnection();
        return CustomerSQL.getCustomer(conn, userId);
    }

    public static Customer insertCustomer(Customer customer) {
        getConnection();
        return CustomerSQL.insertCustomer(conn, customer);
    }

    public static void updateCustomer(Customer customer) {
        getConnection();
        CustomerSQL.updateCustomer(conn, customer);
    }

    public static List<Long> getProductAccessories(Long productId) {
        getConnection();
        return ProductAccessorySQL.getProductAccessories(conn, productId);
    }

    public static void insertProductAccessory(Long productId, Long accessoryId) {
        getConnection();
        ProductAccessorySQL.insertProductAccessory(conn, productId, accessoryId);
    }


    public static void deleteProductAccessory(Long productId, Long accessoryId) {
        getConnection();
        ProductAccessorySQL.deleteProductAccessory(conn, productId, accessoryId);
    }

    public static List<Mostsoldzip> getTopNMostSoldZip(int n) {
        getConnection();
        return TransactionSQL.getTopNMostSoldZip(conn, n);
    }

    public static List<MostSold> getTopNMostSold(int n) {
        getConnection();
        return TransactionSQL.getTopNMostSold(conn, n);
    }

}	