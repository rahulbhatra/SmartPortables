import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/DeleteProduct")
public class DeleteProduct extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        Long productId = Long.parseLong(request.getParameter("productId"));
        List<Long> accessories = MySqlDataStoreUtilities.getProductAccessories(productId);
        for(Long accessoryId: accessories) {
            MySqlDataStoreUtilities.deleteProductAccessory(productId, accessoryId);
        }

        Product product = MySqlDataStoreUtilities.getProduct(productId);
        MySqlDataStoreUtilities.deleteProducts(productId);
        response.sendRedirect("ProductList?category=" + product.getCategory().toString() + "&manufacturer=" +
                product.getManufacturer().toString());
    }
}
