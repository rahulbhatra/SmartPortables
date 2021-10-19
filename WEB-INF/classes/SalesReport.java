import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;

@WebServlet("/SalesReport")
public class SalesReport extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        Utilities utility = new Utilities(request, pw);
        String reportType = request.getParameter("reportType");
        utility.printHtml("Header.html");
        utility.printHtml("LeftNavSales.html");

        pw.print("<div class='post' style='float:right; width:79%'>");
        pw.print("<h2 class='title meta'><a style='font-size: 24px;'>Sales Report</a></h2>"
                + "<div class='entry'>"
                + "<div style='margin:25px; margin-left: auto;margin-right: auto;'>");


        switch (reportType) {
            case "dailySales":
                displayTotalDailySales(pw);
                break;
            case "chart":
                displaySalesReportChart(pw);
                break;
            default:
                displaySalesReport(pw);
        }
        pw.print("</div>");
        pw.print("</div>");
    }

    private void displaySalesReport(PrintWriter pw) {

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
        pw.print("Item Sold");
        pw.print("</th>");


        pw.print("<th>");
        pw.print("Total Sale");
        pw.print("</th>");


        pw.print("</tr>");

        Map<Long, ProductSales> productSalesMap = Utilities.getProductsAndSales();
        System.out.println("-------data kyu nahi aa raha ------------" + productSalesMap.values().toString());

        int index = 1;
        for(ProductSales productSale: productSalesMap.values()) {


            pw.print("<tr class='rowTable'>");

            pw.print("<td>");
            pw.print(index++);
            pw.print("</td>");
            pw.print("<td>");
            pw.print(productSale.getProductName());
            pw.print("</td>");
            pw.print("<td>");
            pw.print(productSale.getPrice());
            pw.print("</td>");
            pw.print("<td>");
            pw.print(productSale.getProductSold());
            pw.print("</td>");


            pw.print("<td>");
            pw.print(productSale.getTotalSales());
            pw.print("</td>");

            pw.print("</tr>");
        }

        pw.print("</table>");
    }

    private void displayTotalDailySales(PrintWriter pw) {

        pw.print("<table class='tableUI'>");

        pw.print("<tr class='rowTable'>");

        pw.print("<th>");
        pw.print("Index");
        pw.print("</th>");
        pw.print("<th>");
        pw.print("Date");
        pw.print("</th>");
        pw.print("<th>");
        pw.print("Total Sales");
        pw.print("</th>");
//        pw.print("<th>");
//        pw.print("Item Sold");
//        pw.print("</th>");
//
//
//        pw.print("<th>");
//        pw.print("Total Sale");
//        pw.print("</th>");


        pw.print("</tr>");

        Map<Date, Double> totalDailySales = Utilities.getTotalDailySales();
        System.out.println("-------data kyu nahi aa raha ------------" + totalDailySales.values().toString());

        int index = 1;
        for(Date date: totalDailySales.keySet()) {

            Double totalSales = totalDailySales.get(date);

            pw.print("<tr class='rowTable'>");

            pw.print("<td>");
            pw.print(index++);
            pw.print("</td>");
            pw.print("<td>");
            pw.print(date);
            pw.print("</td>");
            pw.print("<td>");
            pw.print(totalSales);
            pw.print("</td>");
//            pw.print("<td>");
//            pw.print(productSale.getProductSold());
//            pw.print("</td>");
//
//
//            pw.print("<td>");
//            pw.print(productSale.getTotalSales());
//            pw.print("</td>");

            pw.print("</tr>");
        }

        pw.print("</table>");
    }

    protected void displaySalesReportChart(PrintWriter pw) {
        pw.print("<h3><button id='btnGetChartData'>View Chart</h3>");
        pw.println("<div class='barchart' id='chart_div'></div>");
        pw.println("<script type='text/javascript' src='https://www.gstatic.com/charts/loader.js'></script>");
        pw.println("<script type='text/javascript' src='SalesVisualization.js'></script>");

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Map<Long, ProductSales> productSalesMap = Utilities.getProductsAndSales();
            String productJson = new Gson().toJson(productSalesMap.values());

            response.setContentType("application/JSON");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(productJson);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
