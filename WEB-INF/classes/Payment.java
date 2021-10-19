import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

@WebServlet("/Payment")

public class Payment extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        HttpSession session = request.getSession(true);

        Utilities utilities = new Utilities(request, pw);
        if (!utilities.isLoggedin()) {
            session.setAttribute("login_msg", "Please Login to Pay");
            response.sendRedirect("Login");
            return;
        }

        String orderTotal = request.getParameter("orderTotal");
        String orderTotalRebate = request.getParameter("orderTotalRebate");


        Long userId = Utilities.isNullOrEmptyOrWhiteSpaceOnly(request.getParameter("userId")) ?
                utilities.getUserId() : Long.parseLong(request.getParameter("userId"));


        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String address1 = request.getParameter("address1");
        String address2 = request.getParameter("address2");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String zipCode = request.getParameter("zipCode");
        String phone = request.getParameter("phone");
        String order = request.getParameter("order");
        Long storeLocationId = Long.parseLong(request.getParameter("storePickupLocation"));


        String creditCardNo = request.getParameter("creditCardNo");
        String userAddress = address1 + ", " + address2 + ", " + city + ", " + state + ", " + zipCode;
        System.out.print("the user address is" + userAddress);
        System.out.print(creditCardNo);

        validation(firstName, lastName, address1, city, state, zipCode,
                phone, storeLocationId, order, creditCardNo, utilities, pw);

        Transaction transaction = new Transaction(
                null, userId, storeLocationId, firstName, lastName,
                address1, address2, city, state, zipCode, phone, order, new Date(), Utilities.addDays(new Date(), 15),
                10.0, Double.parseDouble(orderTotal), creditCardNo, utilities.getCustomerOrders().size());

        transaction = MySqlDataStoreUtilities.addTransaction(transaction);

        Customer customer = MySqlDataStoreUtilities.getCustomerByUserId(userId);
        if(customer == null) {
            customer = new Customer(null, userId, firstName, lastName,
                    address1, address2, city, state, zipCode, phone);
            customer = MySqlDataStoreUtilities.insertCustomer(customer);
        } else {
            customer = new Customer(null, userId, firstName, lastName,
                    address1, address2, city, state, zipCode, phone);
            MySqlDataStoreUtilities.updateCustomer(customer);
        }



        for (OrderItem orderItem : utilities.getCustomerOrders()) {
            System.out.println("------------ Transaction Id ---------" + transaction.toString());
            CustomerOrder customerOrder = new CustomerOrder(null,
                    transaction.getTransactionId(),
                    orderItem.getProductId(),
                    userId,
                    orderItem.getName(),
                    orderItem.getPrice(),
                    (orderItem.isWarrantyIncluded() ? 25.0 : 0.0));
            utilities.storePayment(customerOrder);
        }

        OrdersHashMap.orders.remove(utilities.username());

        utilities.printHtml("Header.html");
        utilities.printHtml("LeftNavigationBar.html");


        pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
        pw.print("<a style='font-size: 24px;'>Order</a>");
        pw.print("</h2><div class='entry'>");


        User user = MySqlDataStoreUtilities.getUser(userId);
        pw.print("<h2>Your Order for UserName " + user.getUserName() + " is stored ");
        pw.print("<br>Your Transaction No / Order No is " + transaction.getTransactionId());
        pw.print("<br> PickUP / Delivery Date:" + transaction.getShipDate().toString());
        pw.print("<br> Rebate amount of $" + orderTotalRebate + " will transfer in your credit card" + creditCardNo + " after 15 days");
        pw.print("</h2></div></div></div>");
        utilities.printHtml("Footer.html");

    }

    private void validation(String firstName, String lastName, String address1,
                            String city, String state, String zipCode, String phone, Long storePickupLocation,
                            String order, String creditCardNo, Utilities utilities, PrintWriter pw) {
        if (firstName.isEmpty()) {
            sendError(utilities, pw, "first name");
            return;
        }
        if (lastName.isEmpty()) {
            sendError(utilities, pw, "last name");
            return;
        }
        if (address1.isEmpty()) {
            sendError(utilities, pw, "address1");
            return;
        }
        if (city.isEmpty()) {
            sendError(utilities, pw, "city");
            return;
        }

        if (state.isEmpty()) {
            sendError(utilities, pw, "state");
            return;
        }

        if (zipCode.isEmpty()) {
            sendError(utilities, pw, "zip code");
            return;
        }

        if (phone.isEmpty()) {
            sendError(utilities, pw, "phone");
            return;
        }

        if (order.equalsIgnoreCase("storePickup") && storePickupLocation == null) {
            sendError(utilities, pw, "store pick up location when store pick up is chosed");
            return;
        }

        if (creditCardNo.isEmpty()) {
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
