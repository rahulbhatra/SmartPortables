import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@WebServlet("/Trending")

public class Trending extends HttpServlet {

    List<MostSold> mostSold = new ArrayList<MostSold>();
    List<Mostsoldzip> mostSoldZip = new ArrayList<Mostsoldzip>();
    List<BestRating> bestRated = new ArrayList<BestRating>();

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
//        mostSold = MongoDBDataStoreUtilities.mostSoldProducts();
//        mostsoldzip = MongoDBDataStoreUtilities.mostSoldZip();
        bestRated = MongoDBDataStoreUtilities.topProducts();
        mostSoldZip = MySqlDataStoreUtilities.getTopNMostSoldZip(5);
        mostSold = MySqlDataStoreUtilities.getTopNMostSold(5);


        String name = "Trending";


        Utilities utility = new Utilities(request, pw);
        utility.printHtml("Header.html");
        utility.printHtml("LeftNavigationBar.html");
        pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
        pw.print("<a style='font-size: 24px;'>Best Products</a>");
        pw.print("</h2><div class='entry'><table id='bestseller'>");
        Iterator itr2 = bestRated.iterator();
        while (itr2.hasNext()) {
            BestRating best = (BestRating) itr2.next();
			Product product = MySqlDataStoreUtilities.getProduct(best.productId);
            pw.print("<tr>");
            pw.print("<td>");
            pw.print(product.getProductName());
            pw.print("</td>");
            pw.print("<td>");
            pw.print(best.getRating());
            pw.print("</td>");
            pw.print("</tr>");
        }
        pw.print("</table></div></div></div>");

        pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
        pw.print("<a style='font-size: 24px;'>Most Sold Products by Zipcode</a>");
        pw.print("</h2><div class='entry'><table id='bestseller'>");
        Iterator itr1 = mostSoldZip.iterator();
        while (itr1.hasNext()) {
            Mostsoldzip mostzip = (Mostsoldzip) itr1.next();
            pw.print("<tr>");
            pw.println("<td border: 1px >");

            pw.println(mostzip.getZipcode());
            pw.println("</td>");
            pw.println("<td border: 1px >");
            pw.println(mostzip.getCount());
            pw.println("</td>");
            pw.println("</tr>");
        }
        pw.print("</table></div></div></div>");

        pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
        pw.print("<a style='font-size: 24px;'>Most Sold Products</a>");
        pw.print("</h2><div class='entry'><table id='bestseller'>");

        Iterator itr = mostSold.iterator();
        while (itr.hasNext()) {
            MostSold most = (MostSold) itr.next();
            Product product = MySqlDataStoreUtilities.getProduct(most.getProductId());
            pw.println("<tr>");
            pw.println("<td border: 1px >");
            pw.println(product.getProductName());
            pw.println("</td>");
            pw.println("<td border: 1px >");
            pw.println(most.getCount());
            pw.println("</td>");
            pw.println("</tr>");
        }
        pw.print("</table></div></div></div>");

        //	pw.print("</table></div></div></div>");


        utility.printHtml("Footer.html");
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

    }

}
