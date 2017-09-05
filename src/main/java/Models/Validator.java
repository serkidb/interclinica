/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.joda.time.*;
import java.util.Calendar;
import java.util.concurrent.ThreadLocalRandom;
import javax.servlet.http.HttpSession;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author Serkid
 */
public class Validator {

    public static boolean validateUser(String username, String pass, HttpSession session) {

        boolean st = false;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/interclinica", "root", "");
            PreparedStatement ps = con.prepareStatement("select * from users");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                while (rs.next()) {
                    String salt = rs.getString("salt");
                    String pwd = rs.getString("pwd");
                    String databasePassword = Hash.md5(pass + salt);
                    String databaseUsername = rs.getString("username");

                    if (pwd.equals(databasePassword) && username.equals(databaseUsername)) {
                        session.setAttribute("userId", rs.getString("u_id"));
                        st = true;
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return st;
    }

    public static boolean checkIfAlreadyUser(String username) {
        boolean st = false;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/interclinica", "root", "");
            PreparedStatement ps = con.prepareStatement("select * from users WHERE username=?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                st = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return st;
    }
    
    
    public static boolean checkIfDays(String id)
    {
       boolean isOk = false;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/interclinica", "root", "");
        PreparedStatement ps = con.prepareStatement("SELECT * FROM appointments WHERE app_id = ?");
            ps.setString(1, id);
           ResultSet rs =  ps.executeQuery();
            if(rs.next())
            {
                
                String date = rs.getString("date_time").split("\\s+")[0];
                
                DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MMM-dd");
                
                LocalDate appDate =new LocalDate(date);
                LocalDate today = LocalDate.now();
                
                int days = Days.daysBetween(today, appDate).getDays();
               
                System.out.println(days);
                if(days >=3)
                {
                    isOk = true;
                }else
                {
                    isOk = false;
                }
                
            }
         } catch (Exception e) {
            e.printStackTrace();
        }
       return isOk;
    }

}
