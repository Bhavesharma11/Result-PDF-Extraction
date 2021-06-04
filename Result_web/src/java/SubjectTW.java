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
public class SubjectTW extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try{  
		Class.forName("com.mysql.cj.jdbc.Driver");  
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost/result","root",""); 
                Statement stmt=con.createStatement(); 
                HttpSession session=request.getSession(false);
                int tw=(int) session.getAttribute("tw");
                int pr=(int) session.getAttribute("pr");
                int or=(int) session.getAttribute("or");
                PrintWriter out=response.getWriter();
                String[] subject=new String[tw];
                String[] subcode=new String[tw];
                String tablename = (String) session.getAttribute("tablename");
                System.out.println("In subjectTW "+tablename);
                for(int i=0;i<tw;i++){
                    subject[i]=request.getParameter("subject"+(i+1));
                    subcode[i]=request.getParameter("subcode"+(i+1));
                }
                for(int i=0;i<tw;i++){
                                   
                stmt.executeUpdate("INSERT INTO `"+tablename+"` (`sub_name`, `sub_code`, `sub_type`) VALUES ('"+subject[i]+"', '"+subcode[i]+"', 'tw')");               
                //stmt.executeUpdate("ALTER TABLE `table1` ADD `"+subject[i]+"(TW)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+subject[i]+"(TW_MAX)` VARCHAR(10) NULL DEFAULT NULL,ADD  `"+subject[i]+"_TW(Tot%)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+subject[i]+"_TW(Crd)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+subject[i]+"_TW(Grd)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+subject[i]+"_TW(Gpts)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+subject[i]+"_TW(Cpts)` VARCHAR(10) NULL DEFAULT NULL;");
               
                }
                con.close();
                if(pr>0){
                    request.getRequestDispatcher("/practical.jsp").forward(request, response);
                }
                else if(or>0){
                    request.getRequestDispatcher("/oral.jsp").forward(request, response);
                }
                else{
                    request.getRequestDispatcher("/message.jsp").forward(request, response);
                }
                
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(SubjectTW.class.getName()).log(Level.SEVERE, null, ex);
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
