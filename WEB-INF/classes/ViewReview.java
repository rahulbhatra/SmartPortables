

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@WebServlet("/ViewReview")

public class ViewReview extends HttpServlet {

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
                session.setAttribute("login_msg", "Please Login to view Review");
                response.sendRedirect("Login");
                return;
            }
            Long productId = Long.parseLong(request.getParameter("productId"));
            Map<Long, ArrayList<Review>> hm = MongoDBDataStoreUtilities.selectReview();

            utility.printHtml("Header.html");
            utility.printHtml("LeftNavigationBar.html");

            pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
            pw.print("<a style='font-size: 24px;'>Review</a>");
            pw.print("</h2><div class='entry'>");

            //if there are no reviews for product print no review else iterate over all the reviews using cursor and print the reviews in a table
            if (hm == null) {
                pw.println("<h2>Mongo Db server is not up and running</h2>");
            } else {
                if (!hm.containsKey(productId)) {
                    pw.println("<h2>There are no reviews for this product.</h2>");
                } else {
                    for (Review review : hm.get(productId)) {
                        pw.print("<table style='width:100%; margin:20px auto' class='gridtable'>");

                        pw.print("<tr class='rowTable'>");
                        pw.print("<td class='leftDataTable'> userId: </td>");
                        pw.print("<td class='rightDataTable'>" + review.getUserName() + "</td>");
                        pw.print("</tr>");

                        pw.print("<tr class='rowTable'>");
                        pw.print("<td class='leftDataTable'> User Age: </td>");
                        pw.print("<td class='rightDataTable'>" + review.getUserAge() + "</td>");
                        pw.print("</tr>");

                        pw.print("<tr class='rowTable'>");
                        pw.print("<td class='leftDataTable'> User Gender: </td>");
                        pw.print("<td class='rightDataTable'>" + review.getUserGender() + "</td>");
                        pw.print("</tr>");

                        pw.print("<tr class='rowTable'>");
                        pw.print("<td class='leftDataTable'> User Occupation: </td>");
                        pw.print("<td class='rightDataTable'>" + review.getUserOccupation() + "</td>");
                        pw.print("</tr>");

                        pw.print("<tr class='rowTable'>");
                        pw.print("<td class='leftDataTable'> Product Name: </td>");
                        pw.print("<td class='rightDataTable'>" + review.getProductName() + "</td>");
                        pw.print("</tr>");

                        pw.print("<tr class='rowTable'>");
                        pw.print("<td class='leftDataTable'> Product Category/Type: </td>");
                        pw.print("<td class='rightDataTable'>" + review.getProductCategory() + "</td>");
                        pw.print("</tr>");

                        pw.print("<tr class='rowTable'>");
                        pw.print("<td class='leftDataTable'> Product Price: </td>");
                        pw.print("<td class='rightDataTable'>" + review.getProductPrice() + "</td>");
                        pw.print("</tr>");

                        pw.print("<tr class='rowTable'>");
                        pw.print("<td class='leftDataTable'> Product Manufacturer: </td>");
                        pw.print("<td class='rightDataTable'>" + review.getProductManufacturer() + "</td>");
                        pw.print("</tr>");

                        pw.print("<tr class='rowTable'>");
                        pw.print("<td class='leftDataTable'> Product On Sale: </td>");
                        pw.print("<td class='rightDataTable'>" + (review.isProductIsOnSale() ? "OnSale" : "Not OnSale") + "</td>");
                        pw.print("</tr>");


                        pw.println("<tr class='rowTable'>");
                        pw.println("<td class='leftDataTable'> Store Location ID: </td>");
                        pw.print("<td class='rightDataTable'>" + review.getStoreStreetAddress() + "</td>");
                        pw.print("</tr>");

                        pw.println("<tr class='rowTable'>");
                        pw.println("<td class='leftDataTable'> Store Location City: </td>");
                        pw.print("<td class='rightDataTable'>" + review.getStoreCity() + "</td>");
                        pw.print("</tr>");

                        pw.println("<tr class='rowTable'>");
                        pw.println("<td class='leftDataTable'> Store Location State: </td>");
                        pw.print("<td class='rightDataTable'>" + review.getStoreState() + "</td>");
                        pw.print("</tr>");

                        pw.println("<tr class='rowTable'>");
                        pw.println("<td class='leftDataTable'> Store Location Zip Code: </td>");
                        pw.print("<td class='rightDataTable'>" + review.getStoreZipCode() + "</td>");
                        pw.print("</tr>");

                        pw.println("<tr class='rowTable'>");
                        pw.println("<td class='leftDataTable'> Review Rating: </td>");
                        pw.print("<td class='rightDataTable'>" + review.getReviewRating() + "</td>");
                        pw.print("</tr>");

                        pw.print("<tr class='rowTable'>");
                        pw.print("<td class='leftDataTable'> Retailer City: </td>");
                        pw.print("<td class='rightDataTable'>" + review.getRetailerCity() + "</td>");
                        pw.print("</tr>");

                        pw.print("<tr class='rowTable'>");
                        pw.print("<td class='leftDataTable'> Review Date: </td>");
                        pw.print("<td class='rightDataTable'>" + review.getReviewDate() + "</td>");
                        pw.print("</tr>");

                        pw.print("<tr class='rowTable'>");
                        pw.print("<td class='leftDataTable'> Review Text: </td>");
                        pw.print("<td class='rightDataTable'>" + review.getReviewText() + "</td>");
                        pw.print("</tr>");

                        pw.println("</table>");
                    }

                }
            }
            pw.print("</div></div></div>");
            utility.printHtml("Footer.html");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
