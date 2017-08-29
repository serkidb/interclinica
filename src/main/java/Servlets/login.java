/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Models.Database;
import Models.Validator;
import com.mysql.cj.api.Session;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Serkid
 */
public class login extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            HttpSession session = request.getSession();  
            session = request.getSession(true);
            String username = request.getParameter("username");
            String pwd = request.getParameter("password");
            if(Validator.validateUser(username, pwd, session))
            {
                String type = Database.getUserType(username);
                if(type.equals("patient"))
                 {
                     out.println("Welcome Patient");
                RequestDispatcher rs = request.getRequestDispatcher("/patient.html");
                rs.include(request, response);
                 }else if(type.equals("doctor"))
                 {
                     out.println("Welcome Doctor");
                RequestDispatcher rs = request.getRequestDispatcher("/doctor.html");
                rs.include(request, response);
                 }else if(type.equals("admin"))
                 {
                out.println("Welcome Admin");
                RequestDispatcher rs = request.getRequestDispatcher("/admin.html");
                rs.include(request, response);
                      
                 }
                 
            }else{
                
                out.println("Username or Password incorrect");
                RequestDispatcher rs = request.getRequestDispatcher("index.html");
                rs.include(request, response);
                
            }
            
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
