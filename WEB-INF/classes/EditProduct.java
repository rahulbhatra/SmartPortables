import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/EditProduct")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 10,      // 10 MB
        maxRequestSize = 1024 * 1024 * 100   // 100 MB
)
public class EditProduct extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();

        System.out.println("------------------- Inside Edit Product Post Method ---------------");
        Utilities utilities = new Utilities(request, pw);
        Long productId = Long.parseLong(request.getParameter("productId"));
        Product product = MySqlDataStoreUtilities.getProduct(productId);
        displayEditForm(utilities, pw, product);
    }


    private void displayEditForm(Utilities utility, PrintWriter pw, Product product) {

        System.out.println("------- Product Manufacturer ------" + product.getManufacturer());

        utility.printHtml("Header.html");

        pw.print("<div class='post' style='float: none; width: 100%'>");
        pw.print("<h2 class='title meta'><a style='font-size: 24px;'>Edit Product</a></h2>"
                + "<div class='entry'>"
                + "<div style='margin:25px; margin-left: auto;margin-right: auto;'>");
        pw.print("<div class='content'>");
        pw.print("<section class='createContent'>");

        pw.print("<form action='EditProduct' method='post' enctype=\"multipart/form-data\">");
        pw.print("<input type='hidden' id='productId' name='productId' value='" + product.getProductId() + "'>");
        pw.print("<table style='width: 100%'>");

        pw.print("<tr class='rowTable'>");
        pw.print("<td class='leftDataTable'>");
        pw.print("Product Name:");
        pw.print("</td>");
        pw.print("<td class='rightDataTable'>");
        pw.print("<input style='radius: 20px; width: 100%;' type='text' placeholder='Enter Product Name' name='productName' value='" + product.getProductName() + "' required>");
        pw.print("</td>");
        pw.print("</tr>");

        pw.print("<tr class='rowTable'>");
        pw.print("<td class='leftDataTable'>");
        pw.print("Product Price:");
        pw.print("</td>");
        pw.print("<td class='rightDataTable'>");
        pw.print("<input style='radius: 20px; width: 100%;' type='text' placeholder='Enter Product Price' name='productPrice' value = '" + product.getPrice() + "'required>");
        pw.print("</td>");
        pw.print("</tr>");

        pw.print("<tr class='rowTable'>");
        pw.print("<td class='leftDataTable'>");
        pw.print("Product Image:");
        pw.print("</td>");
        pw.print("<td class='rightDataTable'>");
        pw.print("<input style='radius: 20px; width: 100%;' type='file' placeholder='Select Image' name='productImage' value='" + product.getImage() + "'>");
        pw.print("</td>");
        pw.print("</tr>");

        pw.print("<tr class='rowTable'>");
        pw.print("<td class='leftDataTable'>");
        pw.print("Product Manufacturer:");
        pw.print("</td>");
        pw.print("<td class='rightDataTable'>");
        pw.print("<select style='radius: 20px; width: 100%;' name='productManufacturer' id='productManufacturer'>");

        for(ProductManufacturers productManufacturers : ProductManufacturers.values()) {
            if(productManufacturers.equals(product.getManufacturer())) {
                pw.print("<option value='" + productManufacturers.toString() +"' selected>" + productManufacturers + "</option>");
            }else {
                pw.print("<option value='" + productManufacturers.toString() +"'>" + productManufacturers + "</option>");
            }
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
//        for(Product accessory : MySqlDataStoreUtilities.getProducts()) {
//            pw.print("<option value='" + accessory.getProductId() + "'>" + accessory.getProductName() + "</option>");
//        }
//        pw.print("</select>");
//        pw.print("</td>");
//        pw.print("</tr>");

        pw.print("<tr class='rowTable'>");
        pw.print("<td class='leftDataTable'>");
        pw.print("Product Condition:");
        pw.print("</td>");
        pw.print("<td class='rightDataTable'>");

        pw.print("<select style='radius: 20px; width: 100%;' name='productCondition' id='productCondition'>");
        for(String condition: utility.getProductCondition()) {
            if(condition.equalsIgnoreCase(product.getCondition())) {
                pw.print("<option value='" + condition + "' selected>" + condition + "</option>");
            } else {
                pw.print("<option value='" + condition + "'>" + condition + "</option>");
            }
        }
        pw.print("</select>");
        pw.print("</td>");
        pw.print("</tr>");

        pw.print("<tr class='rowTable'>");
        pw.print("<td class='leftDataTable'>");
        pw.print("Product Category:");
        pw.print("</td>");
        pw.print("<td class='rightDataTable'>");
        pw.print("<select style='radius: 20px; width: 100%;' name='productCategory' id='productCategory'>");

        for(ProductCategory productCategory : ProductCategory.values()) {
            if (product.getCategory() != null && productCategory.equals(product.getCategory())) {
                pw.print("<option value='" + productCategory.toString() +"' selected>" + productCategory + "</option>");
            } else {
                pw.print("<option value='" + productCategory.toString() +"'>" + productCategory + "</option>");
            }
        }

        pw.print("</select>");
        pw.print("</td>");
        pw.print("</tr>");

        pw.print("<tr class='rowTable'>");
        pw.print("<td class='leftDataTable'>");
        pw.print("Product Description:");
        pw.print("</td>");
        pw.print("<td class='rightDataTable'>");
        pw.print("<input style='radius: 20px; width: 100%;' type='text' placeholder='Enter Product Description' name='productDescription' value='" + product.getDescription() + "'>");
        pw.print("</td>");
        pw.print("</tr>");

        pw.print("<tr class='rowTable'>");
        pw.print("<td class='leftDataTable'>");
        pw.print("Product Discount:");
        pw.print("</td>");
        pw.print("<td class='rightDataTable'>");
        pw.print("<input style='radius: 20px; width: 100%;' type='text' placeholder='Enter Product Discount' name='productDiscount' value='" + product.getDiscount() + "'>");
        pw.print("</td>");
        pw.print("</tr>");

        pw.print("<tr class='rowTable'>");
        pw.print("<td class='leftDataTable'>");
        pw.print("Manufacturer Rebate:");
        pw.print("</td>");
        pw.print("<td class='rightDataTable'>");
        pw.print("<input style='radius: 20px; width: 100%;' type='text' placeholder='Enter Manufacturer Rebate' name='manufacturerRebate' value='" + product.getRebate() + "'>");
        pw.print("</td>");
        pw.print("</tr>");

        pw.print("</table>");

        pw.print("<div class='buttonContainer'><button class='classicButton' type='submit' value='Submit'>Edit Product</button></div>");

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
        String productName = request.getParameter("productName");
        System.out.println("------------- Product Id ------------" + request.getParameter("productId") +
                request.getParameter("productName"));

        Long productId = Long.parseLong(request.getParameter("productId"));

        Product oldProduct = MySqlDataStoreUtilities.getProduct(productId);


        Double productPrice = !Utilities.isNullOrEmptyOrWhiteSpaceOnly(request.getParameter("productPrice")) ?
                Double.parseDouble(request.getParameter("productPrice")) : 0;

        Part filePart = request.getPart("productImage");

        String productImage;
        if(Utilities.isNullOrEmptyOrWhiteSpaceOnly(filePart.getSubmittedFileName())) {
            productImage = oldProduct.getImage();
        } else {
            productImage = filePart.getSubmittedFileName();
            utilities.fileUpload(filePart, productName);
        }

        ProductManufacturers productManufacturer = ProductManufacturers.getEnum(request.getParameter("productManufacturer"));
        String productCondition = request.getParameter("productCondition");
        ProductCategory productCategory = ProductCategory.getEnum(request.getParameter("productCategory"));
        String productDescription = request.getParameter("productDescription");
        double productDiscount = !Utilities.isNullOrEmptyOrWhiteSpaceOnly(request.getParameter("productDiscount")) ?
                Double.parseDouble(request.getParameter("productDiscount")) : 0;
        double manufacturerRebate = !Utilities.isNullOrEmptyOrWhiteSpaceOnly(request.getParameter("manufacturerRebate"))
                ? Double.parseDouble(request.getParameter("manufacturerRebate")) : 0;

        System.out.println(productPrice + " " + productImage + " " + productManufacturer
                + " " + productCondition + " " + productCategory + " " + productDescription);

        Product product = new Product(productId, productName, productPrice, productImage, productManufacturer, productCondition,
                productDiscount, productDescription, productCategory, manufacturerRebate);
        MySqlDataStoreUtilities.updateProducts(product);

        response.sendRedirect("ProductList?category=" + product.getCategory().toString() + "&manufacturer=" +
                product.getManufacturer().toString());
    }
}
