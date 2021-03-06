import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
                        rs.getString("creditCardNo"),
                        rs.getInt("quantity"));
            }
            return transaction;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Transaction> getTransactions(Connection conn) {

        List<Transaction> transactions = new ArrayList<>();
        try {
            //select the table
            String selectOrderQuery = "select * from Transactions;";

            PreparedStatement pst = conn.prepareStatement(selectOrderQuery);


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
                        rs.getString("creditCardNo"),
                        rs.getInt("quantity"));

                transactions.add(transaction);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public static Transaction addTransaction(Connection conn, Transaction transaction) {


        try {
            //select the table
            String selectOrderQuery = "insert into Transactions(userId, storeLocationId, firstName, lastName, " +
                    "address1, address2, city, state, zipcode, phone," +
                    "deliveryOption, purchaseDate, shipDate," +
                    "shippingCost, totalSales, creditCardNo, quantity)" +
                    "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

            PreparedStatement pst = conn.prepareStatement(selectOrderQuery, PreparedStatement.RETURN_GENERATED_KEYS);

            pst.setLong(1, transaction.getUserId());
            pst.setLong(2, transaction.getStoreLocationId());
            pst.setString(3, transaction.getFirstName());
            pst.setString(4, transaction.getLastName());
            pst.setString(5, transaction.getAddress1());
            pst.setString(6, transaction.getAddress2());
            pst.setString(7, transaction.getCity());
            pst.setString(8, transaction.getState());
            pst.setString(9,transaction.getZipcode());
            pst.setString(10, transaction.getPhone());
            pst.setString(11,transaction.getDeliveryOption());
            pst.setDate(12, new Date(transaction.getPurchaseDate().getTime()));
            pst.setDate(13, new Date(transaction.getShipDate().getTime()));
            pst.setDouble(14,transaction.getShippingCost());
            pst.setDouble(15,transaction.getTotalSales());
            pst.setString(16,transaction.getCreditCardNo());
            pst.setInt(17,transaction.getQuantity());

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


    public static void updateTransaction(Connection conn, Transaction transaction) {


        try {
            //select the table
            String selectOrderQuery = "update Transactions set storeLocationId=?, firstName=?, lastName=?, " +
                    "address1=?, address2=?, city=?, state=?, zipcode=?, phone=?," +
                    "deliveryOption=?, purchaseDate=?, shipDate=?, creditCardNo=? where transactionId=?;";

            PreparedStatement pst = conn.prepareStatement(selectOrderQuery);

            pst.setLong(1, transaction.getStoreLocationId());
            pst.setString(2, transaction.getFirstName());
            pst.setString(3, transaction.getLastName());
            pst.setString(4, transaction.getAddress1());
            pst.setString(5, transaction.getAddress2());
            pst.setString(6, transaction.getCity());
            pst.setString(7, transaction.getState());
            pst.setString(8,transaction.getZipcode());
            pst.setString(9, transaction.getPhone());
            pst.setString(10,transaction.getDeliveryOption());
            pst.setDate(11, new Date(transaction.getPurchaseDate().getTime()));
            pst.setDate(12, new Date(transaction.getShipDate().getTime()));
            pst.setString(13,transaction.getCreditCardNo());
            pst.setLong(14, transaction.getTransactionId());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Mostsoldzip> getTopNMostSoldZip(Connection conn, int n) {
        List<Mostsoldzip> mostSoldZips = new ArrayList<>();
        try {
            String query = "select zipCode, count(quantity) as zipCount from transactions group by zipCode order by count(zipCode) desc " +
                    "limit ?;";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, n);
            ResultSet rs = pst.executeQuery();

            while( rs.next()) {
                Mostsoldzip mostsoldzip = new Mostsoldzip(rs.getString("zipCode"),
                        rs.getString("zipCount"));
                mostSoldZips.add(mostsoldzip);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mostSoldZips;
    }

    public static List<MostSold> getTopNMostSold(Connection conn, int n) {
        List<MostSold> mostSolds = new ArrayList<>();
        try {
            String query = "select c.productId as productId, c.orderName as productName, count(c.productId) as productCount from transactions t inner join " +
                    "CustomerOrders c on t.transactionId = c.transactionId group by c.productId, c.orderName order by count(c.productId) desc " +
                    "limit ?;";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, n);
            ResultSet rs = pst.executeQuery();

            while( rs.next()) {
                MostSold mostSold = new MostSold(rs.getLong("productId"),
                        rs.getString("productCount"));
                mostSolds.add(mostSold);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mostSolds;
    }

}
