import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/RegistrationSQL")
public class RegistrationSQL extends HttpServlet {
    public static void insertUser(Connection conn, String username, String password, String repassword, String usertype, Integer userAge,
                                  String userGender, String userOccupation) {
        try {
            String insertIntoCustomerRegisterQuery = "INSERT INTO Registration(userName,password,rePassword,userType," +
                    "userAge, userGender, userOccupation) "
                    + "VALUES (?,?,?,?,?,?,?);";

            PreparedStatement pst = conn.prepareStatement(insertIntoCustomerRegisterQuery);
            pst.setString(1, username);
            pst.setString(2, password);
            pst.setString(3, repassword);
            pst.setString(4, usertype);
            pst.setInt(5, userAge);
            pst.setString(6, userGender);
            pst.setString(7, userOccupation);
            System.out.println("--------------- Inser User" + pst.toString());
            pst.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<String, User> selectUser(Connection conn) {
        Map<String, User> userHashMap = new HashMap<>();
        try {
            Statement stmt = conn.createStatement();
            String selectCustomerQuery = "select * from  Registration";
            ResultSet rs = stmt.executeQuery(selectCustomerQuery);
            while (rs.next()) {
                User user = new User(rs.getLong("userId"), rs.getString("username"),
                        rs.getString("password"), rs.getString("repassword"),
                        rs.getString("usertype"), rs.getInt("userAge"),
                        rs.getString("userGender"), rs.getString("userOccupation"));
                userHashMap.put(rs.getString("username"), user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userHashMap;
    }


    public static User getUser(Connection conn, Long userId) {
        try {
            Statement stmt = conn.createStatement();
            String query = "select * from  Registration where userId=?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setLong(1, userId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                User user = new User(rs.getLong("userId"), rs.getString("username"),
                        rs.getString("password"), rs.getString("repassword"),
                        rs.getString("usertype"), rs.getInt("userAge"),
                        rs.getString("userGender"), rs.getString("userOccupation"));
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
