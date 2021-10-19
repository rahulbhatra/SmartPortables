import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/DeleteAccessory")
public class DeleteAccessory extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        Long productId = Long.parseLong(request.getParameter("productId"));
        Long accessoryId = Long.parseLong(request.getParameter("accessoryId"));
        MySqlDataStoreUtilities.deleteProductAccessory(productId, accessoryId);
        response.sendRedirect("ViewProduct?productId=" + productId);
    }

}