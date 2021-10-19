import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@WebServlet("/Registration")

public class Registration extends HttpServlet {
    private String error_msg;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        displayRegistration(request, response, pw, false);
    }

	/*   Username,Password,Usertype information are Obtained from HttpServletRequest variable and validates whether
		 the User Credential Already Exists or else User Details are Added to the Users HashMap */

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        Utilities utility = new Utilities(request, pw);

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String repassword = request.getParameter("repassword");
        Integer userAge = Integer.parseInt(request.getParameter("userAge"));
        String userGender = request.getParameter("userGender");
        String userOccupation = request.getParameter("userOccupation");
        UserType userType = UserType.Customer;

        if (!utility.isLoggedin()) {
            userType = UserType.getEnum(request.getParameter("usertype"));
        }


        if (!password.equals(repassword)) {
            error_msg = "Passwords doesn't match!";
        } else {

            //get the userdata from sql database to hashmap
            Map<String, User> hm = new HashMap<>();

            String message = MySqlDataStoreUtilities.getConnection();

            if (message.equals("Successfull")) {
                hm = MySqlDataStoreUtilities.selectUser();

                // if the user already exist show error that already exist

                if (hm.containsKey(username))
                    error_msg = "Username already exist as " + userType.toString();
                else {
				/*create a user object and store details into hashmap
				store the user hashmap into file  */

                    User user = new User(null, username, password, password, userType.toString(), userAge,
                            userGender, userOccupation);
                    hm.put(username, user);
                    MySqlDataStoreUtilities.insertUser(username, password, repassword, userType.toString(),
                            userAge, userGender, userOccupation);
                    HttpSession session = request.getSession(true);

                    if (!utility.isLoggedin()) {
                        session.setAttribute("login_msg", "Your " + userType.toString() + " account has been created. Please login");
                        response.sendRedirect("Login");
                        return;
                    } else {
                        response.sendRedirect("CustomerCreated");
                        return;
                    }
                }

            } else {
                error_msg = "MySql server is not up and running";
            }
        }
        displayRegistration(request, response, pw, true);

    }

    /*  displayRegistration function displays the Registration page of New User */

    protected void displayRegistration(HttpServletRequest request,
                                       HttpServletResponse response, PrintWriter pw, boolean error)
            throws ServletException, IOException {
        Utilities utility = new Utilities(request, pw);
        utility.printHtml("Header.html");
        pw.print("<div class='post' style='float: none; width: 100%'>");
        pw.print("<h2 class='title meta'><a style='font-size: 24px;'>Login</a></h2>"
                + "<div class='entry'>"
                + "<div style='width:100%; margin:25px; margin-left: auto;margin-right: auto;'>");

        if (error) {
            pw.print("<h4 style='color:red'>" + error_msg + "</h4>");
        }

        pw.print("<form method='post' action='Registration'>" +
                "<table style='width:100%'>" +

                "<tr><td>" +
                "<h3>Username</h3></td><td><input type='text' name='username' value='' class='input' required></input>" +
                "</td></tr>");

        pw.print("<tr>");
        pw.print("<td>");
        pw.print("<h3>User Age</h3>");
        pw.print("</td>");
        pw.print("<td class='rightDataTable'>");
        pw.print("<input style='radius: 20px; width: 100%;' type='number' placeholder='Enter User Age' name='userAge' required");
        pw.print("</td>");
        pw.print("</tr>");

        pw.print("<tr>");
        pw.print("<td>");
        pw.print("<h3>User Gender</h3>");
        pw.print("</td>");
        pw.print("<td>");
        pw.print("<input type='radio' id='male' name='userGender' value='male' checked><label for='male'>Male</label><br>" +
                "<input type='radio' id='female' name='userGender' value='female'><label for='female'>Female</label>");
        pw.print("</td>");
        pw.print("</tr>");

        pw.print("<tr>");
        pw.print("<td>");
        pw.print("<h3>User Occupation</h3>");
        pw.print("</td>");
        pw.print("<td>");
        pw.print("<input style='radius: 20px; width: 100%;' type='text' placeholder='Enter User Occupation' name='userOccupation' required");
        pw.print("</td>");
        pw.print("</tr>");

        pw.print("<tr><td>"
                + "<h3>Password</h3></td><td><input type='password' name='password' value='' class='input' required></input>"
                + "</td></tr>" +
                "<tr><td>"
                + "<h3>Re-Password</h3></td><td><input type='password' name='repassword' value='' class='input' required></input>"
                + "</td></tr>" +
                "<tr><td>"
                + "<h3>User Type</h3></td><td><select name='usertype' class='input'><option value='" + UserType.Customer.toString() + "' selected>Customer</option><option value='" + UserType.StoreManager.toString() + "'>Store Manager</option><option value='" + UserType.SalesMan.toString() + "'>Salesman</option></select>"
                + "</td></tr></table>"
                + "<input type='submit' class='btnbuy' name='ByUser' value='Create User' style='float: right;height: 20px margin: 20px; margin-right: 10px;'></input>"
                + "</form>" + "</div></div></div>");
        utility.printHtml("Footer.html");
    }
}
