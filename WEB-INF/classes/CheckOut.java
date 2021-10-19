import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/CheckOut")

//once the user clicks buy now button page is redirected to checkout page where user has to give checkout information

public class CheckOut extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		storeOrders(request, response);
	}

	protected void storeOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try
		{
			response.setContentType("text/html");
			PrintWriter pw = response.getWriter();
			Utilities utility = new Utilities(request,pw);
			if(!utility.isLoggedin())
			{
				HttpSession session = request.getSession(true);
				session.setAttribute("login_msg", "Please Login to add items to cart");
				response.sendRedirect("Login");
				return;
			}
			HttpSession session=request.getSession();

			//get the order product details	on clicking submit the form will be passed to submitorder page

			String userName = session.getAttribute("username").toString();
			String orderTotal = request.getParameter("orderTotal");
			String orderTotalRebate = request.getParameter("orderTotalRebate");

			Customer customer = MySqlDataStoreUtilities.getCustomerByUserId(utility.getUserId());

			utility.printHtml("Header.html");
			utility.printHtml("LeftNavigationBar.html");
			pw.print("<form name ='CheckOut' action='Payment' method='post'>");
			pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
			pw.print("<a style='font-size: 24px;'>Order</a>");
			pw.print("</h2><div class='entry'>");
			pw.print("<table  class='gridtable'>" +
					"<tr class='rowTable'>" +
					"<td class='leftDataTable'>User Id:</td>" +
					"<td>" +userName +"</td>" +
					"</tr>");

			for (OrderItem oi : utility.getCustomerOrders())
			{
				pw.print("<tr class='rowTable'><td class='leftDataTable'> Product Purchased:</td><td class='rightDataTable'>");
				pw.print(oi.getName()+"</td></tr><tr class='rowTable'><td class='leftDataTable'>");
				pw.print("<input type='hidden' name='orderPrice' value='"+oi.getPrice()+"'>");
				pw.print("<input type='hidden' name='orderName' value='"+oi.getName()+"'>");
				pw.print("Product & Warranty Price:</td><td class='rightDataTable'>$"+ oi.getPrice() + " | $" + (oi.isWarrantyIncluded()? 25 : 0));
				pw.print("</td></tr>");

			}

			pw.print("<tr class='rowTable'>");
			pw.print("<td class='leftDataTable'>Total Order Rebate:</td>");
			pw.print("<td class='rightDataTable'>$"+orderTotalRebate + "<input type='hidden' name='orderTotalRebate' value='"+orderTotalRebate+"'>");
			pw.print("</td></tr>");

			pw.print("<tr class='rowTable'>");
			pw.print("<td class='leftDataTable'>Total Order Cost</td><td class='rightDataTable'>$"+orderTotal);
			pw.print("<input type='hidden' name='orderTotal' value='"+orderTotal+"'>");
			pw.print("</td></tr>");

			if(UserType.SalesMan.equals(utility.usertype())) {
				pw.print("<tr class='rowTable'>");
				pw.print("<td class='leftDataTable'>");
				pw.print("Select UserName:");
				pw.print("</td>");
				pw.print("<td class='rightDataTable'>");
				pw.print("<select style='radius: 20px; width: 100%;' name='userId' id='userId'>");
				for(User user: utility.getUsers().values()) {
					pw.print("<option value='" + user.getUserId() + "'>" + user.getUserName() + "</option>");
				}
				pw.print("</td>");
				pw.print("</tr>");
			}

			pw.print("<tr class='rowTable'><td class='leftDataTable'>");
			pw.print("First Name:");
			pw.print("</td>");
			pw.print("<td class='rightDataTable'>");
			pw.print("<input type='text' name='firstName' value='" + (customer != null? customer.getFirstName() : "") + "' required/>");
			pw.print("</td></tr>");

			pw.print("<tr class='rowTable'><td class='leftDataTable'>");
			pw.print("Last Name:");
			pw.print("</td>");
			pw.print("<td class='rightDataTable'>");
			pw.print("<input type='text' name='lastName' value='" + (customer != null? customer.getLastName() : "") + "' required/>");
			pw.print("</td></tr>");

			pw.print("<tr class='rowTable'><td class='leftDataTable'>");
			pw.print("Address 1:");
			pw.print("</td>");
			pw.print("<td class='rightDataTable'>");
			pw.print("<input type='text' name='address1' value='" + (customer != null? customer.getAddress1() : "") + "' required/>");
			pw.print("</td></tr>");

			pw.print("<tr class='rowTable'><td class='leftDataTable'>");
			pw.print("Address 2:");
			pw.print("</td>");
			pw.print("<td class='rightDataTable'>");
			pw.print("<input type='text' name='address2' value='" + (customer != null? customer.getAddress2() : "") + "' />");
			pw.print("</td></tr>");

			pw.print("<tr class='rowTable'><td class='leftDataTable'>");
			pw.print("City:");
			pw.print("</td>");
			pw.print("<td class='rightDataTable'>");
			pw.print("<input type='text' name='city' value='" + (customer != null? customer.getCity() : "") + "' required/>");
			pw.print("</td></tr>");

			pw.print("<tr class='rowTable'><td class='leftDataTable'>");
			pw.print("State:");
			pw.print("</td>");
			pw.print("<td class='rightDataTable'>");
			pw.print("<input type='text' name='state' value='" + (customer != null? customer.getAddress1() : "") + "' required/>");
			pw.print("</td></tr>");

			pw.print("<tr class='rowTable'><td class='leftDataTable'>");
			pw.print("Zip Code:");
			pw.print("</td>");
			pw.print("<td class='rightDataTable'>");
			pw.print("<input type='number' name='zipCode' value='" + (customer != null? customer.getZipcode() : "") + "' required/>");
			pw.print("</td></tr>");

			pw.print("<tr class='rowTable'><td class='leftDataTable'>");
			pw.print("Phone:");
			pw.print("</td>");
			pw.print("<td class='rightDataTable'>");
			pw.print("<input type='text' name='phone' value='" + (customer != null? customer.getPhone() : "") + "' required/>");
			pw.print("</td></tr>");

			pw.print("<tr class='rowTable'><td class='leftDataTable'>");
			pw.print("Delivery Option:</td>");
			pw.print("<td>");
			pw.print("<input type='radio' id='homeDelivery' name='order' value='homeDelivery' checked><label for='homeDelivery'>Delivery</label><br>");
			pw.print("<input type='radio' id='storePickup' name='order' value='storePickup'><label for='storePickup'>Pickup</label>");
			pw.print("</td></tr>");

			pw.print("<tr class='rowTable'><td class='leftDataTable'>");
			pw.print("In Store Pickup Location:</td>");
			pw.print("<td class='rightDataTable'><select name='storePickupLocation' id='storePickupLocation'>");

			for(StoreLocation storeLocation: MySqlDataStoreUtilities.getStoreLocations()) {
				pw.print("<option value='" + storeLocation.getStoreLocationId() + "'>" + storeLocation.getStreetAddress() + " " + storeLocation.getZipCode() + "</option>");
			}

			pw.print("</select>");
			pw.print("</td></tr>");

			pw.print("<tr class='rowTable'><td class='leftDataTable'>");
			pw.print("Credit/accountNo</td>");
			pw.print("<td class='rightDataTable'><input type='text' name='creditCardNo'>");
			pw.print("</td></tr>");

			pw.print("<tr class='rowTable'><td colspan='2'>");
			pw.print("<input type='submit' name='submit' class='btnbuy'>");
			pw.print("</td></tr></table></form>");
			pw.print("</div></div></div>");
			utility.printHtml("Footer.html");
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("text/html");
	}
}
