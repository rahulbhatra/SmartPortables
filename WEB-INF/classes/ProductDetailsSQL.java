import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/ProductDetailsSQL")
public class ProductDetailsSQL extends HttpServlet {

    public static Product getProduct(Connection conn, Long productId) {
        try {
            String selectGame = "select * from  ProductDetails where productId=?";
            PreparedStatement pst = conn.prepareStatement(selectGame);
            pst.setLong(1, productId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {

                Product product = new Product(
                        rs.getLong("productId"),
                        rs.getString("productName"),
                        rs.getDouble("productPrice"),
                        rs.getString("productImage"),
                        ProductManufacturers.getEnum(rs.getString("productManufacturer")),
                        rs.getString("productCondition"),
                        rs.getDouble("productDiscount"),
                        rs.getString("productDescription"),
                        ProductCategory.getEnum(rs.getString("productCategory")),
                        rs.getDouble("productRebate")

                );
                return product;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String updateProducts(Connection conn, Long productId, String productName, double productPrice, String productImage, String productManufacturer, String productCondition, double productDiscount) {
        String msg = "Product is updated successfully";
        try {
            String updateProductQuery = "UPDATE ProductDetails SET productName=?,productPrice=?,productImage=?,productManufacturer=?,productCondition=?,productDiscount=? where productId =?;";


            PreparedStatement pst = conn.prepareStatement(updateProductQuery);

            pst.setString(1, productName);
            pst.setDouble(2, productPrice);
            pst.setString(3, productImage);
            pst.setString(4, productManufacturer);
            pst.setString(5, productCondition);
            pst.setDouble(6, productDiscount);
            pst.setLong(7, productId);
            pst.executeUpdate();


        } catch (Exception e) {
            msg = "Product cannot be updated";
            e.printStackTrace();

        }
        return msg;
    }

    public static String deleteProducts(Connection conn, Long productId) {
        String msg = "Product is deleted successfully";
        try {
            String deleteproductsQuery = "Delete from ProductDetails where productId=?";
            PreparedStatement pst = conn.prepareStatement(deleteproductsQuery);
            pst.setLong(1, productId);

            pst.executeUpdate();
        } catch (Exception e) {
            msg = "Proudct cannot be deleted";
        }
        return msg;
    }

    public static void insertProducts(Connection conn) {
        try {

            System.out.println("------- Inside insert Products ---------");

            String truncatePA = "delete from ProductAccessories;";
            PreparedStatement pstPA = conn.prepareStatement(truncatePA);
            pstPA.executeUpdate();

            String truncatePD = "delete from  ProductDetails;";
            PreparedStatement pstPD = conn.prepareStatement(truncatePD);
            pstPD.executeUpdate();


//            productId int not null auto_increment,
//                    productName varchar(40),
//                    productPrice double,
//            productImage varchar(40),
//                    productManufacturer varchar(40),
//                    productCondition varchar(40),
//                    productDiscount double,
//            productDescription varchar(1000),
//                    productCategory varchar(40),
//                    productRebate varchar(40),
//                    Primary key(productId)

            String insertProductQurey = "INSERT INTO  ProductDetails(" +
                    "productName, productPrice, productImage, productManufacturer,productCondition,productDiscount," +
                    "productDescription, productCategory, productRebate)" +
                    "VALUES (?,?,?,?,?,?,?,?,?);";
//            for (Map.Entry<String, Product> entry : SaxParserDataStore.accessoriesHasMap.entrySet()) {
//                String name = "accessories";
//                Product acc = entry.getValue();
//
//                System.out.println("---------------- Accessory Printed" + acc);
//
//                PreparedStatement pst = conn.prepareStatement(insertProductQurey);
//                pst.setString(1, acc.getId());
//                pst.setString(2, acc.getName());
//                pst.setDouble(3, acc.getPrice());
//                pst.setString(4, acc.getImage());
//                pst.setString(5, acc.getManufacturer().toString());
//                pst.setString(6, acc.getCondition());
//                pst.setDouble(7, acc.getDiscount());
//                pst.setString(8, acc.getDescription());
//                pst.setString(9, acc.getCategory().toString());
//                pst.setDouble(10, acc.getRebate());
//
//                pst.executeUpdate();
//
//
//            }

//            for (Map.Entry<Long, Product> entry : SaxParserDataStore.wearableTechnologyHashMap.entrySet()) {
//
//                Product product = entry.getValue();
//                System.out.println("-------- Inside Product Map Wearable -- " + product.toString());
//                String name = ProductCategory.WearableTechnology.toString();
//
//
//                PreparedStatement pst = conn.prepareStatement(insertProductQurey, PreparedStatement.RETURN_GENERATED_KEYS);
//                pst.setString(1, product.getName());
//                pst.setDouble(2, product.getPrice());
//                pst.setString(3, product.getImage());
//                pst.setString(4, product.getManufacturer().toString());
//                pst.setString(5, product.getCondition());
//                pst.setDouble(6, product.getDiscount());
//                pst.setString(7, product.getDescription());
//                pst.setString(8, product.getCategory().toString());
//                pst.setDouble(9, product.getRebate());
//
//                pst.executeUpdate();
//
//                ResultSet rs = pst.getGeneratedKeys();
//                if(rs.next()) {
//                    product.setProductId(rs.getLong(1));
//                }
//
//                try {
//                    Map<Integer, Integer> acc = product.getAccessories();
////                    productId int not null,
////                            accessoryId int not null,
//                    String insertAccessoryQurey = "INSERT INTO  ProductAccessories(productId,accessoryId)" +
//                            "VALUES (?,?);";
//                    for (Map.Entry<Integer, Integer> accentry : acc.entrySet()) {
//                        PreparedStatement pstacc = conn.prepareStatement(insertAccessoryQurey);
//                        pstacc.setLong(1, product.getProductId());
//                        pstacc.setLong(2, accentry.getValue());
//                        pstacc.executeUpdate();
//                    }
//                } catch (Exception et) {
//                    et.printStackTrace();
//                }
//            }
//
//            for (Map.Entry<Long, Product> entry : SaxParserDataStore.phoneHashMap.entrySet()) {
//                String name = ProductCategory.Phone.toString();
//
//                Product product = entry.getValue();
//
//                PreparedStatement pst = conn.prepareStatement(insertProductQurey);
//                pst.setString(1, product.getName());
//                pst.setDouble(2, product.getPrice());
//                pst.setString(3, product.getImage());
//                pst.setString(4, product.getManufacturer().toString());
//                pst.setString(5, product.getCondition());
//                pst.setDouble(6, product.getDiscount());
//                pst.setString(7, product.getDescription());
//                pst.setString(8, product.getCategory().toString());
//                pst.setDouble(9, product.getRebate());
//
//                pst.executeUpdate();
//
//
//            }
//
//            for (Map.Entry<Long, Product> entry : SaxParserDataStore.laptopHashMap.entrySet()) {
//                String name = ProductCategory.Laptop.toString();
//                Product product = entry.getValue();
//
//                PreparedStatement pst = conn.prepareStatement(insertProductQurey);
//                pst.setString(1, product.getName());
//                pst.setDouble(2, product.getPrice());
//                pst.setString(3, product.getImage());
//                pst.setString(4, product.getManufacturer().toString());
//                pst.setString(5, product.getCondition());
//                pst.setDouble(6, product.getDiscount());
//                pst.setString(7, product.getDescription());
//                pst.setString(8, product.getCategory().toString());
//                pst.setDouble(9, product.getRebate());
//
//                pst.executeUpdate();
//            }
//
//            for (Map.Entry<Long, Product> entry : SaxParserDataStore.voiceAssistantHashMap.entrySet()) {
//                String name = ProductCategory.Laptop.toString();
//                Product product = entry.getValue();
//
//                PreparedStatement pst = conn.prepareStatement(insertProductQurey);
//                pst.setString(1, product.getName());
//                pst.setDouble(2, product.getPrice());
//                pst.setString(3, product.getImage());
//                pst.setString(4, product.getManufacturer().toString());
//                pst.setString(5, product.getCondition());
//                pst.setDouble(6, product.getDiscount());
//                pst.setString(7, product.getDescription());
//                pst.setString(8, product.getCategory().toString());
//                pst.setDouble(9, product.getRebate());
//
//                pst.executeUpdate();
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<Long, Product> getProductsByCategory(Connection conn, ProductCategory productCategory) {
        Map<Long, Product> hm = new HashMap<>();
        try {

            String selectGame = "select * from  ProductDetails where productCategory=?";
            PreparedStatement pst = conn.prepareStatement(selectGame);
            pst.setString(1, productCategory.toString());
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                Product product = new Product(
                        rs.getLong("productId"),
                        rs.getString("productName"),
                        rs.getDouble("productPrice"),
                        rs.getString("productImage"),
                        ProductManufacturers.getEnum(rs.getString("productManufacturer")),
                        rs.getString("productCondition"),
                        rs.getDouble("productDiscount"),
                        rs.getString("productDescription"),
                        ProductCategory.getEnum(rs.getString("productCategory")),
                        rs.getDouble("productRebate")
                );
                hm.put(product.getProductId(), product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hm;
    }

    public static String addProducts(Connection conn, Product product) {

        String msg = "Product is added successfully";
        try {
            String addProductQurey = "INSERT INTO  ProductDetails(" +
                    "productName, productPrice, productImage, productManufacturer,productCondition,productDiscount," +
                    "productDescription, productCategory, productRebate)" +
                    "VALUES (?,?,?,?,?,?,?,?,?,?);";

            String name = product.getCategory().toString();

            PreparedStatement pst = conn.prepareStatement(addProductQurey);

            pst.setString(2, product.getName());
            pst.setDouble(3, product.getPrice());
            pst.setString(4, product.getImage());
            pst.setString(5, product.getManufacturer().toString());
            pst.setString(6, product.getCondition());
            pst.setDouble(7, product.getDiscount());
            pst.setString(8, product.getDescription());
            pst.setString(9, product.getCategory().toString());
            pst.setDouble(10, product.getRebate());

            pst.executeUpdate();
            try {
                Map<Integer, Integer> acc = product.getAccessories();
                String insertAccessoryQuery = "INSERT INTO  Product_accessories(productName,accessoriesName)" +
                        "VALUES (?,?);";
                for (Map.Entry<Integer, Integer> accentry : acc.entrySet()) {
                    PreparedStatement pstacc = conn.prepareStatement(insertAccessoryQuery);
                    pstacc.setLong(1, product.getProductId());
                    pstacc.setLong(2, accentry.getValue());
                    pstacc.executeUpdate();
                }
            } catch (Exception et) {
                et.printStackTrace();
            }

        } catch (Exception e) {
            msg = "Error while adding the product";
            e.printStackTrace();

        }
        return msg;
    }

}
