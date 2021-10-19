import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@WebServlet("/ViewProduct")
public class ViewProduct extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        HttpSession session = request.getSession(true);
        Long productId = Long.parseLong(request.getParameter("productId"));
        UserType userType = session.getAttribute("usertype") != null ? UserType.getEnum(session.getAttribute("usertype").toString()) :
                null;

        /* Checks the WearableTechnology type whether it is Fitness Watch or Smart Watches or Headphones */
        Product product = MySqlDataStoreUtilities.getProduct(productId);


		/* Header, Left Navigation Bar are Printed.

		All the Console and Console information are dispalyed in the Content Section

		and then Footer is Printed*/

        Utilities utility = new Utilities(request, pw);
        utility.printHtml("Header.html");
        utility.printHtml("LeftNavigationBar.html");
        pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
        pw.print("<a style='font-size: 24px;'>" + product.getProductName() + "</a>");
        pw.print("</h2><div class='entry'><table id='bestseller'>");


        pw.print("<tr>");
        pw.print("<td><div id='shop_item'>");
        pw.print("<h3>" + product.getProductName() + "</h3>");
        pw.print("<strong>$" + product.getPrice() + "</strong><ul>");
        pw.print("<li id='item'><img src='images/ProductImages/" + product.getImage() + "' alt='' /></li>");
        pw.print("<li class='description'><span>" + product.getDescription() + "</span></li>");
        pw.print("<li class='description'><span> Discount:" + product.getDiscount() + "</span></li>");
        pw.print("<li class='description'><span> Rebate:" + product.getRebate() + "</span></li>");


        if (userType != null && UserType.StoreManager.equals(userType)) {

            pw.print("<li><form method='post' action='AddAccessory'>");
            pw.print("<input type='hidden' name='productId' value='" + product.getProductId() + "'>");
            pw.print("<select style='radius: 20px; width: 100%;' name='accessoryId' id='accessoryId'>");
            for(Product accessory : MySqlDataStoreUtilities.getProducts()) {
                pw.print("<option value='" + accessory.getProductId() + "'>" + accessory.getProductName() + "</option>");
            }
            pw.print("</select>");
            pw.print("<input type='submit' class='btnbuy' value='Add Accessory'></form></li>");


            pw.print("<li><form method='post' action='DeleteAccessory'>");
            pw.print("<input type='hidden' name='productId' value='" + product.getProductId() + "'>");
            pw.print("<select style='radius: 20px; width: 100%;' name='accessoryId' id='accessoryId'>");
            for(Long accessoryId : MySqlDataStoreUtilities.getProductAccessories(productId)) {
                Product accessory = MySqlDataStoreUtilities.getProduct(accessoryId);
                pw.print("<option value='" + accessory.getProductId() + "'>" + accessory.getProductName() + "</option>");
            }
            pw.print("</select>");
            pw.print("<input type='submit' class='btnbuy' value='Delete Accessory'></form></li>");
        } else {
            pw.print("<li><form method='post' action='Cart'>" +
                    "<input type='hidden' name='productId' value='" + product.getProductId() + "'>" +
                    "<input type='checkbox' id='warranty' name='warranty' value='warranty'>" +
                    "<label for='vehicle1'>1 Year Warranty 25$</label>" +
                    "<input type='submit' class='btnbuy' value='Buy Now'></form></li>");

            pw.print("<li><form method='post' action='ViewProduct'>" +
                    "<input type='hidden' name='productId' value='" + product.getProductId() + "'>" +
                    "<input type='submit' class='btnbuy' value='View Product'></form></li>");

            pw.print("<li><form method='get' action='WriteReview'>" +
                    "<input type='hidden' name='productId' value='" + product.getProductId() + "'>" +
                    "<input type='submit' value='WriteReview' class='btnreview'></form></li>");
            pw.print("<li><form method='get' action='ViewReview'>" + "<input type='hidden' name='name' value='" + product.getProductName() + "'>" +
                    "<input type='hidden' name='productId' value='" + product.getProductId() + "'>" +
                    "<input type='submit' value='ViewReview' class='btnreview'></form></li>");

        }


        pw.print("</ul></div></td>");
        pw.print("</tr>");
        pw.print("</table></div></div></div>");
        pw.print(Carousel.carouselFeature(utility, product));
        utility.printHtml("Footer.html");
    }



}
