import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@WebServlet("/Account")

public class Account extends HttpServlet {
    private String error_msg;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        displayAccount(request, response);
    }

    /* Display Account Details of the Customer (Name and Usertype) */

    protected void displayAccount(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        Utilities utility = new Utilities(request, pw);
        try {
            response.setContentType("text/html");
            if (!utility.isLoggedin()) {
                HttpSession session = request.getSession(true);
                session.setAttribute("login_msg", "Please Login to add items to cart");
                response.sendRedirect("Login");
                return;
            }
            HttpSession session = request.getSession();
            utility.printHtml("Header.html");
            utility.printHtml("LeftNavigationBar.html");
            pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
            pw.print("<a style='font-size: 24px;'>Account</a>");
            pw.print("</h2><div class='entry'>");
//            pw.print("<table class='gridtable'>");
//            pw.print("<tr>");
//            pw.print("<td> User Name: </td>");
//            pw.print("<td>" + user.getUserName() + "</td>");
//            pw.print("</tr>");
//            pw.print("<tr>");
//            pw.print("<td> User Type: </td>");
//            pw.print("<td>" + user.getUserType() + "</td>");
//            pw.print("</tr>");
//            pw.print("</table>");

            List<CustomerOrder> customerOrders = MySqlDataStoreUtilities.getCustomerOrdersByUserId(utility.getUserId());
            if (customerOrders.size() > 0) {
                pw.print("<form name ='ViewOrder' action='ViewOrder' method='get'>");

                pw.print("<table  class='gridtable'>");
                pw.print("<tr><td></td>");
                pw.print("<td>OrderId</td>");
                pw.print("<td>User Name</td>");
                pw.print("<td>Product Ordered</td>");
                pw.print("<td>Product Price</td>" +
                        "<td>Operation</td>" +
                        "</tr>");
                for (CustomerOrder customerOrder : customerOrders) {
                    User customer = MySqlDataStoreUtilities.getUser(customerOrder.getUserId());
                    pw.print("<tr>");
                    pw.print("<td><input type='radio' name='orderName' value='" + customerOrder.getOrderName() + "'></td>");
                    pw.print("<td>" + customerOrder.getCustomerOrderId() + "</td>" +
                            "<td>" + customer.getUserName() + "</td>" +
                            "<td>" + customerOrder.getOrderName() + "</td>" +
                            "<td>" + customerOrder.getOrderPrice() + "</td>");
                    pw.print("<td><input type='submit' name='Order' value='CancelOrder' class='btnbuy'></td>");
                    pw.print("</tr>");
                    pw.print("<input type='hidden' name='orderId' value='" + customerOrder.getCustomerOrderId() + "'>");
                }
                pw.print("</table>");
                pw.print("</form>");
            } else {
                pw.print("<h4 style='color:red'>You have not placed any order with this order id</h4>");
            }
            pw.print("</table>");
            pw.print("</h2></div></div></div>");
            utility.printHtml("Footer.html");
        } catch (Exception e) {
        }
    }
}
