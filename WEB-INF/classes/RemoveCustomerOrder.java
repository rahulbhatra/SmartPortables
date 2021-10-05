import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/RemoveCustomerOrder")
public class RemoveCustomerOrder extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        Utilities utility = new Utilities(request, pw);
        if (!utility.isLoggedin() && ! UserType.SalesMan.equals(utility.usertype())) {
            HttpSession session = request.getSession(true);
            session.setAttribute("login_msg", "Please Login As Salesman to Remove Customer Order from cart");
            response.sendRedirect("Login");
            return;
        }

        Long orderId = Long.parseLong(request.getParameter("orderId"));
        String userName = request.getParameter("customerName");
        String orderName = request.getParameter("orderName");
        utility.deletePayment(orderId);
        String customerName = request.getParameter("customerName");

        System.out.println("--------- Inside Remove Customer Order ----------" + customerName);


        response.sendRedirect("ViewCustomerOrder?customerName=" + customerName);
    }

}