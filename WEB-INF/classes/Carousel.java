/* This code is used to implement carousel feature in Website. Carousels are used to implement slide show feature. This  code is used to display 
all the accessories related to a particular product. This java code is getting called from cart.java. The product which is added to cart, all the 
accessories realated to product will get displayed in the carousel.*/


import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Carousel {

	public static String carouselFeature(Utilities utilities, Product product) {
		StringBuilder sb = new StringBuilder();
		String myCarousel = "myCarousels";


		sb.append("<div id='content'><div class='post'><h2 class='title meta'>");
		sb.append("<a style='font-size: 24px;'>" + product.getProductName() + " Accessories</a>");

		sb.append("</h2>");

		sb.append("<div class='container'>");
				/* Carousels require the use of an id (in this case id="myCarousel") for carousel controls to function properly.
				The .slide class adds a CSS transition and animation effect, which makes the items slide when showing a new item.
				Omit this class if you do not want this effect.
				The data-ride="carousel" attribute tells Bootstrap to begin animating the carousel immediately when the page loads.

				*/
		sb.append("<div class='carousel slide' id='" + myCarousel + "' data-ride='carousel'>");

				/*The slides are specified in a <div> with class .carousel-inner.
				The content of each slide is defined in a <div> with class .item. This can be text or images.
				The .active class needs to be added to one of the slides. Otherwise, the carousel will not be visible.
				*/
		sb.append("<div class='carousel-inner'>");


		int k = 0;

		List<Long> productAccessories = MySqlDataStoreUtilities.getProductAccessories(product.getProductId());
		System.out.println(productAccessories);
		for (Long accessoryId : productAccessories) {
			Product accessory = MySqlDataStoreUtilities.getProduct(accessoryId);
			if (k == 0) {
				sb.append("<div class='item active'><div class='col-md-6' style = 'background-color: #58acfa;border :1px solid #cfd1d3'>");
			} else {
				sb.append("<div class='item'><div class='col-md-6' style = 'background-color: #58acfa ;border :1px solid #cfd1d3' >");
			}
			sb.append("<div id='shop_item'>");
			sb.append("<h3>" + accessory.getProductName() + "</h3>");
			sb.append("<strong>" + accessory.getPrice() + "$</strong><ul>");
			sb.append("<li id='item'><img src='images/ProductImages/" + accessory.getImage() + "' alt='' /></li>");


			if (utilities.usertype() != null && UserType.StoreManager.equals(utilities.usertype())) {
				sb.append("<li><form method='get' action='EditProduct'>" +
						"<input type='hidden' name='productId' value='" + accessory.getProductId() + "'>" +
						"<input type='submit' class='btnbuy' value='Edit'></form></li>");

				sb.append("<li><form method='post' action='DeleteProduct'>" +
						"<input type='hidden' name='productId' value='" + accessory.getProductId() + "'>" +
						"<input type='submit' class='btnbuy' value='Delete'></form></li>");
				sb.append("<li><form method='get' action='ViewProduct'>" +
						"<input type='hidden' name='productId' value='" + accessory.getProductId() + "'>" +
						"<input type='submit' class='btnbuy' value='Add Accessories'></form></li>");
			} else {


				sb.append("<li><form method='post' action='Cart'>" +
						"<input type='hidden' name='productId' value='" + accessory.getProductId() + "'>" +
						"<input type='checkbox' id='warranty' name='warranty' value='warranty'>" +
						"<label for='vehicle1'>1 Year Warranty 25$</label>" +
						"<input type='submit' class='btnbuy' value='Buy Now'></form></li>");

				sb.append("<li><form method='get' action='ViewProduct'>" +
						"<input type='hidden' name='productId' value='" + accessory.getProductId() + "'>" +
						"<input type='submit' class='btnbuy' value='View Product'></form></li>");

				sb.append("<li><form method='get' action='WriteReview'>"+
						"<input type='hidden' name='productId' value='" + accessory.getProductId() + "'>" +
						"<input type='submit' value='WriteReview' class='btnreview'></form></li>");
				sb.append("<li><form method='get' action='ViewReview'>"+"<input type='hidden' name='name' value='"+product.getProductName()+"'>"+
						"<input type='hidden' name='productId' value='" + accessory.getProductId() + "'>" +
						"<input type='submit' value='ViewReview' class='btnreview'></form></li>");

			}

			sb.append("</ul></div></div>");
			sb.append("</div>");

			k++;

		}

		sb.append("</div>");
				/*		The "Left and right controls" part:
					This code adds "left" and "right" buttons that allows the user to go back and forth between the slides manually.
				The data-slide attribute accepts the keywords "prev" or "next", which alters the slide position relative to its current position.
				*/
		sb.append("<a class='left carousel-control' href='#" + myCarousel + "' data-slide='prev' style = 'width : 10% ;background-color:#D7e4ef; opacity :1'>" +
				"<span class='glyphicon glyphicon-chevron-left' style = 'color :red'></span>" +
				"<span class='sr-only'>Previous</span>" +
				"</a>");
		sb.append("<a class='right carousel-control' href='#" + myCarousel + "' data-slide='next' style = 'width : 10% ;background-color:#D7e4ef; opacity :1'>" +
				"<span class='glyphicon glyphicon-chevron-right' style = 'color :red'></span>" +

				"<span class='sr-only'>Next</span>" +
				"</a>");


		sb.append("</div>");

		sb.append("</div></div>");
		sb.append("</div>");

		return sb.toString();
	}
}
	