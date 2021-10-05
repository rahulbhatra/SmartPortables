import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.ArrayList;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;

@WebServlet("/Utilities")

/* 
	Utilities class contains class variables of type HttpServletRequest, PrintWriter,String and HttpSession.

	Utilities class has a constructor with  HttpServletRequest, PrintWriter variables.
	  
*/

public class Utilities extends HttpServlet {
    HttpServletRequest req;
    PrintWriter pw;
    String url;
    HttpSession session;

    public Utilities(HttpServletRequest req, PrintWriter pw) {
        this.req = req;
        this.pw = pw;
        this.url = this.getFullURL();
        this.session = req.getSession(true);
    }

    public void printHtml(String file) {
        String result = HtmlToString(file);
        //to print the right navigation in header of username cart and logout etc
        if (file == "Header.html") {
            result = result + "<div id='menu' style='float: right;'><ul>";
            if (session.getAttribute("username") != null) {
                String username = session.getAttribute("username").toString();
                UserType userType = UserType.getEnum(session.getAttribute("usertype").toString());
                System.out.println("--------------------------------" + userType + " --------------------------- ");
                username = Character.toUpperCase(username.charAt(0)) + username.substring(1);
                result += "<li><a><span class='glyphicon'>Hello," + username + "</span></a></li>";
                switch (userType) {
                    case Customer:
                        result += "<li><a href='ViewOrder'><span class='glyphicon'>ViewOrder</span></a></li>";
                        result += "<li><a href='Account'><span class='glyphicon'>Account</span></a></li>";
                        break;
                    case StoreManager:
                        result += "<li><a href='CreateProduct'><span class='glyphicon'>CreateProduct</span></a></li>"
                                + "<li><a href='DataVisualization'><span class='glyphicon'>Trending</span></a></li>"
                                + "<li><a href='DataAnalytics'><span class='glyphicon'>DataAnalytics</span></a></li>";
                        break;
                    case SalesMan:
                        result += "<li><a href='CreateUser'><span class='glyphicon'>CreateUser</span></a></li>";
                        result += "<li><a href='ViewCustomerOrder'><span class='glyphicon'>View Orders</span></a></li>";
                        break;
                }

                result += "<li><a href='Logout'><span class='glyphicon'>Logout</span></a></li>";
            } else {

                result += "<li><a href='ViewOrder'><span class='glyphicon'>View Order</span></a></li>";
                result += "<li><a href='Login'><span class='glyphicon'>Login</span></a></li>";
            }
            result = result + "<li><a href='Cart'><span class='glyphicon'>Cart(" + CartCount() + ")</span></a></li></ul></div></div><div id='page'>";
            pw.print(result);
        } else
            pw.print(result);
    }


    /*  getFullURL Function - Reconstructs the URL user request  */

    public String getFullURL() {
        String scheme = req.getScheme();
        String serverName = req.getServerName();
        int serverPort = req.getServerPort();
        String contextPath = req.getContextPath();
        StringBuffer url = new StringBuffer();
        url.append(scheme).append("://").append(serverName);

        if ((serverPort != 80) && (serverPort != 443)) {
            url.append(":").append(serverPort);
        }
        url.append(contextPath);
        url.append("/");
        return url.toString();
    }

    /*  HtmlToString - Gets the Html file and Converts into String and returns the String.*/
    public String HtmlToString(String file) {
        String result = null;
        try {
            String webPage = url + file;
            URL url = new URL(webPage);
            URLConnection urlConnection = url.openConnection();
            InputStream is = urlConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);

            int numCharsRead;
            char[] charArray = new char[1024];
            StringBuffer sb = new StringBuffer();
            while ((numCharsRead = isr.read(charArray)) > 0) {
                sb.append(charArray, 0, numCharsRead);
            }
            result = sb.toString();
        } catch (Exception e) {
        }
        return result;
    }

    /*  logout Function removes the username , usertype attributes from the session variable*/

    public void logout() {
        session.removeAttribute("username");
        session.removeAttribute("usertype");
    }

    /*  logout Function checks whether the user is loggedIn or Not*/

    public boolean isLoggedin() {
        if (session.getAttribute("username") == null)
            return false;
        return true;
    }

    /*  username Function returns the username from the session variable.*/

    public String username() {
        if (session.getAttribute("username") != null)
            return session.getAttribute("username").toString();
        return null;
    }

    public Long getUserId() {
        if (session.getAttribute("userId") != null)
            return Long.parseLong(session.getAttribute("userId").toString());
        return null;
    }

    /*  usertype Function returns the usertype from the session variable.*/
    public UserType usertype() {
        if (session.getAttribute("usertype") != null)
            return UserType.getEnum(session.getAttribute("usertype").toString());
        return null;
    }

    /*  getUser Function checks the user is a customer or retailer or manager and returns the user class variable.*/
    public User getUser() {
        UserType usertype = usertype();
        Map<String, User> hm = new HashMap<>();
        try {
            hm = MySqlDataStoreUtilities.selectUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hm.get(username());
    }

    public Map<String, User> getUsers() {
        Map<String, User> hm = new HashMap<>();
        try {
            hm = MySqlDataStoreUtilities.selectUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hm;
    }

    /*  getCustomerOrders Function gets  the Orders for the user*/
    public ArrayList<OrderItem> getCustomerOrders() {
        ArrayList<OrderItem> order = new ArrayList<OrderItem>();
        if (OrdersHashMap.orders.containsKey(username()))
            order = OrdersHashMap.orders.get(username());
        return order;
    }

    /*  getOrdersPaymentSize Function gets  the size of OrderPayment */
    public int getOrderPaymentSize() {

        HashMap<Integer, ArrayList<CustomerOrder>> orderPayments = new HashMap<Integer, ArrayList<CustomerOrder>>();
        int size = 0;
        try {
            orderPayments = MySqlDataStoreUtilities.selectCustomerOrders();

        } catch (Exception e) {

        }
        for (Map.Entry<Integer, ArrayList<CustomerOrder>> entry : orderPayments.entrySet()) {
            size = entry.getKey();
        }

        return size;
    }

    public CustomerOrder getCustomerOrder(Long customerOrderId) {
        return MySqlDataStoreUtilities.getCustomerOrder(customerOrderId);
    }

    public Map<Integer, ArrayList<CustomerOrder>> getOrderPayments() {
        Map<Integer, ArrayList<CustomerOrder>> orderPayments = new HashMap<Integer, ArrayList<CustomerOrder>>();
        try {
            orderPayments = MySqlDataStoreUtilities.selectCustomerOrders();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderPayments;
    }

    /*  CartCount Function gets  the size of User Orders*/
    public int CartCount() {
        if (isLoggedin())
            return getCustomerOrders().size();
        return 0;
    }

    /* StoreProduct Function stores the Purchased product in Orders HashMap according to the User Names.*/

    public void storeProduct(Long productId, boolean warrantyIncluded) {
        if (!OrdersHashMap.orders.containsKey(username())) {
            ArrayList<OrderItem> arr = new ArrayList<OrderItem>();
            OrdersHashMap.orders.put(username(), arr);
        }
        ArrayList<OrderItem> orderItems = OrdersHashMap.orders.get(username());
        Product product = MySqlDataStoreUtilities.getProduct(productId);

        OrderItem orderitem = new OrderItem(product.getName(), product.getPrice(), product.getImage(),
                product.getManufacturer(), warrantyIncluded, product.getDiscount(), product.getRebate());
        orderItems.add(orderitem);

    }

    // store the payment details for orders
    public void storePayment(CustomerOrder customerOrder) {
        try {
            MySqlDataStoreUtilities.insertCustomerOrder(customerOrder);
        } catch (Exception e) {
            System.out.println("inside exception file not written properly");
        }
    }


    public void updatePayment(CustomerOrder customerOrder) {
        try {
            System.out.println("do nothing");
        } catch (Exception e) {
            System.out.println("inside exception file not written properly");
        }
    }

    public void deletePayment(Long customerOrderId) {
        try {
            MySqlDataStoreUtilities.deleteCustomerOrder(customerOrderId);
        } catch (Exception e) {
            System.out.println("inside exception file not written properly");
        }
    }

    public String storeReview(String productname, String producttype, String productmaker, String reviewrating, String reviewdate, String reviewtext, String reatilerpin, String price, String city) {
        String message = MongoDBDataStoreUtilities.insertReview(productname, username(), producttype, productmaker, reviewrating, reviewdate, reviewtext, reatilerpin, price, city);
        if (!message.equals("Successfull")) {
            return "UnSuccessfull";
        } else {
            HashMap<String, ArrayList<Review>> reviews = new HashMap<String, ArrayList<Review>>();
            try {
                reviews = MongoDBDataStoreUtilities.selectReview();
            } catch (Exception e) {

            }
            if (reviews == null) {
                reviews = new HashMap<>();
            }
            // if there exist product review already add it into same list for productname or create a new record with product name

            if (!reviews.containsKey(productname)) {
                ArrayList<Review> arr = new ArrayList<Review>();
                reviews.put(productname, arr);
            }
            ArrayList<Review> listReview = reviews.get(productname);
            Review review = new Review(productname, username(), producttype, productmaker, reviewrating, reviewdate, reviewtext, reatilerpin, price, city);
            listReview.add(review);

            // add Reviews into database

            return "Successfull";
        }
    }

//    public ArrayList<String> getProducts() {
//        ArrayList<String> ar = new ArrayList<String>();
//        for (Map.Entry<String, Product> entry : getWearableTechnologies().entrySet()) {
//            ar.add(entry.getValue().getName());
//        }
//        return ar;
//    }

    /* getProducts Functions returns the Arraylist of games in the store.*/

//    public ArrayList<String> getProductsGame() {
//        ArrayList<String> ar = new ArrayList<String>();
//        for (Map.Entry<String, Product> entry : getPhones().entrySet()) {
//            ar.add(entry.getValue().getName());
//        }
//        return ar;
//    }

    /* getProducts Functions returns the Arraylist of Tablets in the store.*/

//    public ArrayList<String> getProductsTablets() {
//        ArrayList<String> ar = new ArrayList<String>();
//        for (Map.Entry<String, Product> entry : getLaptops().entrySet()) {
//            ar.add(entry.getValue().getName());
//        }
//        return ar;
//    }

    public void fileUpload(Part filePart, String fileName, String productCategory) throws IOException {
        String TOMCAT_HOME = System.getProperty("catalina.home");
        String fileLocation = TOMCAT_HOME + "/webapps/Tutorial_1/images";

        switch (productCategory.toLowerCase()) {
            case "wearabletechnology":
                fileLocation += "/wearableTechnology";
                break;
            case "laptop":
                fileLocation += "/laptop";
                break;
            case "phone":
                fileLocation += "/phone";
                break;
            case "voiceassistant":
                fileLocation += "/voiceAssistant";
                break;
        }

        OutputStream out = null;
        InputStream filecontent = null;

        try {
            out = new FileOutputStream(new File(fileLocation + File.separator
                    + fileName));
            filecontent = filePart.getInputStream();
            int read = 0;
            final byte[] bytes = new byte[1024];

            while ((read = filecontent.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
        } catch (FileNotFoundException fne) {
            fne.getStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
            if (filecontent != null) {
                filecontent.close();
            }
        }
    }

    public List<String> getProductCategories() {
        String[] array = {"WearableTechnology", "Phone", "Laptop", "VoiceAssistant"};
        return Arrays.asList(array);
    }

    public List<String> getProductCondition() {
        String[] array = {"New", "Old"};
        return Arrays.asList(array);
    }

    public List<String> getProductManufacturers() {
        String[] array = {"FitnessWatches", "SmartWatches", "HeadPhones", "VirtualReality", "PetTracker",
                "Apple", "Samsung", "Motorola", "Google", "OnePlus", "MicroSoft", "Dell", "HP", "Lenovo", "Navi", "Rufus",
                "Amazon"};
        return Arrays.asList(array);
    }

    public List<String> getPickUpLocations() {

        String[] array = {"None", "Albany 60625", "Ashburn 60652", "Back of the Yards 60609",
                "Beverly View 60620", "BridgePort 60609", "Chinatown 60616",
                "Cottage Grove Heights 60638", "DePaul 60614",
                "Dunning 60634", "Fuller Park 60609", "Irving Park 60618"};
        return Arrays.asList(array);
    }


}
