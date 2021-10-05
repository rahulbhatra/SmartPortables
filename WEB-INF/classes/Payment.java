import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
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

        // get the payment details like credit card no address processed from cart servlet

        String userName = utilities.username();

        if (request.getParameter("customerName") != null) {
            userName = request.getParameter("customerName");
        }

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

        for (OrderItem orderItem : utilities.getCustomerOrders()) {
            System.out.println("----------- userName --------------" + userName);

            Transaction transaction = new Transaction(
                    null,
                    utilities.getUserId(),
                    storeLocationId,
                    firstName,
                    lastName,
                    address1,
                    address2,
                    city,
                    state,
                    zipCode,
                    phone,
                    order,
                    new Date(),
                    new Date(),
                    10.0,
                    Double.parseDouble(orderTotal),
                    creditCardNo
            );
            transaction = MySqlDataStoreUtilities.addTransaction(transaction);

            System.out.println("------------ Transaction Id ---------" + transaction.toString());
            CustomerOrder customerOrder = new CustomerOrder(null,
                    transaction.getTransactionId(),
                    utilities.getUserId(),
                    orderItem.getName(),
                    orderItem.getPrice(),
                    (orderItem.isWarrantyIncluded() ? 25.0 : 0.0));
            utilities.storePayment(customerOrder);
        }

        //remove the order details from cart after processing

        OrdersHashMap.orders.remove(utilities.username());
        utilities.printHtml("Header.html");
        utilities.printHtml("LeftNavigationBar.html");
        pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
        pw.print("<a style='font-size: 24px;'>Order</a>");
        pw.print("</h2><div class='entry'>");

        pw.print("<h2>Your Order");
        pw.print("&nbsp&nbsp");
        pw.print("is stored ");
        pw.print("<br>Your Order No is " + ( 1));

        LocalDate todayDate = LocalDate.now();

        pw.print("<br> PickUP / Delivery Date:" + todayDate.plusDays(15));
        pw.print("<br> You will get rebate amount of $" + orderTotalRebate + " in your credit card" + creditCardNo + " after 15 days");
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        Utilities utility = new Utilities(request, pw);


    }
}
