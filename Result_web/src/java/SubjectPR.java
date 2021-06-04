/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author BHAVESH
 */
public class SubjectPR extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try{  
		Class.forName("com.mysql.cj.jdbc.Driver");  
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost/result","root",""); 
                Statement stmt=con.createStatement(); 
                HttpSession session=request.getSession(false);
                int pr=(int) session.getAttribute("pr");
                int or=(int) session.getAttribute("or");
                String[] subject=new String[pr];
                String[] subcode=new String[pr];
                String tablename = (String) session.getAttribute("tablename");
                System.out.println("In subjectPR "+tablename);
                for(int i=0;i<pr;i++){
                    subject[i]=request.getParameter("subject"+(i+1));
                    subcode[i]=request.getParameter("subcode"+(i+1));
                }
                for(int i=0;i<pr;i++){
                  
                stmt.executeUpdate("INSERT INTO `"+tablename+"` (`sub_name`, `sub_code`, `sub_type`) VALUES ('"+subject[i]+"', '"+subcode[i]+"', 'pr')");               
                //stmt.executeUpdate("ALTER TABLE `table1` ADD `"+subject[i]+"(PR)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+subject[i]+"(PR_MAX)` VARCHAR(10) NULL DEFAULT NULL,ADD  `"+subject[i]+"_PR(Tot%)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+subject[i]+"_PR(Crd)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+subject[i]+"_PR(Grd)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+subject[i]+"_PR(Gpts)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+subject[i]+"_PR(Cpts)` VARCHAR(10) NULL DEFAULT NULL;");
               
                }
                con.close();
                if(or>0){
                    request.getRequestDispatcher("/oral.jsp").forward(request, response);
                }
                else{
                    request.getRequestDispatcher("/message.jsp").forward(request, response);
                }
                
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(SubjectPR.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    /*@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }*/

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

}
