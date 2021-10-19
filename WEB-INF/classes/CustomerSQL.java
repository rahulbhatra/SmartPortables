import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/CustomerSQL")
public class CustomerSQL extends HttpServlet {

    public static Customer insertCustomer(Connection conn, Customer customer) {
        try {
            String query = "INSERT INTO Customer(userId, firstName, lastName," +
                    "address1, address2, city, state, zipcode, phone) "
                    + "VALUES (?,?,?,?,?,?,?,?,?);";
            PreparedStatement pst = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setLong(1, customer.getUserId());
            pst.setString(2, customer.getFirstName());
            pst.setString(3, customer.getLastName());
            pst.setString(4, customer.getAddress1());
            pst.setString(5, customer.getAddress2());
            pst.setString(6, customer.getCity());
            pst.setString(7, customer.getState());
            pst.setString(8,customer.getZipcode());
            pst.setString(9,customer.getPhone());
            pst.execute();
            ResultSet rs = pst.getGeneratedKeys();
            if(rs.next()) {
                customer.setCustomerId(rs.getLong(1));
            }
            return customer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void updateCustomer(Connection conn, Customer customer) {
        try {
            String query = "update Customer set firstName=?, lastName=?," +
                    "address1=?, address2=?, city=?, state=?, zipcode=?, phone=? where userId=?;";
            PreparedStatement pst = conn.prepareStatement(query);

            pst.setString(1, customer.getFirstName());
            pst.setString(2, customer.getLastName());
            pst.setString(3, customer.getAddress1());
            pst.setString(4, customer.getAddress2());
            pst.setString(5, customer.getCity());
            pst.setString(6, customer.getState());
            pst.setString(7,customer.getZipcode());
            pst.setString(8,customer.getPhone());
            pst.setLong(9,customer.getUserId());

            pst.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Customer getCustomer(Connection conn, Long userId) {
        try {
            String query = "select * from  Customer where userId=?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setLong(1, userId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Customer customer = new Customer(rs.getLong("customerId"),
                        rs.getLong("userId"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("address1"),
                        rs.getString("address2"),
                        rs.getString("city"),
                        rs.getString("state"),
                        rs.getString("zipcode"),
                        rs.getString("phone"));
                return customer;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
