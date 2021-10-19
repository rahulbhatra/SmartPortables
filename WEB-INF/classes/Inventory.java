import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/Inventory")
public class Inventory extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        Utilities utility = new Utilities(request, pw);
        String inventoryType = request.getParameter("inventoryType");
        utility.printHtml("Header.html");
        utility.printHtml("LeftNavInventory.html");

        pw.print("<div class='post' style='float:right; width:79%'>");
        pw.print("<h2 class='title meta'><a style='font-size: 24px;'>Inventory</a></h2>"
                + "<div class='entry'>"
                + "<div style='margin:25px; margin-left: auto;margin-right: auto;'>");

//        pw.print("<div class='menu'>");
//        pw.print("<div class='menu-item'><a href='Inventory?inventoryType=rebate'><span>Rebate Products</span></a></div>");
//        pw.print("<div class='menu-item'><a href='Inventory?inventoryType=onSale'><span>OnSale Products</span></a></div>");
//        pw.print("<div class='menu-item'><a href='Inventory?inventoryType=countChart'><span>Products Count Chart</span></a></div>");
//        pw.print("</div>");


        switch (inventoryType) {
            case "rebate":
                displayInventory(pw, true, false);
                break;
            case "onSale":
                displayInventory(pw, false, true);
                break;
            case "countChart":
                displayCountChart(utility, pw);
                break;
            default:
                displayInventory(pw, false, false);
        }
        pw.print("</div>");
        pw.print("</div>");
//        utility.printHtml("Footer.html");
    }

    private void displayInventory(PrintWriter pw, boolean isRebate, boolean isOnSale) {

        pw.print("<table class='tableUI'>");

        pw.print("<tr class='rowTable'>");

        pw.print("<th>");
        pw.print("Index");
        pw.print("</th>");
        pw.print("<th>");
        pw.print("Product Name");
        pw.print("</th>");
        pw.print("<th>");
        pw.print("Product Price");
        pw.print("</th>");
        pw.print("<th>");
        pw.print("Product Count");
        pw.print("</th>");

        if(isOnSale) {
            pw.print("<th>");
            pw.print("On Sale");
            pw.print("</th>");
        }

        if(isRebate) {
            pw.print("<th>");
            pw.print("Manufacturer Rebate");
            pw.print("</th>");
        }

        pw.print("</tr>");

        List<Product> products = MySqlDataStoreUtilities.getProducts();

        int index = 1;
        for(Product product: products) {

            if(isOnSale && product.getDiscount() == 0) {
                continue;
            }

            if(isRebate && product.getRebate() == 0) {
                continue;
            }

            pw.print("<tr class='rowTable'>");

            pw.print("<td>");
            pw.print(index++);
            pw.print("</td>");
            pw.print("<td>");
            pw.print(product.getProductName());
            pw.print("</td>");
            pw.print("<td>");
            pw.print(product.getPrice());
            pw.print("</td>");
            pw.print("<td>");
            pw.print(product.getCount());
            pw.print("</td>");

            if(isOnSale) {
                pw.print("<td>");
                pw.print(product.getDiscount() > 0 ? "Yes" : "No");
                pw.print("</td>");
            }

            if(isRebate) {
                pw.print("<td>");
                pw.print(product.getRebate() > 0 ? "Yes" : "No");
                pw.print("</td>");
            }

            pw.print("</tr>");
        }

        pw.print("</table>");
    }

    protected void displayCountChart(Utilities utility, PrintWriter pw) {


        pw.print("<h3><button id='btnGetChartData'>View Chart</h3>");
        pw.println("<div class='barchart' id='chart_div'></div>");

        pw.println("<script type='text/javascript' src='https://www.gstatic.com/charts/loader.js'></script>");
        pw.println("<script type='text/javascript' src='InventoryVisualization.js'></script>");

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<Product> products = MySqlDataStoreUtilities.getProducts();
            String productJson = new Gson().toJson(products);

            response.setContentType("application/JSON");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(productJson);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
