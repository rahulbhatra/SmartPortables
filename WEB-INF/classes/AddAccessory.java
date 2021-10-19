import com.mysql.cj.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;

@WebServlet("/AddAccessory")
public class AddAccessory extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        HttpSession session = request.getSession();

        Long productId = Long.parseLong(request.getParameter("productId"));
        Long accessoryId = Long.parseLong(request.getParameter("accessoryId"));
        MySqlDataStoreUtilities.insertProductAccessory(productId, accessoryId);

        response.sendRedirect("ViewProduct?productId=" + productId);
    }

}
