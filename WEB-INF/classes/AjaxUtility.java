import java.util.*;

import java.sql.*;


public class AjaxUtility {
    StringBuffer sb = new StringBuffer();
    boolean namesAdded = false;
    static Connection conn = null;
    static String message;

    public static String getConnection() {

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/exampledatabase", "root", "root");
            message = "Successfull";
            return message;
        } catch (SQLException e) {
            message = "unsuccessful";
            return message;
        } catch (Exception e) {
            message = "unsuccessful";
            return message;
        }
    }

    public StringBuffer readData(String searchId) {

        List<Product> products = MySqlDataStoreUtilities.getProducts();
        for (Product product : products) {
            if (product.getProductName().toLowerCase().startsWith(searchId.toLowerCase())) {
                sb.append("<product>");
                sb.append("<id>" + product.getProductId() + "</id>");
                sb.append("<productName>" + product.getProductName() + "</productName>");
                sb.append("</product>");
            }
        }
        return sb;
    }

    public static void storeData(HashMap<String, Product> productdata) {
        try {

            getConnection();

            String insertIntoProductQuery = "INSERT INTO product(productId,productName,image,retailer,productCondition,productPrice,productType,discount) "
                    + "VALUES (?,?,?,?,?,?,?,?);";
            for (Map.Entry<String, Product> entry : productdata.entrySet()) {

                PreparedStatement pst = conn.prepareStatement(insertIntoProductQuery);
                //set the parameter for each column and execute the prepared statement
                pst.setLong(1, entry.getValue().getProductId());
                pst.setString(2, entry.getValue().getProductName());
                pst.setString(3, entry.getValue().getImage());
                pst.setString(4, entry.getValue().getManufacturer().toString());
                pst.setString(5, entry.getValue().getCondition());
                pst.setDouble(6, entry.getValue().getPrice());
                pst.setString(7, entry.getValue().getCategory().toString());
                pst.setDouble(8, entry.getValue().getDiscount());
                pst.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
