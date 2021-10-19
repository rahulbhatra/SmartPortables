import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductAccessorySQL {
    public static List<Long> getProductAccessories(Connection conn, Long productId) {
        List<Long> productAccessories = new ArrayList<>();
        try {
            String selectGame = "select * from  ProductAccessories where productId=?";
            PreparedStatement pst = conn.prepareStatement(selectGame);
            pst.setLong(1, productId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                productAccessories.add(rs.getLong("accessoryId"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productAccessories;
    }

    public static void insertProductAccessory(Connection conn, Long productId, Long accessoryId) {
        try {
            String query = "Insert Into ProductAccessories(productId, accessoryId) values(?,?);";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setLong(1, productId);
            pst.setLong(2, accessoryId);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteProductAccessory(Connection conn, Long productId, Long accessoryId) {
        try {
            String query = "delete from ProductAccessories where productId=? and accessoryId =?;";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setLong(1, productId);
            pst.setLong(2, accessoryId);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
