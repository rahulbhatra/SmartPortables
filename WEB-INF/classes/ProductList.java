import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/ProductList")

public class ProductList extends HttpServlet {

    /* Console Page Displays all the WearableTechnology and their Information in Game Speed */

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        HttpSession session = request.getSession(true);
        Object userType = session.getAttribute("usertype");

        String manufacturer = request.getParameter("manufacturer");
        ProductCategory category = ProductCategory.getEnum(request.getParameter("category"));

        HashMap<Long, Product> productHashMap = new HashMap<>();

        String name = "";
        switch (category) {
            case WearableTechnology:
                name = "Wearable";
                productHashMap.putAll(MySqlDataStoreUtilities.getProductsByCategory(ProductCategory.WearableTechnology));
                break;
            case Phone:
                name = "Phone";
                productHashMap.putAll(MySqlDataStoreUtilities.getProductsByCategory(ProductCategory.Phone));
                break;
            case Laptop:
                name = "Laptop";
                productHashMap.putAll(MySqlDataStoreUtilities.getProductsByCategory(ProductCategory.Laptop));
                break;
            case VoiceAssistant:
                name = "Voice Assistant";
                productHashMap.putAll(MySqlDataStoreUtilities.getProductsByCategory(ProductCategory.VoiceAssistant));
                break;
        }

        if (manufacturer != null && !manufacturer.isEmpty()) {
            HashMap<Long, Product> temp = new HashMap<>();
            ProductManufacturers productManufacturers = ProductManufacturers.getEnum(manufacturer);
            name = manufacturer;
            for (Map.Entry<Long, Product> entry : productHashMap.entrySet()) {
                if (entry.getValue().getManufacturer().equals(productManufacturers)) {
                    temp.put(entry.getValue().getProductId(), entry.getValue());
                }
            }
            productHashMap = temp;
        }

		/* Header, Left Navigation Bar are Printed.

		All the Console and Console information are dispalyed in the Content Section

		and then Footer is Printed*/

        Utilities utility = new Utilities(request, pw);
        utility.printHtml("Header.html");
        utility.printHtml("LeftNavigationBar.html");
        pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
        pw.print("<a style='font-size: 24px;'>" + name + "</a>");
        pw.print("</h2><div class='entry'><table id='bestseller'>");
        int i = 1;
        int size = productHashMap.size();
        for (Map.Entry<Long, Product> entry : productHashMap.entrySet()) {
            Product product = entry.getValue();
            if (i % 3 == 1) pw.print("<tr>");
            pw.print("<td><div id='shop_item'>");
            pw.print("<h3>" + product.getName() + "</h3>");
            pw.print("<strong>$" + product.getPrice() + "</strong><ul>");
            pw.print("<li id='item'><img src='images/ProductImages/" + product.getImage() + "' alt='' /></li>");
            pw.print("<li class='description'><span>" + product.getDescription() + "</span></li>");

            pw.print("<li class='description'><span> Discount:" + product.getDiscount() + "</span></li>");
            pw.print("<li class='description'><span> Rebate:" + product.getRebate() + "</span></li>");

            System.out.println(entry.getKey() + " " + manufacturer);


            if (userType != null && "retailer".equalsIgnoreCase(userType.toString())) {
                pw.print("<li><form method='post' action='EditProduct'>" +
                        "<input type='hidden' name='name' value='" + entry.getKey() + "'>" +
                        "<input type='hidden' name='type' value='" + product.getCategory() + "'>" +
                        "<input type='submit' class='btnbuy' value='Edit'></form></li>");

                pw.print("<li><form method='post' action='DeleteProduct'>" +
                        "<input type='hidden' name='name' value='" + entry.getKey() + "'>" +
                        "<input type='hidden' name='type' value='" + product.getCategory() + "'>" +
                        "<input type='hidden' name='maker' value='" + manufacturer + "'>" +
                        "<input type='hidden' name='access' value=''>" +
                        "<input type='submit' class='btnbuy' value='Delete'></form></li>");
            } else {
                System.out.println(entry.getKey() + " " + manufacturer);

                pw.print("<li><form method='post' action='Cart'>" +
                        "<input type='hidden' name='productId' value='" + product.getProductId() + "'>" +
                        "<input type='checkbox' id='warranty' name='warranty' value='warranty'>" +
                        "<label for='vehicle1'>1 Year Warranty 25$</label>" +
                        "<input type='submit' class='btnbuy' value='Buy Now'></form></li>");

                pw.print("<li><form method='post' action='ViewProduct'>" +
                        "<input type='hidden' name='name' value='" + entry.getKey() + "'>" +
                        "<input type='hidden' name='type' value='" + product.getCategory() + "'>" +
                        "<input type='hidden' name='maker' value='" + manufacturer + "'>" +
                        "<input type='hidden' name='access' value=''>" +
                        "<input type='submit' class='btnbuy' value='View Product'></form></li>");

                pw.print("<li><form method='post' action='WriteReview'>"+"<input type='hidden' name='name' value='"+product.getName()+"'>"+
                        "<input type='hidden' name='type' value='accessories'>"+
                        "<input type='hidden' name='maker' value='"+ product.getCategory()+"'>"+
                        "<input type='hidden' name='access' value='"+product.getName()+"'>"+
                        "<input type='hidden' name='price' value='"+product.getPrice()+"'>"+
                        "<input type='submit' value='WriteReview' class='btnreview'></form></li>");
                pw.print("<li><form method='post' action='ViewReview'>"+"<input type='hidden' name='name' value='"+product.getName()+"'>"+
                        "<input type='hidden' name='type' value='accessories'>"+
                        "<input type='hidden' name='maker' value='"+product.getCategory()+"'>"+
                        "<input type='hidden' name='access' value='"+product.getName()+"'>"+
                        "<input type='submit' value='ViewReview' class='btnreview'></form></li>");

            }


            pw.print("</ul></div></td>");
            if (i % 3 == 0 || i == size) pw.print("</tr>");
            i++;
        }
        pw.print("</table></div></div></div>");

        utility.printHtml("Footer.html");

    }
}
