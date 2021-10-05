import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@WebServlet("/CustomerOrderSQL")
public class CustomerOrderSQL extends HttpServlet {
    public static void deleteCustomerOrder(Connection conn, int orderId, String userName, String orderName) {
        try {
            String deleteOrderQuery = "Delete from customerorders where OrderId=? and userName=? and orderName=?";
            PreparedStatement pst = conn.prepareStatement(deleteOrderQuery);
            pst.setInt(1, orderId);
            pst.setString(2, userName);
            pst.setString(3, orderName);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertCustomerOrder(Connection conn, int orderId,
                                           String userName,
                                           String firstName,
                                           String lastName,
                                           String address1,
                                           String address2,
                                           String city,
                                           String state,
                                           String zipCode,
                                           String phone,
                                           String deliveryOption,
                                           String pickupLocation,
                                           String orderName,
                                           double orderPrice,
                                           String creditCardNo,
                                           double warrantyPrice) {
        try {

            String insertIntoCustomerOrderQuery = "INSERT INTO customerOrders(" +
                    "orderId, userName, firstName, lastName, address1, address2, city, state, zipcode, phone, deliveryOption," +
                    "pickupLocation, orderName, orderPrice, creditCardNo, warrantyPrice) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

            System.out.println(insertIntoCustomerOrderQuery);

            PreparedStatement pst = conn.prepareStatement(insertIntoCustomerOrderQuery);
            //set the parameter for each column and execute the prepared statement
            pst.setInt(1, orderId);
            pst.setString(2, userName);
            pst.setString(3, firstName);
            pst.setString(4, lastName);
            pst.setString(5, address1);
            pst.setString(6, address2);
            pst.setString(7, city);
            pst.setString(8, state);
            pst.setString(9, zipCode);
            pst.setString(10, phone);
            pst.setString(11, deliveryOption);
            pst.setString(12, pickupLocation);
            pst.setString(13, orderName);
            pst.setDouble(14, orderPrice);
            pst.setString(15, creditCardNo);
            pst.setDouble(16, warrantyPrice);
            pst.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateCustomerOrder(Connection conn, int orderId,
                                           String userName,
                                           String firstName,
                                           String lastName,
                                           String address1,
                                           String address2,
                                           String city,
                                           String state,
                                           String zipCode,
                                           String phone,
                                           String deliveryOption,
                                           String pickupLocation,
                                           String orderName,
                                           double orderPrice,
                                           String creditCardNo,
                                           double warrantyPrice) {
        try {

            String updateIntoCustomerOrderQuery = "update customerOrders " +
                    "set firstName=?, lastName=?, address1=?, address2=?, city=?, state=?, zipcode=?, phone=?, deliveryOption=?," +
                    "pickupLocation=?, orderName=?, creditCardNo=? where orderId=? and userName=? and orderName=?;";

            System.out.println(updateIntoCustomerOrderQuery);

            PreparedStatement pst = conn.prepareStatement(updateIntoCustomerOrderQuery);
            //set the parameter for each column and execute the prepared statement
            pst.setString(1, firstName);
            pst.setString(2, lastName);
            pst.setString(3, address1);
            pst.setString(4, address2);
            pst.setString(5, city);
            pst.setString(6, state);
            pst.setString(7, zipCode);
            pst.setString(8, phone);
            pst.setString(9, deliveryOption);
            pst.setString(10, pickupLocation);
            pst.setString(11, orderName);
            pst.setString(12, creditCardNo);
            pst.setInt(13, orderId);
            pst.setString(14, userName);
            pst.setString(15, orderName);
            pst.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static CustomerOrder getCustomerOrder(Connection conn, Long customerOrderId) {


        try {
            //select the table
            String selectOrderQuery = "select * from customerOrders where customerOrderId=?;";
            System.out.println(selectOrderQuery + customerOrderId);
            PreparedStatement pst = conn.prepareStatement(selectOrderQuery);
            pst.setLong(1, customerOrderId);

            System.out.println(pst.toString());
            ResultSet rs = pst.executeQuery();
            CustomerOrder order = null;
            while(rs.next()) {
                order = new CustomerOrder(
                        rs.getLong("customerOrderId"),
                        rs.getLong("transactionId"),
                        rs.getLong("userId"),
                        rs.getString("orderName"),
                        rs.getDouble("orderPrice"),
                        rs.getDouble("warrantyPrice"));
            }
            return order;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<CustomerOrder> getCustomerOrderByUserId(Connection conn, Long userId) {

        List<CustomerOrder> customerOrders = new ArrayList<>();
        try {
            //select the table
            String selectOrderQuery = "select * from customerOrders where userId=?;";
            System.out.println(selectOrderQuery + userId);
            PreparedStatement pst = conn.prepareStatement(selectOrderQuery);
            pst.setLong(1, userId);

            System.out.println(pst.toString());
            ResultSet rs = pst.executeQuery();

            while(rs.next()) {
                CustomerOrder order = new CustomerOrder(
                        rs.getLong("customerOrderId"),
                        rs.getLong("transactionId"),
                        rs.getLong("userId"),
                        rs.getString("orderName"),
                        rs.getDouble("orderPrice"),
                        rs.getDouble("warrantyPrice"));
                customerOrders.add(order);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return customerOrders;
    }

    public static HashMap<Integer, ArrayList<CustomerOrder>> selectCustomerOrders(Connection conn) {

        HashMap<Integer, ArrayList<CustomerOrder>> orderPayments = new HashMap<Integer, ArrayList<CustomerOrder>>();

        try {
            //select the table
            String selectOrderQuery = "select * from customerOrders";
            PreparedStatement pst = conn.prepareStatement(selectOrderQuery);
            ResultSet rs = pst.executeQuery();
            ArrayList<CustomerOrder> orderList = new ArrayList<CustomerOrder>();
            while (rs.next()) {
                if (!orderPayments.containsKey(rs.getInt("OrderId"))) {
                    ArrayList<CustomerOrder> arr = new ArrayList<CustomerOrder>();
                    orderPayments.put(rs.getInt("orderId"), arr);
                }
                ArrayList<CustomerOrder> listCustomerOrder = orderPayments.get(rs.getInt("OrderId"));
                CustomerOrder customerOrder = new CustomerOrder(
                        rs.getLong("customerOrderId"),
                        rs.getLong("transactionId"),
                        rs.getLong("userId"),
                        rs.getString("orderName"),
                        rs.getDouble("orderPrice"),
                        rs.getDouble("warrantyPrice"));
                listCustomerOrder.add(customerOrder);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderPayments;
    }
}
