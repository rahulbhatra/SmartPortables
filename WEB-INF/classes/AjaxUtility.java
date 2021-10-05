import java.util.*;

import java.sql.*;


public class AjaxUtility {
	StringBuffer sb = new StringBuffer();
	boolean namesAdded = false;
	static Connection conn = null;
    static String message;
	public static String getConnection()
	{

		try
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/exampledatabase","root","root");							
			message="Successfull";
			return message;
		}
		catch(SQLException e)
		{
			 message="unsuccessful";
		     return message;
		}
		catch(Exception e)
		{
			 message="unsuccessful";
		     return message;
		}
	}
	
	public  StringBuffer readdata(String searchId)
	{	
		HashMap<String,Product> data;
		data=getData();
		
 	    Iterator it = data.entrySet().iterator();	
        while (it.hasNext()) 
	    {
                    Map.Entry pi = (Map.Entry)it.next();
			if(pi!=null)
			{
				Product p=(Product)pi.getValue();                   
                if (p.getName().toLowerCase().startsWith(searchId))
                {
                        sb.append("<product>");
                        sb.append("<id>" + p.getProductId() + "</id>");
                        sb.append("<productName>" + p.getName() + "</productName>");
                        sb.append("</product>");
                }
			}
       }
	  
	   return sb;
	}
	
	public static HashMap<String,Product> getData()
	{
		HashMap<String,Product> hm=new HashMap<String,Product>();
		try
		{
			getConnection();
			
		    String selectproduct="select * from  Productdetails";
		    PreparedStatement pst = conn.prepareStatement(selectproduct);
			ResultSet rs = pst.executeQuery();
			
			while(rs.next())
			{	Product p = new Product(
						rs.getLong("productId"),
						rs.getString("productName"),
						rs.getDouble("productPrice"),
						rs.getString("productImage"),
						ProductManufacturers.getEnum(rs.getString("productManufacturer")),
						rs.getString("productCondition"),
						rs.getDouble("productDiscount"),
						rs.getString("productDescription"),
						ProductCategory.getEnum(rs.getString("productCategory")),
						rs.getDouble("productRebate"))
					;
				hm.put(rs.getString("Id"), p);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return hm;			
	}
	public static void storeData(HashMap<String,Product> productdata)
	{
		try
		{
		
			getConnection();
				
			String insertIntoProductQuery = "INSERT INTO product(productId,productName,image,retailer,productCondition,productPrice,productType,discount) "
			+ "VALUES (?,?,?,?,?,?,?,?);";	
			for(Map.Entry<String, Product> entry : productdata.entrySet())
			{	

				PreparedStatement pst = conn.prepareStatement(insertIntoProductQuery);
				//set the parameter for each column and execute the prepared statement
				pst.setLong(1,entry.getValue().getProductId());
				pst.setString(2,entry.getValue().getName());
				pst.setString(3,entry.getValue().getImage());
				pst.setString(4,entry.getValue().getManufacturer().toString());
				pst.setString(5,entry.getValue().getCondition());
				pst.setDouble(6,entry.getValue().getPrice());
				pst.setString(7,entry.getValue().getCategory().toString());
				pst.setDouble(8,entry.getValue().getDiscount());
				pst.execute();
			}
		}
		catch(Exception e)
		{	
			e.printStackTrace();
		}		
	}

}
