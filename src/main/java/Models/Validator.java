/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.sql.*;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Serkid
 */
public class Validator {
    
    public static boolean validateUser(String username, String pass,HttpSession session)
    {
        boolean st =false;
      try{
         Class.forName("com.mysql.cj.jdbc.Driver");
         Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/interclinica","root","");
         PreparedStatement ps =con.prepareStatement
                             ("select * from users where username=? and pwd=?");
         ps.setString(1, username);
         ps.setString(2, pass);
         ResultSet rs =ps.executeQuery();
         if(rs.next())
         {
            
            session.setAttribute("userId", rs.getString("u_id"));
             st = true;
              
             
         }
         
        
      }catch(Exception e)
      {
          e.printStackTrace();
      }
         return st;                
    }
    
    
     public static boolean checkIfAlreadyUser(String username)
    {
        boolean st =false;
      try{
         Class.forName("com.mysql.cj.jdbc.Driver");
         Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/interclinica","root","");
         PreparedStatement ps =con.prepareStatement("select * from users WHERE username=?");
         ps.setString(1, username);
         ResultSet rs =ps.executeQuery();
         if(rs.next()){st = true;}
         
        
      }catch(Exception e)
      {
          e.printStackTrace();
      }
         return st;                
    }
    
    
    
    
    
    
}
