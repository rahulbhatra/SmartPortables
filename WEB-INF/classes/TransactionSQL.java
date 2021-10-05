import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

@WebServlet("/TransactionSQL")
public class TransactionSQL extends HttpServlet {

    public static Transaction getTransaction(Connection conn, Long transactionId) {


        try {
            //select the table
            String selectOrderQuery = "select * from Transactions where transactionId=?;";
            System.out.println(selectOrderQuery + transactionId);
            PreparedStatement pst = conn.prepareStatement(selectOrderQuery);
            pst.setLong(1, transactionId);

            System.out.println(pst.toString());
            ResultSet rs = pst.executeQuery();
            Transaction transaction = null;
            while(rs.next()) {
                transaction = new Transaction(
                        rs.getLong("transactionId"),
                        rs.getLong("userId"),
                        rs.getLong("storeLocationId"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("address1"),
                        rs.getString("address2"),
                        rs.getString("city"),
                        rs.getString("state"),
                        rs.getString("zipcode"),
                        rs.getString("phone"),
                        rs.getString("deliveryOption"),
                        rs.getDate("purchaseDate"),
                        rs.getDate("shipDate"),
                        rs.getDouble("shippingCost"),
                        rs.getDouble("totalSales"),
                        rs.getString("creditCardNo"));
            }
            return transaction;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Transaction addTransaction(Connection conn, Transaction transaction) {


        try {
            //select the table
            String selectOrderQuery = "insert into Transactions(userId, storeLocationId, firstName, lastName, " +
                    "address1, address2, city, state, zipcode, phone," +
                    "deliveryOption, purchaseDate, shipDate," +
                    "shippingCost, totalSales, creditCardNo)" +
                    "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

            PreparedStatement pst = conn.prepareStatement(selectOrderQuery, PreparedStatement.RETURN_GENERATED_KEYS);

            pst.setLong(1, transaction.getUserId());
            pst.setLong(2, transaction.getTransactionId());
            pst.setString(3, transaction.getFirstName());
            pst.setString(4, transaction.getLastName());
            pst.setString(5, transaction.getAddress1());
            pst.setString(6, transaction.getAddress2());
            pst.setString(7, transaction.getCity());
            pst.setString(8, transaction.getState());
            pst.setString(9,transaction.getZipcode());
            pst.setString(10, transaction.getPhone());
            pst.setString(11,transaction.getDeliveryOption());
            pst.setDate(12, (Date) transaction.getPurchaseDate());
            pst.setDate(13, (Date) transaction.getShipDate());
            pst.setDouble(14,transaction.getShippingCost());
            pst.setDouble(15,transaction.getTotalSales());
            pst.setString(16,transaction.getCreditCardNo());

            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            if(rs.next()) {
                transaction.setTransactionId(rs.getLong(1));
            }

            return transaction;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
