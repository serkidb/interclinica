/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.sql.*;

public class Database {
    
    
    public static String getUserType(String username)
    {
        String type = new String();
        
         try{
         Class.forName("com.mysql.cj.jdbc.Driver");
         Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/interclinica","root","");
         PreparedStatement ps =con.prepareStatement
                         ("select * from users where username=?");
         ps.setString(1, username);   
         ResultSet rs =ps.executeQuery();
         if(rs.next())
         {type = rs.getString("type");}
      
      }catch(Exception e)
      {
          e.printStackTrace();
      }
        
        
        return type;
    }
    
    public static void registerUser(String firstName, String lastName, String username, String email, String address, String phoneNumber, String password)
    {
        String type = new String();
        
         try{
         Class.forName("com.mysql.cj.jdbc.Driver");
         Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/interclinica","root","");
         PreparedStatement ps =con.prepareStatement("INSERT INTO users(username,pwd,email,first_name,last_name,address,phone_number,type,specialty,created_at) VALUES (?,?,?,?,?,?,?,?,?,CURRENT_DATE)");
         ps.setString(1, username);
         ps.setString(2, password);  
         ps.setString(3, email);  
         ps.setString(4, firstName);  
         ps.setString(5, lastName);  
         ps.setString(6, address);  
         ps.setString(7, phoneNumber);  
         ps.setString(8, type);  
         ps.setString(9, "");
         ps.executeUpdate();
               
      }catch(Exception e)
      {
          e.printStackTrace();
      }
        
        
    }
    
    
    
    
}
