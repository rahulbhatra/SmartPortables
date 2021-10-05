import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/ViewCustomerOrder")
public class ViewCustomerOrder extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        displayCustomerOrders(request, response, pw);
    }

    protected void displayCustomerOrders(HttpServletRequest request, HttpServletResponse response, PrintWriter pw) throws ServletException, IOException {
        Utilities utility = new Utilities(request, pw);
        HttpSession session = request.getSession(true);
        Object userType = session.getAttribute("usertype");
        Object customerName = request.getParameter("customerName");

        if (!utility.isLoggedin() && userType != null && userType.toString().equalsIgnoreCase("manager")) {
            session.setAttribute("login_msg", "Please Login as Salesman to View Order of Customers");
            response.sendRedirect("ViewCustomerOrder");
            return;
        }

        utility.printHtml("Header.html");
        pw.print("<div class='post' style='float: none; width: 100%'>");
        pw.print("<h2 class='title meta'><a style='font-size: 24px;'>View Customer Order</a></h2>"
                + "<div class='entry'>"
                + "<div style='width:100%; margin:25px; margin-left: auto;margin-right: auto;'>");


        Map<String, User> userHashMap = utility.getUsers();

        pw.print("<form action='ViewCustomerOrder' method='get'>");

        pw.print("<table style='width: 100%'>");

        pw.print("<tr class='createContentRow'>");
        pw.print("<td class='loginAsCustomerLabel'>");
        pw.print("Select UserName:");
        pw.print("</td>");

        pw.print("<td class='createContentInput'>");

        pw.print("<select style='radius: 20px; width: 100%;' name='customerName' id='customerName'>");
        for (User user : userHashMap.values()) {
            if (customerName != null && user.getUserName() != null && user.getUserName().equalsIgnoreCase(customerName.toString())) {
                pw.print("<option value='" + user.getUserName() + "' selected>" + user.getUserName() + "</option>");
            } else {
                pw.print("<option value='" + user.getUserName() + "'>" + user.getUserName() + "</option>");
            }
        }
        pw.print("</td>");
        pw.print("</tr>");
        pw.print("</table>");

        pw.print("<div class='buttonContainer'><button class='loginAsCustomerButton' type='submit' value='Submit'>Search Orders</button></div>");
        pw.print("</form>");

        int size = utility.getOrderPaymentSize();

        if (size > 0) {
            pw.print("<table style='width:100%'");
            pw.print("<tr>" +
                    "<th> Order Id </th>" +
                    "<th> User Name </th>" +
                    "<th> Product Name </th>" +
                    "<th> Price </th>" +
                    "<th> Warranty Price </th>" +
                    "<th> Operations </th>" +
                    "</tr>"
            );

            List<CustomerOrder> orderPayments = new ArrayList<>();
            if (customerName != null) {
                orderPayments = MySqlDataStoreUtilities.getCustomerOrdersByUserId(utility.getUserId());
            }

            for (CustomerOrder customerOrder : orderPayments) {
                System.out.println("Order username:" + utility.username() + " customer name" + customerName);

                pw.print("<tr>");
                pw.print("<td>" + customerOrder.getCustomerOrderId() + "</td>" +
                        "<td>" + utility.username() + "</td>" +
                        "<td>" + customerOrder.getOrderName() + "</td>" +
                        "<td>" + customerOrder.getOrderPrice() + "</td>" +
                        "<td>" + customerOrder.getWarrantyPrice() + "</td>"
                );


                pw.print("<td style='display:inline-flex;'><form style='margin:auto' action='EditCustomerOrder' method='get'>" +
                        "<input type='hidden' name='orderId' value='" + customerOrder.getCustomerOrderId() + "'>" +
                        "<input type='hidden' name='customerName' value='" + customerName.toString() + "'>" +
                        "<input type='hidden' name='orderName' value='" + customerOrder.getOrderName() + "'>" +
                        "<button class='mainButton' type='submit' value='submit'>Edit</button>" +
                        "</form>");

                pw.print("<form style='margin:auto' action='RemoveCustomerOrder' method='post'>" +
                        "<input type='hidden' name='orderId' value='" + customerOrder.getCustomerOrderId() + "'>" +
                        "<input type='hidden' name='customerName' value='" + customerName.toString() + "'>" +
                        "<input type='hidden' name='orderName' value='" + customerOrder.getOrderName() + "'>" +
                        "<button class='mainButton' type='submit' value='submit'>Remove</button>" +
                        "</form>" +
                        "</td>");

                pw.print("</tr>");

            }


            pw.print("</table>");
        }
    }

}
