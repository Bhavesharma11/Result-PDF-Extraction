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
public class SubjectOR extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
                try{  
		Class.forName("com.mysql.cj.jdbc.Driver");  
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost/result","root",""); 
                Statement stmt=con.createStatement(); 
                HttpSession session=request.getSession(false);
                int or=(int) session.getAttribute("or");
                String[] subject=new String[or];
                String[] subcode=new String[or];
                String tablename = (String) session.getAttribute("tablename");
                System.out.println("In subjectOR "+tablename);
                for(int i=0;i<or;i++){
                    subject[i]=request.getParameter("subject"+(i+1));
                    subcode[i]=request.getParameter("subcode"+(i+1));
                }
                for(int i=0;i<or;i++){
                   
                stmt.executeUpdate("INSERT INTO `"+tablename+"` (`sub_name`, `sub_code`, `sub_type`) VALUES ('"+subject[i]+"', '"+subcode[i]+"', 'or')");  
                //stmt.executeUpdate("ALTER TABLE `table1` ADD `"+subject[i]+"(OR)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+subject[i]+"(OR_MAX)` VARCHAR(10) NULL DEFAULT NULL,ADD  `"+subject[i]+"_OR(Tot%)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+subject[i]+"_OR(Crd)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+subject[i]+"_OR(Grd)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+subject[i]+"_OR(Gpts)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+subject[i]+"_OR(Cpts)` VARCHAR(10) NULL DEFAULT NULL;");
                }
                stmt.executeUpdate("DELETE FROM "+tablename+" WHERE sub_name IS NULL");
                con.close();
                request.getRequestDispatcher("/message.jsp").forward(request, response);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(SubjectOR.class.getName()).log(Level.SEVERE, null, ex);
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
