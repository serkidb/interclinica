/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Models.Database;
import Models.Validator;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Serkid
 */
public class register extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String specialty = new String();
            String firstName = request.getParameter("first_name");
            String lastName = request.getParameter("last_name");
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String address = request.getParameter("address");
            String phone_number = request.getParameter("phone_number");
            String password = request.getParameter("password");
            specialty = request.getParameter("specialty");
            
            
            if(Validator.checkIfAlreadyUser(username))
            {
                out.println("Someone is already using that username");
                RequestDispatcher rs = request.getRequestDispatcher("index.html");
                rs.include(request, response);
                
            }else if(specialty == null){
                
                System.out.println("Register User");
                
                Database.registerUser(firstName, lastName, username, email, address, phone_number, password,"patient","");
                RequestDispatcher rs = request.getRequestDispatcher("index.html");
                rs.include(request, response);
                
            }else if(!specialty.isEmpty()){
                System.out.println("Register Doctor");
                Database.registerUser(firstName, lastName, username, email, address, phone_number, password,"doctor",specialty);
                RequestDispatcher rs = request.getRequestDispatcher("admin.html");
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
