import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@WebServlet("/ViewOrder")

public class ViewOrder extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        Utilities utility = new Utilities(request, pw);

        if (!utility.isLoggedin()) {
            HttpSession session = request.getSession(true);
            session.setAttribute("login_msg", "Please Login to View your Orders");
            response.sendRedirect("Login");
            return;
        }
        List<CustomerOrder> customerOrders = MySqlDataStoreUtilities.getCustomerOrdersByUserId(utility.getUserId());
        utility.printHtml("Header.html");
        utility.printHtml("LeftNavigationBar.html");

        pw.print("<form name ='ViewOrder' action='ViewOrder' method='get'>");
        pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
        pw.print("<a style='font-size: 24px;'>Order</a>");
        pw.print("</h2><div class='entry'>");

        if (request.getParameter("Order") == null) {
            pw.print("<table align='center'>" +
                        "<tr>" +
                            "<td>Enter OrderId </td>");
            pw.print("<td><select style='radius: 20px; width: 100%;' name='orderId' id='orderId'>");
            for (CustomerOrder customerOrder: customerOrders) {
                    pw.print("<option value='" + customerOrder.getCustomerOrderId() + "'>" + customerOrder.
                            getCustomerOrderId() + " " + customerOrder.getOrderName() + "</option>");
            }
            pw.print("</td>");
            pw.print(       "<td><input type='submit' name='Order' value='ViewOrder' class='btnbuy'></td>" +
                        "</tr>" +
                    "</table>");
        }

        if (request.getParameter("Order") != null && request.getParameter("Order").equals("ViewOrder")) {
            if (request.getParameter("orderId") != null && !request.getParameter("orderId").isEmpty()) {
                Long orderId = Long.parseLong(request.getParameter("orderId"));
                List<CustomerOrder> customerOrders1 = new ArrayList<>();
                pw.print("<input type='hidden' name='orderId' value='" + orderId + "'>");
                try {
                    customerOrders1 = MySqlDataStoreUtilities.getCustomerOrdersByUserId(utility.getUserId());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (customerOrders1.size() > 0) {
                    pw.print("<table  class='gridtable'>");
                    pw.print("<tr><td></td>");
                    pw.print("<td>OrderId:</td>");
                    pw.print("<td>UserName:</td>");
                    pw.print("<td>productOrdered:</td>");
                    pw.print("<td>productPrice:</td></tr>");
                    for (CustomerOrder customerOrder : customerOrders1) {
                        if(customerOrder.getCustomerOrderId() != orderId) {
                            continue;
                        }
                        User user = MySqlDataStoreUtilities.getUser(customerOrder.getUserId());
                        pw.print("<tr>");
                        pw.print("<td><input type='radio' name='orderName' value='" + customerOrder.getOrderName() + "'></td>");
                        pw.print("<td>" + customerOrder.getCustomerOrderId() + ".</td><td>" + user.getUserName() + "</td><td>" + customerOrder.getOrderName() + "</td><td>Price: " + customerOrder.getOrderPrice() + "</td>");
                        pw.print("<td><input type='submit' name='Order' value='CancelOrder' class='btnbuy'></td>");
                        pw.print("</tr>");
                    }

                    pw.print("</table>");
                } else {
                    pw.print("<h4 style='color:red'>You have not placed any order with this order id</h4>");
                }
            } else {
                pw.print("<h4 style='color:red'>Please enter a valid order id</h4>");
            }
        }

        if (request.getParameter("Order") != null && request.getParameter("Order").equals("CancelOrder")) {
            String orderName = request.getParameter("orderName");
            if (request.getParameter("orderName") != null) {

                Long orderId = Long.parseLong(request.getParameter("orderId"));
                ArrayList<CustomerOrder> listCustomerOrder = new ArrayList<>();
                List<CustomerOrder> orderPayments = MySqlDataStoreUtilities.getCustomerOrdersByUserId(utility.getUserId());

                for (CustomerOrder customerOrder : orderPayments) {
                    if (customerOrder.getOrderName().equals(orderName)) {
                        MySqlDataStoreUtilities.deleteCustomerOrder(orderId);
                        listCustomerOrder.add(customerOrder);
                        pw.print("<h4 style='color:red'>Your Order is Cancelled</h4>");
                    }
                }
            } else {
                pw.print("<h4 style='color:red'>Please select any product</h4>");
            }
        }
        pw.print("</form></div></div></div>");
        utility.printHtml("Footer.html");
    }

}


