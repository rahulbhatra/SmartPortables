import java.io.*;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ProductCrud")

public class ProductCrud extends HttpServlet {
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        Utilities utility = new Utilities(request, pw);
        String action = request.getParameter("button");

        String msg = "good";
        ProductCategory productCategory = null;
        Long productId;
        String productName = "";
        String productImage = "";
        String productManufacturer = "";
        String productCondition = "";
        String prod = "";

        double productPrice = 0.0, productDiscount = 0.0;
        Map<Long, Product> wearableTechMap = new HashMap<>();
        Map<Long, Product> phoneMap = new HashMap<>();
        Map<Long, Product> laptopMap = new HashMap<>();
        Map<Long, Product> voiceAssistantMap = new HashMap<>();
        Map<Long, Product> accessoriesMap = new HashMap<>();

        if (action.equals("add") || action.equals("update")) {
            productCategory = ProductCategory.getEnum(request.getParameter("producttype"));
            productId = Long.parseLong(request.getParameter("productId"));
            productName = request.getParameter("productName");
            productPrice = Double.parseDouble(request.getParameter("productPrice"));
            productImage = request.getParameter("productImage");
            productManufacturer = request.getParameter("productManufacturer");
            productCondition = request.getParameter("productCondition");
            productDiscount = Double.parseDouble(request.getParameter("productDiscount"));

        } else {
            productId = Long.parseLong(request.getParameter("productId"));
        }
        utility.printHtml("Header.html");
        utility.printHtml("LeftNavigationBar.html");

        if (action.equals("add")) {
            if (ProductCategory.WearableTechnology.equals(productCategory)) {
                wearableTechMap = MySqlDataStoreUtilities.getProductsByCategory(ProductCategory.WearableTechnology);
                if (wearableTechMap.containsKey(productId)) {
                    msg = "Product already available";

                }

            } else if (productCategory.equals("games")) {
                laptopMap = MySqlDataStoreUtilities.getProductsByCategory(ProductCategory.Phone);
                if (laptopMap.containsKey(productId)) {
                    msg = "Product already available";
                }
            } else if (productCategory.equals(ProductCategory.Laptop)) {
                phoneMap = MySqlDataStoreUtilities.getProductsByCategory(ProductCategory.Laptop);
                if (phoneMap.containsKey(productId)) {
                    msg = "Product already available";
                }
            } else if (productCategory.equals("accessories")) {
                if (!request.getParameter("product").isEmpty()) {
                    prod = request.getParameter("product");
                    wearableTechMap = MySqlDataStoreUtilities.getProductsByCategory(ProductCategory.WearableTechnology);
                    if (wearableTechMap.containsKey(prod)) {
                        accessoriesMap = MySqlDataStoreUtilities.getProductsByCategory(ProductCategory.Accessories);
                        if (accessoriesMap.containsKey(productId)) {
                            msg = "Product already available";
                        }
                    } else {
                        msg = "The product related to accessories is not available";
                    }


                } else {
                    msg = "Please add the prodcut name";
                }

            }
            if (msg.equals("good")) {
                try {
                    Product product = new Product(productId, productName, productPrice, productImage,
                            ProductManufacturers.getEnum(productManufacturer), productCondition,
                            productDiscount, "", productCategory, 0);
                    msg = MySqlDataStoreUtilities.addProducts(product);
                } catch (Exception e) {
                    msg = "Product cannot be inserted";
                }
                msg = "Product has been successfully added";
            }
        } else if (action.equals("update")) {

            if (ProductCategory.WearableTechnology.equals(productCategory)) {
                wearableTechMap = MySqlDataStoreUtilities.getProductsByCategory(ProductCategory.WearableTechnology);
                if (!wearableTechMap.containsKey(productId)) {
                    msg = "Product not available";
                }

            } else if (ProductCategory.Phone.equals(productCategory)) {
                laptopMap = MySqlDataStoreUtilities.getProductsByCategory(ProductCategory.Phone);
                if (!laptopMap.containsKey(productId)) {
                    msg = "Product not available";
                }
            } else if (ProductCategory.Laptop.equals(productCategory)) {
                phoneMap = MySqlDataStoreUtilities.getProductsByCategory(ProductCategory.Laptop);
                if (!phoneMap.containsKey(productId)) {
                    msg = "Product not available";
                }
            } else if (ProductCategory.VoiceAssistant.equals(productCategory)) {
                accessoriesMap = MySqlDataStoreUtilities.getProductsByCategory(ProductCategory.VoiceAssistant);
                if (!accessoriesMap.containsKey(productId)) {
                    msg = "Product not available";
                }
            } else if (ProductCategory.Accessories.equals(productCategory)) {

                accessoriesMap = MySqlDataStoreUtilities.getProductsByCategory(ProductCategory.Accessories);
                if (!accessoriesMap.containsKey(productId)) {
                    msg = "Product not available";
                }
            }

            if (msg.equals("good")) {

                try {
                    msg = MySqlDataStoreUtilities.updateProducts(productId, productName, productPrice, productImage, productManufacturer, productCondition, productDiscount);
                } catch (Exception e) {
                    msg = "Product cannot be updated";
                }
                msg = "Product has been successfully updated";
            }
        } else if (action.equals("delete")) {
            msg = "bad";
            wearableTechMap = MySqlDataStoreUtilities.getProductsByCategory(ProductCategory.WearableTechnology);
            if (wearableTechMap.containsKey(productId)) {
                msg = "good";

            }


            laptopMap = MySqlDataStoreUtilities.getProductsByCategory(ProductCategory.Phone);
            if (laptopMap.containsKey(productId)) {
                msg = "good";
            }

            phoneMap = MySqlDataStoreUtilities.getProductsByCategory(ProductCategory.Laptop);
            if (phoneMap.containsKey(productId)) {
                msg = "good";
            }

            accessoriesMap = MySqlDataStoreUtilities.getProductsByCategory(ProductCategory.Accessories);
            if (accessoriesMap.containsKey(productId)) {
                msg = "good";
            }

            if (msg.equals("good")) {

                try {
                    msg = MySqlDataStoreUtilities.deleteProducts(productId);
                } catch (Exception e) {
                    msg = "Product cannot be deleted";
                }
                msg = "Product has been successfully deleted";
            } else {
                msg = "Product not available";
            }
        }

        pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
        pw.print("<a style='font-size: 24px;'>Order</a>");
        pw.print("</h2><div class='entry'>");
        pw.print("<h4 style='color:blue'>" + msg + "</h4>");
        pw.print("</div></div></div>");
        utility.printHtml("Footer.html");

    }
}