import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/EditCustomerOrder")
public class EditCustomerOrder extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        editCustomerOrder(request, response);

    }

    protected void editCustomerOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try
        {
            response.setContentType("text/html");
            PrintWriter pw = response.getWriter();
            Utilities utility = new Utilities(request,pw);
            if(!utility.isLoggedin() && ! UserType.SalesMan.equals(utility.usertype()))
            {
                HttpSession session = request.getSession(true);
                session.setAttribute("login_msg", "Please Login as Salesman to EditCustomerOrder");
                response.sendRedirect("Login");
                return;
            }


            //get the order product details	on clicking submit the form will be passed to submitorder page

            Long customerOrderId = Long.parseLong(request.getParameter("customerOrderId"));
            int orderPaymentIndex = Integer.parseInt(request.getParameter("orderPaymentIndex"));
            String userName = request.getParameter("customerName");
            String orderName = request.getParameter("orderName");

            System.out.println( "---- Inside Edit Customer ------------");
            CustomerOrder customerOrder = utility.getCustomerOrder(customerOrderId);
            Transaction transaction = MySqlDataStoreUtilities.getTransaction(customerOrder.getTransactionId());
            System.out.println( "---- See Order Payment ------------" + customerOrder.toString());

            utility.printHtml("Header.html");
            utility.printHtml("LeftNavigationBar.html");
            pw.print("<form name ='EditCustomerOrder' action='EditCustomerOrder' method='post'>");
            pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
            pw.print("<a style='font-size: 24px;'>EditCustomerOrder</a>");
            pw.print("</h2><div class='entry'>");
            pw.print("<table  class='gridtable'>" + "<tr><td>Customer Name:</td><td>");
            pw.print(userName);
            pw.print("</td></tr>");

            pw.print("<tr><td>");
            pw.print("First Name:");
            pw.print("</td>");
            pw.print("<td>");
            pw.print("<input type='text' name='firstName' value='" + transaction.getFirstName() + "' />");
            pw.print("<input type='hidden' name='customerOrderId' value='" + customerOrderId + "'>" +
                    "<input type='hidden' name='orderPaymentIndex' value='" + orderPaymentIndex + "'>" +
                    "<input type='hidden' name='orderName' value='" + orderName + "'>" +
                    "<input type='hidden' name='customerName' value='" + userName + "'>");
            pw.print("</td></tr>");

            pw.print("<tr><td>");
            pw.print("Last Name:");
            pw.print("</td>");
            pw.print("<td>");
            pw.print("<input type='text' name='lastName' value='" + transaction.getLastName() + "'  />");
            pw.print("</td></tr>");

            pw.print("<tr><td>");
            pw.print("Address 1:");
            pw.print("</td>");
            pw.print("<td>");
            pw.print("<input type='text' name='address1' value='" + transaction.getAddress1() + "' />");
            pw.print("</td></tr>");

            pw.print("<tr><td>");
            pw.print("Address 2:");
            pw.print("</td>");
            pw.print("<td>");
            pw.print("<input type='text' name='address2' value='" + transaction.getAddress2() + "' />");
            pw.print("</td></tr>");

            pw.print("<tr><td>");
            pw.print("City:");
            pw.print("</td>");
            pw.print("<td>");
            pw.print("<input type='text' name='city' value='" + transaction.getCity() + "' />");
            pw.print("</td></tr>");

            pw.print("<tr><td>");
            pw.print("State:");
            pw.print("</td>");
            pw.print("<td>");
            pw.print("<input type='text' name='state' value='" + transaction.getState() + "' />");
            pw.print("</td></tr>");

            pw.print("<tr><td>");
            pw.print("Zip Code:");
            pw.print("</td>");
            pw.print("<td>");
            pw.print("<input type='text' name='zipCode' value='" + transaction.getZipcode() + "' />");
            pw.print("</td></tr>");

            pw.print("<tr><td>");
            pw.print("Phone:");
            pw.print("</td>");
            pw.print("<td>");
            pw.print("<input type='text' name='phone' value='" + transaction.getPhone() + "' />");
            pw.print("</td></tr>");

            pw.print("<tr><td>");
            pw.print("Delivery Option:</td>");
            if("homeDelivery".equalsIgnoreCase(transaction.getDeliveryOption())) {
                pw.print("<td><input type='radio' id='homeDelivery' name='order' value='homeDelivery' checked><label for='homeDelivery'>Delivery</label></td>");
                pw.print("<td><input type='radio' id='storePickup' name='order' value='storePickup'><label for='storePickup'>Pickup</label>");
            } else {
                pw.print("<td><input type='radio' id='homeDelivery' name='order' value='homeDelivery'><label for='homeDelivery'>Delivery</label></td>");
                pw.print("<td><input type='radio' id='storePickup' name='order' value='storePickup' checked><label for='storePickup'>Pickup</label>");
            }

            pw.print("</td></tr>");

            pw.print("<tr><td>");
            pw.print("In Store Pickup Location:</td>");
            pw.print("<td><select name='storePickupLocation' id='storePickupLocation'>");


            for(StoreLocation storeLocation: MySqlDataStoreUtilities.getStoreLocations()) {
                if(storeLocation.getStoreLocationId().equals(transaction.getStoreLocationId())) {
                    pw.print("<option value='" + storeLocation.getStoreLocationId() + "' selected>" + storeLocation.getStreetAddress() + " " + storeLocation.getZipCode() + "</option>");
                } else {
                    pw.print("<option value='" + storeLocation.getStoreLocationId() + "'>" + storeLocation.getStreetAddress() + " " + storeLocation.getZipCode() + "</option>");
                }
            }

            pw.print("</select>");
            pw.print("</td></tr>");

            pw.print("<tr><td>");
            pw.print("Credit/accountNo</td>");
            pw.print("<td><input type='text' name='creditCardNo' value='" + transaction.getCreditCardNo() + "'>");
            pw.print("</td></tr>");

            pw.print("<tr><td colspan='2'>");
            pw.print("<input type='submit' name='submit' class='btnbuy'>");
            pw.print("</td></tr></table></form>");
            pw.print("</div></div></div>");
            utility.printHtml("Footer.html");
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        HttpSession session = request.getSession(true);


        Utilities utilities = new Utilities(request, pw);
        if(!utilities.isLoggedin() && ! UserType.SalesMan.equals(utilities.usertype()))
        {
            session.setAttribute("login_msg", "Please Login as Salesman to EditCustomerOrder");
            response.sendRedirect("Login");
            return;
        }
        // get the payment details like credit card no address processed from cart servlet
        Long customerOrderId = Long.parseLong(request.getParameter("customerOrderId"));
        int orderPaymentIndex = Integer.parseInt(request.getParameter("orderPaymentIndex"));

        String userName = request.getParameter("customerName");
        String orderName = request.getParameter("orderName");

        String customerName = request.getParameter("customerName");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String address1 = request.getParameter("address1");
        String address2 = request.getParameter("address2");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String zipCode = request.getParameter("zipCode");
        String phone = request.getParameter("phone");
        String order = request.getParameter("order");
        String storePickupLocation = request.getParameter("storePickupLocation");


        String creditCardNo=request.getParameter("creditCardNo");
        String userAddress = address1 + ", " + address2 + ", " + city + ", " + state + ", " + zipCode;
        System.out.print("the user address is" + userAddress);
        System.out.print(creditCardNo);

        validation(firstName, lastName, address1, city, state, zipCode,
                phone, storePickupLocation, order, creditCardNo, utilities, pw);

        CustomerOrder customerOrder = utilities.getCustomerOrder(customerOrderId);
        Transaction transaction = MySqlDataStoreUtilities.getTransaction(customerOrder.getTransactionId());
        utilities.updatePayment(customerOrder);

        response.sendRedirect("ViewCustomerOrder?customerName=" +  customerName);

    }

    private void validation(String firstName, String lastName, String address1,
                            String city, String state, String zipCode, String phone, String storePickupLocation,
                            String order, String creditCardNo, Utilities utilities, PrintWriter pw) {
        if(firstName.isEmpty()) {
            sendError(utilities, pw, "first name");
            return;
        }
        if(lastName.isEmpty()) {
            sendError(utilities, pw, "last name");
            return;
        }
        if(address1.isEmpty()) {
            sendError(utilities, pw, "address1");
            return;
        }
        if(city.isEmpty()) {
            sendError(utilities, pw, "city");
            return;
        }

        if(state.isEmpty()) {
            sendError(utilities, pw, "state");
            return;
        }

        if(zipCode.isEmpty()) {
            sendError(utilities, pw, "zip code");
            return;
        }

        if(phone.isEmpty()) {
            sendError(utilities, pw, "phone");
            return;
        }

        if(order.equalsIgnoreCase("storePickup") && storePickupLocation.equalsIgnoreCase("None")) {
            sendError(utilities, pw, "store pick up location when store pick up is chosed");
            return;
        }

        if(creditCardNo.isEmpty()) {
            sendError(utilities, pw, "Credit Card Number");
            return;
        }
    }

    private void sendError(Utilities utilities, PrintWriter pw, String errorName) {
        utilities.printHtml("Header.html");
        utilities.printHtml("LeftNavigationBar.html");
        pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
        pw.print("<a style='font-size: 24px;'>Order</a>");
        pw.print("</h2><div class='entry'>");

        pw.print("<h4 style='color:red'>Please enter " + errorName + "</h4>");
        pw.print("</h2></div></div></div>");
        utilities.printHtml("Footer.html");
    }
}
