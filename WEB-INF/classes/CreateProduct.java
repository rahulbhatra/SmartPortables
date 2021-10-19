import com.mysql.cj.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;

@WebServlet("/CreateProduct")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 10,      // 10 MB
        maxRequestSize = 1024 * 1024 * 100   // 100 MB
)
public class CreateProduct extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        HttpSession session = request.getSession();
        Utilities utility = new Utilities(request, pw);

        utility.printHtml("Header.html");

        pw.print("<div class='post' style='float: none; width: 100%'>");
        pw.print("<h2 class='title meta'><a style='font-size: 24px;'>Create Product</a></h2>"
                + "<div class='entry'>"
                + "<div style='margin:25px; margin-left: auto;margin-right: auto;'>");
        pw.print("<div class='content'>");
        pw.print("<section class='createContent'>");

        pw.print("<form action='CreateProduct' method='post' enctype=\"multipart/form-data\">");

        pw.print("<table style='width: 100%'>");

        pw.print("<tr class='rowTable'>");
        pw.print("<td class='leftDataTable'>");
        pw.print("Product Name:");
        pw.print("</td>");
        pw.print("<td class='rightDataTable'>");
        pw.print("<input style='radius: 20px; width: 100%;' type='text' placeholder='Enter Product Name' name='productName' required>");
        pw.print("</td>");
        pw.print("</tr>");

        pw.print("<tr class='rowTable'>");
        pw.print("<td class='leftDataTable'>");
        pw.print("Product Price:");
        pw.print("</td>");
        pw.print("<td class='rightDataTable'>");
        pw.print("<input style='radius: 20px; width: 100%;' min='0' type='number' placeholder='Enter Product Price' name='productPrice' required>");
        pw.print("</td>");
        pw.print("</tr>");

        pw.print("<tr class='rowTable'>");
        pw.print("<td class='leftDataTable'>");
        pw.print("Product Image:");
        pw.print("</td>");
        pw.print("<td class='rightDataTable'>");
        pw.print("<input style='radius: 20px; width: 100%;' type='file' placeholder='Select Image' name='productImage' required>");
        pw.print("</td>");
        pw.print("</tr>");

        pw.print("<tr class='rowTable'>");
        pw.print("<td class='leftDataTable'>");
        pw.print("Product Manufacturer:");
        pw.print("</td>");
        pw.print("<td class='rightDataTable'>");
        pw.print("<select style='radius: 20px; width: 100%;' name='productManufacturer' id='productManufacturer'>");
        for(ProductManufacturers productManufacturers : ProductManufacturers.values()) {
            pw.print("<option value='" + productManufacturers.toString() + "'>" + productManufacturers + "</option>");
        }
        pw.print("</select>");
        pw.print("</td>");
        pw.print("</tr>");

//        pw.print("<tr class='rowTable'>");
//        pw.print("<td class='leftDataTable'>");
//        pw.print("Product Accessory:");
//        pw.print("</td>");
//        pw.print("<td class='rightDataTable'>");
//        pw.print("<select style='radius: 20px; width: 100%;' name='accessoryId' id='accessoryId'>");
//        pw.print("<option value='" + 0 + "'>" + "None" + "</option>");
//        for(Product product : MySqlDataStoreUtilities.getProducts()) {
//            pw.print("<option value='" + product.getProductId() + "'>" + product.getProductName() + "</option>");
//        }
//        pw.print("</select>");
//        pw.print("</td>");
//        pw.print("</tr>");

        pw.print("<tr class='rowTable'>");
        pw.print("<td class='leftDataTable'>");
        pw.print("Product Condition:");
        pw.print("</td>");
        pw.print("<td class='rightDataTable'>");
        pw.print("<select style='radius: 20px; width: 100%;' name='productCondition' id='productCondition'>" +
                "<option value='New'>NEW</option>" +
                "<option value='Old'>OLD</option>" +
                "</select>");
        pw.print("</td>");
        pw.print("</tr>");

        pw.print("<tr class='rowTable'>");
        pw.print("<td class='leftDataTable'>");
        pw.print("Product Category:");
        pw.print("</td>");
        pw.print("<td class='rightDataTable'>");
        pw.print("<select style='radius: 20px; width: 100%;' name='productCategory' id='productCategory'>");

        for(ProductCategory productCategory: ProductCategory.values()) {
            pw.print("<option value='" + productCategory.toString() + "'>" + productCategory + "</option>");
        }
        pw.print("</select>");
        pw.print("</td>");
        pw.print("</tr>");

        pw.print("<tr class='rowTable'>");
        pw.print("<td class='leftDataTable'>");
        pw.print("Product Description:");
        pw.print("</td>");
        pw.print("<td class='rightDataTable'>");
        pw.print("<input style='radius: 20px; width: 100%;' type='text' placeholder='Enter Product Description' name='productDescription'>");
        pw.print("</td>");
        pw.print("</tr>");

        pw.print("<tr class='rowTable'>");
        pw.print("<td class='leftDataTable'>");
        pw.print("Product Discount:");
        pw.print("</td>");
        pw.print("<td class='rightDataTable'>");
        pw.print("<input style='radius: 20px; width: 100%;' min='0' type='number' placeholder='Enter Product Discount' name='productDiscount'>");
        pw.print("</td>");
        pw.print("</tr>");

        pw.print("<tr class='rowTable'>");
        pw.print("<td class='leftDataTable'>");
        pw.print("Manufacturer Rebate:");
        pw.print("</td>");
        pw.print("<td class='rightDataTable'>");
        pw.print("<input style='radius: 20px; width: 100%;' min='0' type='number' placeholder='Enter Manufacturer Rebate' name='manufacturerRebate'>");
        pw.print("</td>");
        pw.print("</tr>");

        pw.print("</table>");

        pw.print("<div class='buttonContainer'><button class='createContentButton' type='submit' value='Submit'>Add</button></div>");

        pw.print("</form>");

        pw.print("</section>");
        pw.print("</div>");

        pw.print("</div>");
        pw.print("</div>");
        pw.print("</div>");
        utility.printHtml("Footer.html");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();


        Utilities utilities = new Utilities(request, pw);
//        Long productId = Long.parseLong(request.getParameter("productId"));
        String productName = request.getParameter("productName");
        Double productPrice = !Utilities.isNullOrEmptyOrWhiteSpaceOnly(request.getParameter("productPrice")) ?
                Double.parseDouble(request.getParameter("productPrice")) : 0;

        Part filePart = request.getPart("productImage");
        String productImage = filePart.getSubmittedFileName();
        ProductManufacturers productManufacturer = ProductManufacturers.getEnum(request.getParameter("productManufacturer"));
        String productCondition = request.getParameter("productCondition");
        ProductCategory productCategory = ProductCategory.getEnum(request.getParameter("productCategory"));
        String productDescription = Utilities.isNullOrEmptyOrWhiteSpaceOnly(request.getParameter("productDescription")) ? "" :
                request.getParameter("productDescription").toString();
        double productDiscount = !Utilities.isNullOrEmptyOrWhiteSpaceOnly(request.getParameter("productDiscount")) ?
                Double.parseDouble(request.getParameter("productDiscount")) : 0;
        double manufacturerRebate = !Utilities.isNullOrEmptyOrWhiteSpaceOnly(request.getParameter("manufacturerRebate"))
                ? Double.parseDouble(request.getParameter("manufacturerRebate")) : 0;

        System.out.println(productPrice + " " + productImage + " " + productManufacturer
                + " " + productCondition + " " + productCategory + " " + productDescription);


        utilities.fileUpload(filePart, productImage);
        Product product = new Product(null, productName, productPrice, productImage, productManufacturer, productCondition,
                productDiscount, productDescription, productCategory, manufacturerRebate);
        product = MySqlDataStoreUtilities.addProducts(product);
//        Long accessoryId = Long.parseLong(request.getParameter("accessoryId"));
//        if(accessoryId != 0) {
//            System.out.println("----------- Product Id after insertion is -----------" + product.getProductId());
//            MySqlDataStoreUtilities.insertProductAccessory(product.getProductId(), accessoryId);
//        }

        response.sendRedirect("ProductList?category=" + product.getCategory().toString() + "&manufacturer=" +
                product.getManufacturer().toString());
    }

}
