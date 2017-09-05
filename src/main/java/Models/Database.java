/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import org.json.JSONArray;
import org.json.JSONObject;

public class Database {

    public static String getUserType(String username) {
        String type = new String();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/interclinica", "root", "");
            PreparedStatement ps = con.prepareStatement("select * from users where username=?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                type = rs.getString("type");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return type;
    }

    public static void registerUser(String firstName, String lastName, String username, String email, String address, String phoneNumber, String password, String type, String specialty) {
        Generator gen = new Generator(12, ThreadLocalRandom.current());
        String randomString = gen.nextString();
        String newPassword = Hash.md5(password+randomString);
        System.out.println(newPassword);
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/interclinica", "root", "");
            PreparedStatement ps = con.prepareStatement("INSERT INTO users(username,pwd,email,first_name,last_name,address,phone_number,type,specialty,salt,created_at) VALUES (?,?,?,?,?,?,?,?,?,?,CURRENT_TIMESTAMP)");
            ps.setString(1, username);
            ps.setString(2, newPassword);
            ps.setString(3, email);
            ps.setString(4, firstName);
            ps.setString(5, lastName);
            ps.setString(6, address);
            ps.setString(7, phoneNumber);
            ps.setString(8, type);
            ps.setString(9, specialty);
            ps.setString(10, randomString);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public static JSONArray getAppointments(String id, String type) {
        JSONArray myArray = new JSONArray();
        try {
            String sql = new String();
            if (type.equals("doctor")) {
                sql = "SELECT * FROM appointments INNER JOIN users ON appointments.patient_id = users.u_id WHERE appointments.doctor_id = ? ORDER BY appointments.date_time DESC";

            } else if (type.equals("patient")) {
                sql = "SELECT * FROM appointments INNER JOIN users ON appointments.doctor_id = users.u_id WHERE appointments.patient_id = ? ORDER BY appointments.date_time DESC";
            }
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/interclinica", "root", "");
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                JSONObject myObj = new JSONObject();
                myObj.put("app_id", rs.getString("app_id"));
                myObj.put("doctor_id", rs.getString("doctor_id"));
                myObj.put("date_time", rs.getString("date_time"));
                myObj.put("app_status", rs.getString("app_status"));
                myObj.put("first_name", rs.getString("first_name"));
                myObj.put("last_name", rs.getString("last_name"));
                myObj.put("specialty", rs.getString("specialty"));

                //myObj.put("status", rs.getString("status"));
                myArray.put(myObj);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return myArray;
    }

    public static void deleteDoctor(String doctorId) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/interclinica", "root", "");
            PreparedStatement ps = con.prepareStatement("DELETE FROM users WHERE u_id =?");
            ps.setString(1, doctorId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static JSONArray getInfo(String id) {
        JSONArray myArray = new JSONArray();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/interclinica", "root", "");
            PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE u_id = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                JSONObject myObj = new JSONObject();
                myObj.put("first_name", rs.getString("first_name"));
                myObj.put("last_name", rs.getString("last_name"));
                myObj.put("specialty", rs.getString("specialty"));
                myArray.put(myObj);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return myArray;
    }

    public static JSONArray getAllDoctors() {
        JSONArray myArray = new JSONArray();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/interclinica", "root", "");
            PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE type LIKE 'doctor'");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                JSONObject myObj = new JSONObject();
                myObj.put("u_id", rs.getString("u_id"));
                myObj.put("first_name", rs.getString("first_name"));
                myObj.put("last_name", rs.getString("last_name"));
                myObj.put("specialty", rs.getString("specialty"));
                myArray.put(myObj);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return myArray;
    }

    public static String changeState(String status, String id) {
        String message = new String();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/interclinica", "root", "");
            
            if(Validator.checkIfDays(id) && status.equals("Cancelled"))
            {
                System.out.println("Can cancel");
                message= "Cancel done";
                
            PreparedStatement ps = con.prepareStatement("UPDATE `appointments` SET app_status =? WHERE app_id = ?");
            ps.setString(1, status);
            ps.setString(2, id);
            ps.executeUpdate();
                
            }else if(status.equals("Completed")){
                
            PreparedStatement ps = con.prepareStatement("UPDATE `appointments` SET app_status =? WHERE app_id = ?");
            ps.setString(1, status);
            ps.setString(2, id);
            ps.executeUpdate();
            message = "Completed done";
            
            }else{
                message = "cannot update";
                        
            }
            
            
            

        } catch (Exception e) {
            e.printStackTrace();
        }
            return message;
    }

    public static JSONArray checkAvailability(String doctorType, String date, String timeOfDay) {
        JSONArray myArray = new JSONArray();
        try {
            //
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date startDate;
            startDate = df.parse(date);
            String myString = new SimpleDateFormat("EE", Locale.ENGLISH).format(startDate);
            String days = new String();
            String sql = new String();
             sql="SELECT * FROM users INNER JOIN availability ON users.u_id = availability.doctor_id WHERE users.specialty LIKE ? AND availability.days LIKE ? AND availability.hours LIKE ?";
            if (myString.equals("Sat")) {
                days = "Mon-Sat";
            } else if (myString.equals("Sun")) {
                days = "Sunday";
                timeOfDay = "all day";
            } else {
                days = "Mon-Fri";
                sql="SELECT * FROM users INNER JOIN availability ON users.u_id = availability.doctor_id WHERE users.specialty LIKE ? AND availability.days LIKE ? OR availability.days LIKE 'Mon-Sat' AND availability.hours LIKE ?";
            }
            System.out.println(days);
            System.out.println(sql);
            //
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/interclinica", "root", "");
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, doctorType);
            ps.setString(2, days);
            ps.setString(3, timeOfDay);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                JSONObject myObj = new JSONObject();
                myObj.put("u_id", rs.getString("u_id"));
                myObj.put("first_name", rs.getString("first_name"));
                myObj.put("last_name", rs.getString("last_name"));
                myObj.put("specialty", rs.getString("specialty"));

                ps = con.prepareStatement("SELECT * FROM appointments WHERE doctor_id = ? AND DATE(date_time) = ? AND app_status LIKE 'active'");
                ps.setString(1, rs.getString("u_id"));
                ps.setString(2, date);
                ResultSet rs2 = ps.executeQuery();
                JSONArray appointments = new JSONArray();
                while (rs2.next()) {
                    JSONObject appointment = new JSONObject();
                    appointment.put("app_id", rs2.getString("app_id"));
                    appointment.put("hour", rs2.getString("date_time"));
                    appointments.put(appointment);
                }
                myObj.put("appointments", appointments);
                myArray.put(myObj);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return myArray;
    }

    public static void sAvailability(String doctorId, String days, String hours) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/interclinica", "root", "");
            PreparedStatement ps = con.prepareStatement("DELETE FROM availability WHERE doctor_id=?");
            ps.setString(1, doctorId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/interclinica", "root", "");
            PreparedStatement ps = con.prepareStatement("INSERT INTO availability(doctor_id, days, hours) VALUES (?,?,?)");
            ps.setString(1, doctorId);
            ps.setString(2, days);
            ps.setString(3, hours);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static JSONArray currentAvailability(String id) {
        JSONArray myArray = new JSONArray();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/interclinica", "root", "");
            PreparedStatement ps = con.prepareStatement("SELECT * FROM availability WHERE doctor_id = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                JSONObject myObj = new JSONObject();
                myObj.put("doctor_id", rs.getString("doctor_id"));
                myObj.put("days", rs.getString("days"));
                myObj.put("hours", rs.getString("hours"));
                myArray.put(myObj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myArray;
    }
    
    public static void bookApp(String userId, String docId, String date_time) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/interclinica", "root", "");
            PreparedStatement ps = con.prepareStatement("INSERT INTO appointments(doctor_id, patient_id, date_time, app_status) VALUES (?,?,?,?)");
            String appStatus = "active";
            ps.setString(1, docId);
            ps.setString(2, userId);
            ps.setString(3, date_time);
            ps.setString(4, appStatus);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
