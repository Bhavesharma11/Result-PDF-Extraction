/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author BHAVESH
 */
public class Admin extends HttpServlet {

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
        
        String year = request.getParameter("year");
        String sem = request.getParameter("sem");
        
        System.out.println(year + sem);
        
        System.out.println(request.getParameter("theory"));
        System.out.println(request.getParameter("practical"));
        System.out.println(request.getParameter("oral"));
        System.out.println(request.getParameter("termwork"));
        int th=Integer.parseInt(request.getParameter("theory").trim());
        int pr=Integer.parseInt(request.getParameter("practical").trim());
        int or=Integer.parseInt(request.getParameter("oral").trim());
        int tw=Integer.parseInt(request.getParameter("termwork").trim());
        String tablename = year+"_"+sem+"_"+"subjects";
        System.out.println("In admin "+tablename);
        
        try{  
Class.forName("com.mysql.cj.jdbc.Driver");  
Connection con=DriverManager.getConnection("jdbc:mysql://localhost/result","root","");  
      
DatabaseMetaData dbm = con.getMetaData();
ResultSet tables = dbm.getTables(null, null, tablename, null); 

if(tables.next())
{
    System.out.println("table exists");
    System.out.println("going to viewdelete");
    RequestDispatcher rd=request.getRequestDispatcher("ViewDelete");  
    rd.forward(request,response);
}
else
{
    Statement stmt=con.createStatement();
                //stmt.executeUpdate("DROP TABLE IF EXISTS result."+tablename);
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `result`.`"+tablename+"` ( `sub_name` VARCHAR(40) NOT NULL , `sub_code` VARCHAR(20) NOT NULL, `sub_type` VARCHAR(10) NOT NULL ) ENGINE = InnoDB;");
                PrintWriter out=response.getWriter();
                HttpSession session=request.getSession();
                session.setAttribute("tablename",tablename);
                session.setAttribute("th",th);
                session.setAttribute("pr",pr);
                session.setAttribute("or",or);
                session.setAttribute("tw",tw);
                request.getRequestDispatcher("/theory.jsp").forward(request, response);
                con.close();
}
}catch(Exception e){System.out.println(e);}
        

        
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
    /*@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }*/

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
