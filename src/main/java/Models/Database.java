/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.sql.*;
import org.json.JSONArray;
import org.json.JSONObject;

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
    
    public static void registerUser(String firstName, String lastName, String username, String email, String address, String phoneNumber, String password, String type,String specialty)
    {
        
        
         try{
         Class.forName("com.mysql.cj.jdbc.Driver");
         Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/interclinica","root","");
         PreparedStatement ps =con.prepareStatement("INSERT INTO users(username,pwd,email,first_name,last_name,address,phone_number,type,specialty,created_at) VALUES (?,?,?,?,?,?,?,?,?,CURRENT_TIMESTAMP)");
         ps.setString(1, username);
         ps.setString(2, password);  
         ps.setString(3, email);  
         ps.setString(4, firstName);  
         ps.setString(5, lastName);  
         ps.setString(6, address);  
         ps.setString(7, phoneNumber);  
         ps.setString(8, type);  
         ps.setString(9, specialty);
         ps.executeUpdate();
               
      }catch(Exception e)
      {
          e.printStackTrace();
      }
        
        
    }
    
    
    
    
        public static JSONArray getAppointments(String id)
    {
       JSONArray myArray =new JSONArray();
         try{
         Class.forName("com.mysql.cj.jdbc.Driver");
         Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/interclinica","root","");
         PreparedStatement ps =con.prepareStatement
                         ("SELECT * FROM appointments INNER JOIN users ON appointments.doctor_id = users.u_id WHERE appointments.patient_id = ?");
         ps.setString(1, id);
         ResultSet rs =ps.executeQuery();
            while (rs.next()) {
                
                
                
                JSONObject myObj = new JSONObject();
                myObj.put("app_id", rs.getString("app_id"));
                myObj.put("doctor_id", rs.getString("doctor_id"));
                myObj.put("date_time", rs.getString("date_time"));
                myObj.put("app_status",rs.getString("app_status"));
                myObj.put("first_name", rs.getString("first_name"));
                myObj.put("last_name", rs.getString("last_name"));
                myObj.put("specialty", rs.getString("specialty"));
                
                //myObj.put("status", rs.getString("status"));
                
                myArray.put(myObj);
            }
        
      
      }catch(Exception e)
      {
          e.printStackTrace();
      }
        
        
        return myArray;
    }
        
        
        
           public static void deleteDoctor(String doctorId)
    {
        
        
         try{
         Class.forName("com.mysql.cj.jdbc.Driver");
         Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/interclinica","root","");
          PreparedStatement ps =con.prepareStatement("DELETE FROM users WHERE u_id =?");
         ps.setString(1, doctorId);
         ps.executeUpdate();
               
      }catch(Exception e)
      {
          e.printStackTrace();
      }
        
        
    }
           
           
           
            public static JSONArray getInfo(String id)
    {
       JSONArray myArray =new JSONArray();
         try{
         Class.forName("com.mysql.cj.jdbc.Driver");
         Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/interclinica","root","");
         PreparedStatement ps =con.prepareStatement
                         ("SELECT * FROM user WHERE u_id = 1");
         ps.setString(1, id);
         ResultSet rs =ps.executeQuery();
            while (rs.next()) {
                
                
                
                JSONObject myObj = new JSONObject();
                myObj.put("first_name", rs.getString("first_name"));
                myObj.put("last_name", rs.getString("last_name"));
                myArray.put(myObj);
            }
        
      
      }catch(Exception e)
      {
          e.printStackTrace();
      }
        
        
        return myArray;
    }
    
    
}
