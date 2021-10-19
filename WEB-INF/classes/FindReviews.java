import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mongodb.*;

import javax.servlet.http.HttpSession;

@WebServlet("/FindReviews")

public class FindReviews extends HttpServlet {
    static DBCollection myReviews;

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        Utilities utility = new Utilities(request, pw);


        //check if the user is logged in
        if (!utility.isLoggedin()) {
            HttpSession session = request.getSession(true);
            session.setAttribute("login_msg", "Please Login to View your Orders");
            response.sendRedirect("Login");
            return;
        }

        Long productId = Long.parseLong(request.getParameter("productId"));
        int productPrice = Integer.parseInt(request.getParameter("productPrice"));
        int reviewRating = Integer.parseInt(request.getParameter("reviewRating"));
        String compareRating = request.getParameter("compareRating");
        String comparePrice = request.getParameter("comparePrice");
        String retailerCity = request.getParameter("retailerCity");
        String retailerZipcode = request.getParameter("retailerZipcode");

        String[] filters = request.getParameterValues("queryCheckBox");
        String[] extraSettings = request.getParameterValues("extraSettings");
        String dataGroupBy = request.getParameter("dataGroupBy");


        boolean noFilter = false;
        boolean filterByProduct = false;
        boolean filterByPrice = false;
        boolean filterByRating = false;
        myReviews = MongoDBDataStoreUtilities.getConnection();
        BasicDBObject query = new BasicDBObject();
        boolean groupBy = false;
        boolean filterByCity = false;
        boolean groupByCity = false;
        boolean groupByProduct = false;
        boolean filterByZip = false;

        boolean countOnly = false;
        String groupByDropdown = request.getParameter("groupByDropdown");
        DBCursor dbCursor = null;
        DBObject match = null;
        DBObject groupFields = null;
        DBObject group = null;
        DBObject projectFields = null;
        DBObject project = null;
        AggregationOutput aggregate = null;
        String groupfield = null;

        //Check for extra settings(Grouping Settings)
        if (extraSettings != null) {
            //User has selected extra settings
            groupBy = true;

            for (int x = 0; x < extraSettings.length; x++) {
                switch (extraSettings[x]) {
                    case "COUNT_ONLY":
                        //Not implemented functionality to return count only
                        countOnly = true;
                        break;
                    case "GROUP_BY":
                        //Can add more grouping conditions here
                        if (groupByDropdown.equals("GROUP_BY_CITY")) {
                            groupByCity = true;
                        } else if (groupByDropdown.equals("GROUP_BY_PRODUCT")) {
                            groupByProduct = true;
                        }
                        break;
                }
            }
        }


        if (filters != null && groupBy != true) {
            for (int i = 0; i < filters.length; i++) {
                //Check what all filters are ON
                //Build the query accordingly
                switch (filters[i]) {
                    case "productId":
                        filterByProduct = true;
                        if (productId != new Long(0)) {
                            query.put("productId", productId);
                        }
                        break;

                    case "productPrice":
                        filterByPrice = true;
                        if (comparePrice.equals("EQUALS_TO")) {
                            query.put("productPrice", productPrice);
                        } else if (comparePrice.equals("GREATER_THAN")) {
                            query.put("productPrice", new BasicDBObject("$gt", productPrice));
                        } else if (comparePrice.equals("LESS_THAN")) {
                            query.put("productPrice", new BasicDBObject("$lt", productPrice));
                        }

                        break;

                    case "retailerZipcode":
                        filterByZip = true;
                        System.out.println("inside if");
                        query.put("retailerZipCode", retailerZipcode);
                        break;

                    case "retailerCity":
                        filterByCity = true;
                        if (!retailerCity.equals("All") && !groupByCity) {
                            query.put("retailerCity", retailerCity);
                        }
                        break;

                    case "reviewRating":
                        filterByRating = true;
                        if (compareRating.equals("EQUALS_TO")) {
                            query.put("reviewRating", reviewRating);
                        } else {
                            query.put("reviewRating", new BasicDBObject("$gt", reviewRating));
                        }
                        System.out.println("------ Inside Review Rating -------" + reviewRating);
                        break;

                    default:
                        //Show all the reviews if nothing is selected
                        noFilter = true;
                        break;
                }
            }
        } else {
            //Show all the reviews if nothing is selected
            noFilter = true;
        }


        //Run the query
        if (groupBy == true) {
            //Run the query using aggregate function

            if (groupByCity) {
                groupfield = "RetailerCity";
                groupFields = new BasicDBObject("_id", 0);
                groupFields.put("_id", "$retailerCity");
                groupFields.put("count", new BasicDBObject("$sum", 1));
                groupFields.put("productId", new BasicDBObject("$push", "$productId"));
                groupFields.put("review", new BasicDBObject("$push", "$reviewText"));
                groupFields.put("rating", new BasicDBObject("$push", "$reviewRating"));
                groupFields.put("productPrice", new BasicDBObject("$push", "$productPrice"));
                groupFields.put("retailerCity", new BasicDBObject("$push", "$retailerCity"));
                groupFields.put("retailerZipCode", new BasicDBObject("$push", "$retailerZipCode"));

                group = new BasicDBObject("$group", groupFields);

                projectFields = new BasicDBObject("_id", 0);
                projectFields.put("value", "$_id");
                projectFields.put("ReviewValue", "$count");
                projectFields.put("Product", "$productId");
                projectFields.put("User", "$userName");
                projectFields.put("Reviews", "$review");
                projectFields.put("Rating", "$rating");
                projectFields.put("productPrice", "$productPrice");
                projectFields.put("retailerCity", "$retailerCity");
                projectFields.put("zipCode", "$retailerZipCode");

                project = new BasicDBObject("$project", projectFields);

                aggregate = myReviews.aggregate(group, project);

                //Construct the page content

            } else if (groupByProduct) {

                groupfield = "productId";
                groupFields = new BasicDBObject("_id", 0);
                groupFields.put("_id", "$productId");
                groupFields.put("count", new BasicDBObject("$sum", 1));
                groupFields.put("review", new BasicDBObject("$push", "$reviewText"));
                groupFields.put("rating", new BasicDBObject("$push", "$reviewRating"));
                groupFields.put("productId", new BasicDBObject("$push", "$productId"));
                groupFields.put("productPrice", new BasicDBObject("$push", "$productPrice"));
                groupFields.put("retailerCity", new BasicDBObject("$push", "$retailerCity"));
                groupFields.put("zipCode", new BasicDBObject("$push", "$retailerZipCode"));

                group = new BasicDBObject("$group", groupFields);

                projectFields = new BasicDBObject("_id", 0);
                projectFields.put("value", "$_id");
                projectFields.put("ReviewValue", "$count");
                projectFields.put("Product", "$productId");
                projectFields.put("Reviews", "$review");
                projectFields.put("Rating", "$rating");
                projectFields.put("productPrice", "$productPrice");
                projectFields.put("retailerCity", "$retailerCity");
                projectFields.put("zipCode", "$zipCode");

                project = new BasicDBObject("$project", projectFields);

                aggregate = myReviews.aggregate(group, project);

                //Construct the page content

            }
        } else {
            System.out.println("-------------- Query of Mongo DB ----------" + query.toJson().toString());
            dbCursor = myReviews.find(query);
            System.out.println("----------------- Output of String " + dbCursor.toString());
        }

        utility.printHtml("Header.html");
        utility.printHtml("LeftNavigationBar.html");
        pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
        pw.print("<a style='font-size: 24px;'>Data Analytics on Review</a>");
        pw.print("</h2><div class='entry'>");
        if (groupBy == true) {
            pw.print("<table class='gridtable'>");
            constructGroupByContent(aggregate, pw, dataGroupBy);
            pw.print("</table>");
        } else if (groupBy != true) {
            constructTableContent(dbCursor, pw);
        }
        pw.print("</div></div></div>");
        utility.printHtml("Footer.html");
    }

    public void constructGroupByContent(AggregationOutput aggregate, PrintWriter pw, String dataGroupBy) {
        String tableData = "";
        int count = 0;
        if (dataGroupBy.equals("Count")) {

            pw.print("<tr><td>Name</td><td>Count</td></tr>");

            for (DBObject result : aggregate.results()) {
                BasicDBObject bobj = (BasicDBObject) result;
                tableData = "<tr><td> " + bobj.getString("value") + "</td>&nbsp"
                        + "<td>" + bobj.getString("ReviewValue") + "</td></tr>";
                pw.print(tableData);
                count++;
            }
        }

        if (dataGroupBy.equals("Detail")) {

            int detailcount = 0;
            for (DBObject result : aggregate.results()) {
                BasicDBObject bobj = (BasicDBObject) result;
                BasicDBList productList = (BasicDBList) bobj.get("Product");

                BasicDBList productReview = (BasicDBList) bobj.get("Reviews");
                BasicDBList rating = (BasicDBList) bobj.get("Rating");
                BasicDBList retailerCity = (BasicDBList) bobj.get("retailerCity");
                BasicDBList zipcode = (BasicDBList) bobj.get("zipCode");
                BasicDBList productPrice = (BasicDBList) bobj.get("productPrice");

                pw.print("<tr><td>" + bobj.getString("value") + "</td></tr>");

                while (detailcount < productList.size()) {
                    System.out.println("inside 1 inside 2" + productList.get(detailcount) + rating.get(detailcount));
                    tableData = "<tr rowspan = \"3\"><td> Product: " + productList.get(detailcount) + "</br>"
                            + "Rating: " + rating.get(detailcount) + "</br>"
                            + "Price: " + productPrice.get(detailcount) + "</br>"
                            + "retailerCity: " + retailerCity.get(detailcount) + "</br>"
                            + "retailerZipCode: " + zipcode.get(detailcount) + "</br>"
                            + "Review: " + productReview.get(detailcount) + "</td></tr>";

                    pw.print(tableData);

                    detailcount++;

                }
                detailcount = 0;
                count++;
            }
        }
        if (count == 0) {
            tableData = "<h2>No Data Found</h2>";
            pw.print(tableData);
        }

    }

    public void constructTableContent(DBCursor dbCursor, PrintWriter pw) {
        String tableData = "";
        pw.print("<table class='gridtable'>");

        while (dbCursor.hasNext()) {
            BasicDBObject bobj = (BasicDBObject) dbCursor.next();
            Product product = MySqlDataStoreUtilities.getProduct(Long.parseLong(bobj.getString("productId")));

            tableData = "<tr><td align='center' colspan='2'>Review</td></tr><tr><td>Name: </td>" +
					"<td>" +  product.getProductName() + "</td></tr>"

					+ "<tr><td>Rating:</td>" +
					"<td>" + bobj.getString("reviewRating") + "</td></tr>"

					+ "<tr><td>Price:</td>" +
					"<td>" + product.getPrice() + "</td></tr>"

					+ "<tr><td>Retailer City:</td>" +
					"<td>" + bobj.getString("retailerCity") + "</td></tr>"

                    + "<tr><td>Date:</td>" +
					"<td>" + bobj.getString("reviewDate") + "</td></tr>"

					+ "<tr><td>Review Text:</td>" +
					"<td>" + bobj.getString("reviewText") + "</td><tr>"

                    + "<tr><td>RetailerZipCode:</td><td>" + bobj.getString("retailerZipCode") + "</td><tr>";


            pw.print(tableData);
        }
        pw.print("</table>");

        //No data found
        if (dbCursor.count() == 0) {
            tableData = "<h2>No Data Found</h2>";
            pw.print(tableData);
        }

    }

}
