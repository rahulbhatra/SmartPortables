import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/AccessoryList")

public class AccessoryList extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        String manufacturer = request.getParameter("maker");
        Map<Long, Product> allAccessoriesHashMap = new HashMap<>();
        Map<Long, Product> productHashMap = new HashMap<>();
        try {
            productHashMap = MySqlDataStoreUtilities.getProductsByCategory(ProductCategory.WearableTechnology);
			allAccessoriesHashMap = MySqlDataStoreUtilities.getProductsByCategory(ProductCategory.Accessory);
        } catch (Exception e) {
            e.printStackTrace();
        }

		if (manufacturer != null && !manufacturer.isEmpty()) {
			HashMap<Long, Product> temp = new HashMap<>();
			ProductManufacturers productManufacturers = ProductManufacturers.getEnum(manufacturer);
			for (Map.Entry<Long, Product> entry : productHashMap.entrySet()) {
				if (entry.getValue().getManufacturer().equals(productManufacturers)) {
					temp.put(entry.getValue().getProductId(), entry.getValue());
				}
			}
			productHashMap = temp;
		}

        Utilities utility = new Utilities(request, pw);
        utility.printHtml("Header.html");
        utility.printHtml("LeftNavigationBar.html");
        pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
        pw.print("<a style='font-size: 24px;'>" + manufacturer + ": Accessories</a>");
        pw.print("</h2><div class='entry'><table id='bestseller'>");
        int i = 1;
        int size = 2;
        for (Product product: productHashMap.values()) {

            for (Map.Entry<Integer, Integer> acc : product.getAccessories().entrySet()) {

                Product accessory = allAccessoriesHashMap.get(acc.getValue());
                if (i % 2 == 1) pw.print("<tr>");

                pw.print("<td><div id='shop_item'>");
                pw.print("<h3>" + accessory.getProductName() + "</h3>");
                pw.print("<strong>" + accessory.getPrice() + "$</strong><ul>");
                pw.print("<li id='item'><img src='images/accessories/" + accessory.getImage() + "' alt='' /></li>");
                pw.print("<li><form method='post' action='Cart'>" +
                        "<input type='hidden' name='name' value='" + acc.getValue() + "'>" +
                        "<input type='hidden' name='type' value='accessories'>" +
                        "<input type='hidden' name='maker' value='" + product.getManufacturer().toString() + "'>" +
                        "<input type='hidden' name='access' value='" + product.getProductName() + "'>" +
                        "<input type='submit' class='btnbuy' value='Buy Now'></form></li>");
                pw.print("<li><form method='post' action='WriteReview'>" + "<input type='hidden' name='name' value='" + accessory.getProductName() + "'>" +
                        "<input type='hidden' name='type' value='accessories'>" +
                        "<input type='hidden' name='maker' value='" + product.getManufacturer().toString() + "'>" +
                        "<input type='hidden' name='access' value='" + product.getProductName() + "'>" +
                        "<input type='hidden' name='price' value='" + accessory.getPrice() + "'>" +
                        "<input type='submit' value='WriteReview' class='btnreview'></form></li>");
                pw.print("<li><form method='post' action='ViewReview'>" + "<input type='hidden' name='name' value='" + accessory.getProductName() + "'>" +
                        "<input type='hidden' name='type' value='accessories'>" +
                        "<input type='hidden' name='maker' value='" + product.getManufacturer().toString() + "'>" +
                        "<input type='hidden' name='access' value='" + product.getProductName() + "'>" +
                        "<input type='submit' value='ViewReview' class='btnreview'></form></li>");

                pw.print("</ul></div></td>");
                if (i % 2 == 0 || i == size) pw.print("</tr>");
                i++;

            }
        }
        pw.print("</table></div></div></div>");
        utility.printHtml("Footer.html");
    }
}
