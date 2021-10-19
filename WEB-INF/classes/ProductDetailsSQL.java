import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public static List<Product> getProducts(Connection conn) {
        List<Product> products = new ArrayList<>();
        try {
            String selectGame = "select * from  ProductDetails";
            PreparedStatement pst = conn.prepareStatement(selectGame);
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
                products.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    public static String updateProducts(Connection conn, Product product) {
        String msg = "Product is updated successfully";
        try {
            String updateProductQuery = "UPDATE ProductDetails SET productName=?, productPrice=?, productImage=?," +
                    "productManufacturer=?, productCondition=?, productCategory=?, productDescription=?, productDiscount=?," +
                    "productRebate=? where productId =?;";


            PreparedStatement pst = conn.prepareStatement(updateProductQuery);

            pst.setString(1, product.getProductName());
            pst.setDouble(2, product.getPrice());
            pst.setString(3, product.getImage());
            pst.setString(4, product.getManufacturer().toString());
            pst.setString(5, product.getCondition());
            pst.setString(6, product.getCategory().toString());
            pst.setString(7, product.getDescription());
            pst.setDouble(8, product.getDiscount());
            pst.setDouble(9, product.getRebate());
            pst.setLong(10, product.getProductId());
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
            e.printStackTrace();
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

            String insertProductQurey = "INSERT INTO  ProductDetails(" +
                    "productName, productPrice, productImage, productManufacturer,productCondition,productDiscount," +
                    "productDescription, productCategory, productRebate)" +
                    "VALUES (?,?,?,?,?,?,?,?,?);";
            for (Product product : SaxParserDataStore.accessoriesHasMap.values()) {
                System.out.println("---------------- Accessory Printed" + product);
                PreparedStatement pst = conn.prepareStatement(insertProductQurey);
                pst.setString(1, product.getProductName());
                pst.setDouble(2, product.getPrice());
                pst.setString(3, product.getImage());
                pst.setString(4, product.getManufacturer().toString());
                pst.setString(5, product.getCondition());
                pst.setDouble(6, product.getDiscount());
                pst.setString(7, product.getDescription());
                pst.setString(8, product.getCategory().toString());
                pst.setDouble(9, product.getRebate());
                pst.executeUpdate();
            }

            for (Product product : SaxParserDataStore.wearableTechnologyHashMap.values()) {

                System.out.println("-------- Inside Product Map Wearable -- " + product.toString());
                String name = ProductCategory.WearableTechnology.toString();


                PreparedStatement pst = conn.prepareStatement(insertProductQurey, PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setString(1, product.getProductName());
                pst.setDouble(2, product.getPrice());
                pst.setString(3, product.getImage());
                pst.setString(4, product.getManufacturer().toString());
                pst.setString(5, product.getCondition());
                pst.setDouble(6, product.getDiscount());
                pst.setString(7, product.getDescription());
                pst.setString(8, product.getCategory().toString());
                pst.setDouble(9, product.getRebate());

                pst.executeUpdate();

                ResultSet rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    product.setProductId(rs.getLong(1));
                }

                try {
                    Map<Integer, Integer> acc = product.getAccessories();
                    String insertAccessoryQurey = "INSERT INTO  ProductAccessories(productId,accessoryId)" +
                            "VALUES (?,?);";
                    for (Map.Entry<Integer, Integer> accentry : acc.entrySet()) {
                        PreparedStatement pstacc = conn.prepareStatement(insertAccessoryQurey);
                        pstacc.setLong(1, product.getProductId());
                        pstacc.setLong(2, accentry.getValue());
                        pstacc.executeUpdate();
                    }
                } catch (Exception et) {
                    et.printStackTrace();
                }
            }

            for (Product product : SaxParserDataStore.phoneHashMap.values()) {
                PreparedStatement pst = conn.prepareStatement(insertProductQurey);
                pst.setString(1, product.getProductName());
                pst.setDouble(2, product.getPrice());
                pst.setString(3, product.getImage());
                pst.setString(4, product.getManufacturer().toString());
                pst.setString(5, product.getCondition());
                pst.setDouble(6, product.getDiscount());
                pst.setString(7, product.getDescription());
                pst.setString(8, product.getCategory().toString());
                pst.setDouble(9, product.getRebate());
                pst.executeUpdate();
            }

            for (Product product : SaxParserDataStore.laptopHashMap.values()) {
                PreparedStatement pst = conn.prepareStatement(insertProductQurey);
                pst.setString(1, product.getProductName());
                pst.setDouble(2, product.getPrice());
                pst.setString(3, product.getImage());
                pst.setString(4, product.getManufacturer().toString());
                pst.setString(5, product.getCondition());
                pst.setDouble(6, product.getDiscount());
                pst.setString(7, product.getDescription());
                pst.setString(8, product.getCategory().toString());
                pst.setDouble(9, product.getRebate());
                pst.executeUpdate();
            }

            for (Product product : SaxParserDataStore.voiceAssistantHashMap.values()) {
                PreparedStatement pst = conn.prepareStatement(insertProductQurey);
                pst.setString(1, product.getProductName());
                pst.setDouble(2, product.getPrice());
                pst.setString(3, product.getImage());
                pst.setString(4, product.getManufacturer().toString());
                pst.setString(5, product.getCondition());
                pst.setDouble(6, product.getDiscount());
                pst.setString(7, product.getDescription());
                pst.setString(8, product.getCategory().toString());
                pst.setDouble(9, product.getRebate());
                pst.executeUpdate();
            }

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

    public static Product addProducts(Connection conn, Product product) {

        String msg = "Product is added successfully";
        try {
            String addProductQurey = "INSERT INTO  ProductDetails(" +
                    "productName, productPrice, productImage, productManufacturer,productCondition,productDiscount," +
                    "productDescription, productCategory, productRebate)" +
                    "VALUES (?,?,?,?,?,?,?,?,?);";

            PreparedStatement pst = conn.prepareStatement(addProductQurey, PreparedStatement.RETURN_GENERATED_KEYS);

            pst.setString(1, product.getProductName());
            pst.setDouble(2, product.getPrice());
            pst.setString(3, product.getImage());
            pst.setString(4, product.getManufacturer().toString());
            pst.setString(5, product.getCondition());
            pst.setDouble(6, product.getDiscount());
            pst.setString(7, product.getDescription());
            pst.setString(8, product.getCategory().toString());
            pst.setDouble(9, product.getRebate());

            System.out.println("----- Query Generated ------" + pst.toString());

            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                product.setProductId(rs.getLong(1));
            }
//            try {
//                Map<Integer, Integer> acc = product.getAccessories();
//                String insertAccessoryQuery = "INSERT INTO  Product_accessories(productName,accessoriesName)" +
//                        "VALUES (?,?);";
//                for (Map.Entry<Integer, Integer> accentry : acc.entrySet()) {
//                    PreparedStatement pstacc = conn.prepareStatement(insertAccessoryQuery);
//                    pstacc.setLong(1, product.getProductId());
//                    pstacc.setLong(2, accentry.getValue());
//                    pstacc.executeUpdate();
//                }
//            } catch (Exception et) {
//                et.printStackTrace();
//            }

        } catch (Exception e) {
            msg = "Error while adding the product";
            e.printStackTrace();

        }
        return product;
    }

}
