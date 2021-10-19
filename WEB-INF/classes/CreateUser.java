import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/CreateUser")
public class CreateUser extends HttpServlet {
    private String error_msg;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        Utilities utility = new Utilities(request, pw);

        System.out.println("----------------- Inside Create User Get ----------------");
        displayContainer(request, response, utility, pw, false, null);

    }

    private void displayContainer(HttpServletRequest request, HttpServletResponse response, Utilities utility, PrintWriter pw, boolean error, String msg) {
        utility.printHtml("Header.html");

        pw.print("<div class='post' style='float: none; width: 100%'>");
        pw.print("<h2 class='title meta'><a style='font-size: 24px;'>Create New User</a></h2>"
                + "<div class='entry'>"
                + "<div style='width:100%; margin:25px; margin-left: auto;margin-right: auto;'>");

        pw.print("<div class='content'>");

//        String error_msg = (String) request.getAttribute("error_msg");
        pw.print("<section class='createContent'>");

        if (error) {
            pw.print("<h4 style='color:red; text-align:center;'>" + msg + "</h4>");
        }

        pw.print("<form action='CreateUser' method='post'>");

        pw.print("<table style='width: 100%'>");

        pw.print("<tr class='rowTable'>");
        pw.print("<td class='leftDataTable'>");
        pw.print("User Name:");
        pw.print("</td>");
        pw.print("<td class='rightDataTable'>");
        pw.print("<input style='radius: 20px; width: 100%;' type='text' placeholder='Enter User Name' name='userName' required");
        pw.print("</td>");
        pw.print("</tr>");

        pw.print("<tr class='rowTable'>");
        pw.print("<td class='leftDataTable'>");
        pw.print("User Age:");
        pw.print("</td>");
        pw.print("<td class='rightDataTable'>");
        pw.print("<input style='radius: 20px; width: 100%;' type='number' placeholder='Enter User Age' name='userAge' required");
        pw.print("</td>");
        pw.print("</tr>");

        pw.print("<tr class='rowTable'>");
        pw.print("<td class='leftDataTable'>");
        pw.print("User Gender:");
        pw.print("</td>");
        pw.print("<td class='rightDataTable'>");
        pw.print("<input type='radio' id='male' name='userGender' value='male' checked><label for='male'>Male</label><br>" +
                 "<input type='radio' id='female' name='userGender' value='female'><label for='female'>Female</label>");
        pw.print("</td>");
        pw.print("</tr>");

        pw.print("<tr class='rowTable'>");
        pw.print("<td class='leftDataTable'>");
        pw.print("User Occupation:");
        pw.print("</td>");
        pw.print("<td class='rightDataTable'>");
        pw.print("<input style='radius: 20px; width: 100%;' type='text' placeholder='Enter User Occupation' name='userOccupation' required");
        pw.print("</td>");
        pw.print("</tr>");

        pw.print("<tr class='rowTable'>");
        pw.print("<td class='leftDataTable'>");
        pw.print("Password:");
        pw.print("</td>");
        pw.print("<td class='rightDataTable'>");
        pw.print("<input style='radius: 20px; width: 100%;' type='password' placeholder='Enter User Password' name='password' required");
        pw.print("</td>");
        pw.print("</tr>");

        pw.print("<tr class='rowTable'>");
        pw.print("<td class='leftDataTable'>");
        pw.print("RePassword:");
        pw.print("</td>");
        pw.print("<td class='rightDataTable'>");
        pw.print("<input style='radius: 20px; width: 100%;' type='password' placeholder='Enter User RePassword' name='rePassword' required");
        pw.print("</td>");
        pw.print("</tr>");

        pw.print("</table>");

        pw.print("<div class='buttonContainer'><button class='classicButton' type='submit' value='Submit'>Create User</button></div>");

        pw.print("</form>");

        pw.print("</section>");
        pw.print("</div>");

        pw.print("</div>");
        pw.print("</div>");
        pw.print("</div>");
        utility.printHtml("Footer.html");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("----------------- Inside Create User Post ----------------");
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();

        Utilities utilities = new Utilities(request, pw);
        String username = request.getParameter("userName");
        String password = request.getParameter("password");
        Integer userAge = Integer.parseInt(request.getParameter("userAge"));
        String userGender = request.getParameter("userGender");
        String userOccupation = request.getParameter("userOccupation");
        UserType usertype = UserType.Customer;

        System.out.println(username + " " + password + " " + usertype);


        //get the userdata from sql database to hashmap
        Map<String, User> hm = new HashMap<>();

        String message = MySqlDataStoreUtilities.getConnection();

        if (message.equals("Successfull")) {
            hm = MySqlDataStoreUtilities.selectUser();

            // if the user already exist show error that already exist

            if (hm.containsKey(username))
                error_msg = "Username already exist as " + usertype.toString();
            else {
				/*create a user object and store details into hashmap
				store the user hashmap into file  */

                User user = new User(null, username, password, password, usertype.toString(),
                        userAge, userGender, userOccupation);
                hm.put(username, user);
                MySqlDataStoreUtilities.insertUser(username, password, password, usertype.toString(), userAge,
                        userGender, userOccupation);
                HttpSession session = request.getSession(true);

                if (!utilities.isLoggedin()) {
                    session.setAttribute("login_msg", "Your " + usertype.toString() + " account has been created. Please login");
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


        displayContainer(request, response, utilities, pw, true, message);
    }

}
