

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/WriteReview")
public class WriteReview extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        review(request, response);
    }

    protected void review(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {

            response.setContentType("text/html");
            PrintWriter pw = response.getWriter();
            Utilities utility = new Utilities(request, pw);

            if (!utility.isLoggedin()) {
                HttpSession session = request.getSession(true);
                session.setAttribute("login_msg", "Please Login to Write a Review");
                response.sendRedirect("Login");
                return;
            }
            Long productId = Long.parseLong(request.getParameter("productId"));
            Product product = MySqlDataStoreUtilities.getProduct(productId);
            User user = MySqlDataStoreUtilities.getUser(utility.getUserId());

            // on filling the form and clicking submit button user will be directed to submit review page
            utility.printHtml("Header.html");
            utility.printHtml("LeftNavigationBar.html");
            pw.print("<form name ='WriteReview' action='WriteReview' method='post'>");
            pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
            pw.print("<a style='font-size: 24px;'>Review</a>");
            pw.print("</h2><div class='entry'>");

            pw.print("<table class='gridtable'>");

            pw.print("<tr>");
            pw.print("<td> User Id: </td>");
            pw.print("<td> " + user.getUserName() + " </td>");
            pw.print("</tr>");

            pw.print("<tr>");
            pw.print("<td> User Age: </td>");
            pw.print("<td> " + user.getUserAge() + " </td>");
            pw.print("</tr>");

            pw.print("<tr>");
            pw.print("<td> User Gender: </td>");
            pw.print("<td> " + user.getUserGender() + " </td>");
//            pw.print("<input type='radio' id='male' name='userGender' value='male' checked><label for='male'>Male</label><br>" +
//                    "<input type='radio' id='female' name='userGender' value='female'><label for='female'>Female</label>" +
//                    "</td>");
            pw.print("</tr>");

            pw.print("<tr>");
            pw.print("<td> User Occupation: </td>");
            pw.print("<td> " + user.getUserOccupation() + " </td>");
            pw.print("</tr>");

            pw.print("<tr>" +
                    "<td> Product Name: </td>" +
                    "<td> " + product.getProductName() + "<input type='hidden' name='productId' value='" + product.getProductId() + "'></td>" +
                    "</tr>");

            pw.print("<tr>" +
                    "<td> Product Category/Type:</td>" +
                    "<td> " + product.getCategory().toString() + "</td>" +
                    "</tr>");

            pw.print("<tr><td> Product Price:</td>" +
                    "<td> " + product.getPrice() + "</td>" +
                    "</tr>");

            pw.print("<tr><td> Product Manufacturer: </td>" +
                    "<td>" + product.getManufacturer().toString() + "</td>" +
                    "</tr>");


            pw.print("<tr><td> Product On Sale: </td>");
            pw.print("<td>" + (product.getDiscount() > 0 ? "OnSale" : "Not OnSale"));
            pw.print("</td></tr>");

            pw.print("<tr>" +
                    "<td> Review Rating: </td>");

            pw.print("<td>");
            pw.print("<select name='reviewrating'>");
            pw.print("<option value='1' selected>1</option>");
            pw.print("<option value='2'>2</option>");
            pw.print("<option value='3'>3</option>");
            pw.print("<option value='4'>4</option>");
            pw.print("<option value='5'>5</option>");
            pw.print("</td></tr>");

            pw.print("<tr><td>");
            pw.print("Store Pickup Location:</td>");
            pw.print("<td><select name='storeLocationId' id='storeLocationId'>");
            for (StoreLocation storeLocation : MySqlDataStoreUtilities.getStoreLocations()) {
                pw.print("<option value='" + storeLocation.getStoreLocationId() + "'>" + storeLocation.getStreetAddress()
                        + ", " + storeLocation.getZipCode() + ", " +
                        storeLocation.getCity() + ", " + storeLocation.getState() + "</option>");
            }
            pw.print("</select>");
            pw.print("</td></tr>");

            pw.print("<tr>");
            pw.print("<td> Retailer Zip Code: </td>");
            pw.print("<td> <input type='number' name='zipcode' required> </td>");
            pw.print("</tr>");


            pw.print("<tr>");
            pw.print("<td> Retailer City: </td>");
            pw.print("<td> <input type='text' name='retailercity' required> </td>");
            pw.print("</tr>");

            pw.print("<tr>");
            pw.print("<td> Review Date: </td>");
            pw.print("<td> <input type='date' name='reviewdate' required> </td>");
            pw.print("</tr>");

            pw.print("<tr>");
            pw.print("<td> Review Text: </td>");
            pw.print("<td><textarea name='reviewtext' rows='4' cols='50'> </textarea></td></tr>");

            pw.print("<tr><td colspan='2'><input type='submit' class='btnbuy' name='SubmitReview' value='SubmitReview'></td></tr>");


            pw.print("</table>");

            pw.print("</h2></div></div></div>");
            utility.printHtml("Footer.html");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        Utilities utility = new Utilities(request, pw);
        storeReview(request, response);
    }

    protected void storeReview(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            response.setContentType("text/html");
            PrintWriter pw = response.getWriter();
            Utilities utility = new Utilities(request, pw);
            if (!utility.isLoggedin()) {
                HttpSession session = request.getSession(true);
                session.setAttribute("login_msg", "Please Login to add items to cart");
                response.sendRedirect("Login");
                return;
            }

            Long productId = Long.parseLong(request.getParameter("productId"));
            Product product = MySqlDataStoreUtilities.getProduct(productId);
            Long storeLocationId = Long.parseLong(request.getParameter("storeLocationId"));
            StoreLocation storeLocation = MySqlDataStoreUtilities.getStoreLocation(storeLocationId);

            String reviewRating = request.getParameter("reviewrating");
            String reviewDate = request.getParameter("reviewdate");
            String reviewText = request.getParameter("reviewtext");
            String retailerZipCode = request.getParameter("zipcode");
            String retailerCity = request.getParameter("retailercity");


//            String productManufacturer, boolean productIsOnSale, Long userId, String userName, double userAge,
//            String userGender, String userOccupation, Long storeId, Long storeLocationId,
//                    String storeStreetAddress, String storeCity, String storeState, String storeZipCode,
//                    Integer reviewRating, String reviewDate, String reviewText, String retailerZipCode, String retailerCity


            User user = MySqlDataStoreUtilities.getUser(utility.getUserId());
            boolean isStored = utility.storeReview(productId, product.getProductName(), product.getCategory().toString()
                    , product.getPrice(), product.getManufacturer().toString(), product.getDiscount() > 0 ? true : false,
                    utility.getUserId(), user.getUserName(), user.getUserAge(), user.getUserGender(),
                    user.getUserOccupation(), storeLocationId,
                    storeLocation.getStreetAddress(), storeLocation.getCity(), storeLocation.getState(),
                    storeLocation.getZipCode(), Integer.parseInt(reviewRating),
                    reviewDate, reviewText, retailerZipCode, retailerCity);

            utility.printHtml("Header.html");
            utility.printHtml("LeftNavigationBar.html");
            pw.print("<form name ='Cart' action='CheckOut' method='post'>");
            pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
            pw.print("<a style='font-size: 24px;'>Review</a>");
            pw.print("</h2><div class='entry'>");
            if (isStored) {
                pw.print("<h2>Review for &nbsp" + product.getProductName() + " Stored </h2>");
            } else {
                pw.print("<h2>Mongo Db is not up and running </h2>");
            }

            pw.print("</div></div></div>");
            utility.printHtml("Footer.html");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }
}
