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

			utility.printHtml("Header.html");
			utility.printHtml("LeftNavigationBar.html");
			pw.print("<form name ='CheckOut' action='Payment' method='post'>");
			pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
			pw.print("<a style='font-size: 24px;'>Order</a>");
			pw.print("</h2><div class='entry'>");
			pw.print("<table  class='gridtable'>" + "<tr><td>Customer Name:</td><td>");
			pw.print(userName);
			pw.print("</td></tr>");

			for (OrderItem oi : utility.getCustomerOrders())
			{
				pw.print("<tr><td> Product Purchased:</td><td>");
				pw.print(oi.getName()+"</td></tr><tr><td>");
				pw.print("<input type='hidden' name='orderPrice' value='"+oi.getPrice()+"'>");
				pw.print("<input type='hidden' name='orderName' value='"+oi.getName()+"'>");
				pw.print("Product & Warranty Price:</td><td>$"+ oi.getPrice() + " | $" + (oi.isWarrantyIncluded()? 25 : 0));
				pw.print("</td></tr>");

			}

			pw.print("<tr><td>");
			pw.print("Total Order Rebate:</td><td>$"+orderTotalRebate);
			pw.print("<input type='hidden' name='orderTotalRebate' value='"+orderTotalRebate+"'>");
			pw.print("</td></tr>");

			pw.print("<tr><td>");
			pw.print("Total Order Cost</td><td>$"+orderTotal);
			pw.print("<input type='hidden' name='orderTotal' value='"+orderTotal+"'>");
			pw.print("</td></tr></table><table><tr></tr><tr></tr>");

			if(UserType.SalesMan.equals(utility.usertype())) {
				pw.print("<tr class='createContentRow'>");
				pw.print("<td>");
				pw.print("Select UserName:");
				pw.print("</td>");
				pw.print("<td class='createContentInput'>");
				pw.print("<select style='radius: 20px; width: 100%;' name='customerName' id='customerName'>");
				for(User user: utility.getUsers().values()) {
					pw.print("<option value='" + user.getUserName() + "'>" + user.getUserName() + "</option>");
				}
				pw.print("</td>");
				pw.print("</tr>");
			}

			pw.print("<tr><td>");
			pw.print("First Name:");
			pw.print("</td>");
			pw.print("<td>");
			pw.print("<input type='text' name='firstName' />");
			pw.print("</td></tr>");

			pw.print("<tr><td>");
			pw.print("Last Name:");
			pw.print("</td>");
			pw.print("<td>");
			pw.print("<input type='text' name='lastName' />");
			pw.print("</td></tr>");

			pw.print("<tr><td>");
			pw.print("Address 1:");
			pw.print("</td>");
			pw.print("<td>");
			pw.print("<input type='text' name='address1' />");
			pw.print("</td></tr>");

			pw.print("<tr><td>");
			pw.print("Address 2:");
			pw.print("</td>");
			pw.print("<td>");
			pw.print("<input type='text' name='address2' />");
			pw.print("</td></tr>");

			pw.print("<tr><td>");
			pw.print("City:");
			pw.print("</td>");
			pw.print("<td>");
			pw.print("<input type='text' name='city' />");
			pw.print("</td></tr>");

			pw.print("<tr><td>");
			pw.print("State:");
			pw.print("</td>");
			pw.print("<td>");
			pw.print("<input type='text' name='state' />");
			pw.print("</td></tr>");

			pw.print("<tr><td>");
			pw.print("Zip Code:");
			pw.print("</td>");
			pw.print("<td>");
			pw.print("<input type='text' name='zipCode' />");
			pw.print("</td></tr>");

			pw.print("<tr><td>");
			pw.print("Phone:");
			pw.print("</td>");
			pw.print("<td>");
			pw.print("<input type='text' name='phone' />");
			pw.print("</td></tr>");

			pw.print("<tr><td>");
			pw.print("Delivery Option:</td>");
			pw.print("<td>");
			pw.print("<input type='radio' id='homeDelivery' name='order' value='homeDelivery' checked><label for='homeDelivery'>Delivery</label></td>");
			pw.print("<td><input type='radio' id='storePickup' name='order' value='storePickup'><label for='storePickup'>Pickup</label>");
			pw.print("</td></tr>");

			pw.print("<tr><td>");
			pw.print("In Store Pickup Location:</td>");
			pw.print("<td><select name='storePickupLocation' id='storePickupLocation'>");

			for(StoreLocation storeLocation: MySqlDataStoreUtilities.getStoreLocations()) {
				pw.print("<option value='" + storeLocation.getStoreLocationId() + "'>" + storeLocation.getStreetAddress() + " " + storeLocation.getZipCode() + "</option>");
			}

			pw.print("</select>");
			pw.print("</td></tr>");

			pw.print("<tr><td>");
			pw.print("Credit/accountNo</td>");
			pw.print("<td><input type='text' name='creditCardNo'>");
			pw.print("</td></tr>");

			pw.print("<tr><td colspan='2'>");
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
