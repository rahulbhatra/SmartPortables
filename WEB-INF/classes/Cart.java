import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/Cart")

public class Cart extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();


		/* From the HttpServletRequest variable name,type,maker and acessories information are obtained.*/

		Utilities utility = new Utilities(request, pw);
		Long productId = Long.parseLong(request.getParameter("productId"));
		String warranty = request.getParameter("warranty");
		boolean warrantyIncluded = "warranty".equalsIgnoreCase(warranty) ? true : false;


		/* StoreProduct Function stores the Purchased product in Orders HashMap.*/

		utility.storeProduct(productId ,warrantyIncluded);
		displayCart(request, response);
	}


	/* displayCart Function shows the products that users has bought, these products will be displayed with Total Amount.*/

	protected void displayCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Utilities utility = new Utilities(request, pw);
		Carousel carousel = new Carousel();
		if (!utility.isLoggedin()) {
			HttpSession session = request.getSession(true);
			session.setAttribute("login_msg", "Please Login to add items to cart");
			response.sendRedirect("Login");
			return;
		}

		utility.printHtml("Header.html");
		utility.printHtml("LeftNavigationBar.html");
		pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
		pw.print("<a style='font-size: 24px;'>Cart(" + utility.CartCount() + ")</a>");
		pw.print("</h2><div class='entry'>");
		if (utility.CartCount() > 0) {
			pw.print("<table  style='width: 100%' class='gridtable'>");
			pw.print("<tr>");
			pw.print("<th>Number</th>");
			pw.print("<th>Order Name</th>");
			pw.print("<th>Order Price</th>");
			pw.print("<th>Retailer Discount</th>");
			pw.print("<th>Manufacturer Rebate</th>");
			pw.print("<th>Warranty Price</th>");
			pw.print("<th>Operation</th>");
			pw.print("</tr>");
			int i = 1;
			double total = 0;
			double totalRebate = 0;
			for (OrderItem orderItem : utility.getCustomerOrders()) {
				pw.print("<tr>");
				pw.print("<td>" + i + ".</td>");
				pw.print("<td>" + orderItem.getName() + "</td><td> " + orderItem.getPrice() + "</td>");
				pw.print("<td>" + (orderItem.getDiscount()) + "</td>");
				pw.print("<td>" + (orderItem.getRebate()) + "</td>");
				pw.print("<td>" + (orderItem.isWarrantyIncluded() ? 25: 0) + "</td>");
				pw.print("<td class='buttonContainer'>" +
						"<form style='margin:auto' action='RemoveOrder' method='post'>" +
						"<input type='hidden' name='orderIndex' value='" + (i - 1) + "'>" +
						"<button class='mainButton' type='submit' value='submit'>Remove</button>" +
						"</form>" +
						"</td>");
				pw.print("</tr>");

				total = total + orderItem.getPrice() + (orderItem.isWarrantyIncluded() ? 25: 0) - orderItem.getDiscount();
				totalRebate += orderItem.getRebate();
				i++;
			}
			pw.print("<input type='hidden' name='orderTotal' value='" + total + "'>");
			pw.print("<input type='hidden' name='orderTotalRebate' value='" + totalRebate + "'>");
			pw.print("<tr><th></th><th>Total</th><th>" + total + "</th></tr>");
//			pw.print("<tr><td></td><td></td>");
			pw.print("</table>");

			pw.print("<form name ='Cart' action='CheckOut' method='post'>");
			pw.print("<input type='hidden' name='orderTotal' value='" + total + "'>");
			pw.print("<input type='hidden' name='orderTotalRebate' value='" + totalRebate + "'>");
			pw.print("<input type='submit' name='CheckOut' value='CheckOut' class='btnbuy' />");
			pw.print("</form>");
			/* This code is calling Carousel.java code to implement carousel feature*/
			pw.print(carousel.carouselfeature(utility));
		} else {
			pw.print("<h4 style='color:red'>Your Cart is empty</h4>");
		}
		pw.print("</div></div></div>");
		utility.printHtml("Footer.html");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Utilities utility = new Utilities(request, pw);

		displayCart(request, response);
	}
}
