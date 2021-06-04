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
public class SubjectTH extends HttpServlet {
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");  
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost/result","root",""); 
                Statement stmt=con.createStatement();
                HttpSession session=request.getSession(false);
                int th=(int) session.getAttribute("th");
                int tw=(int) session.getAttribute("tw");
                int pr=(int) session.getAttribute("pr");
                int or=(int) session.getAttribute("or");
                PrintWriter out=response.getWriter();
                String[] subject=new String[th];
                String[] subcode=new String[th];
                String tablename = (String) session.getAttribute("tablename");
                System.out.println("In subjectTH "+tablename);
                for(int i=0;i<th;i++){
                    subject[i]=request.getParameter("subject"+(i+1));
                    subcode[i]=request.getParameter("subcode"+(i+1));
                }
                 for(int i=0;i<th;i++){
                  stmt.executeUpdate("INSERT INTO `"+tablename+"` (`sub_name`, `sub_code`, `sub_type`) VALUES ('"+subject[i]+"', '"+subcode[i]+"', 'th')"); 
                
                 //stmt.executeUpdate("ALTER TABLE `table1` ADD `"+subject[i]+"(ISE)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+subject[i]+"(ISE_MAX)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+subject[i]+"(ESE)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+subject[i]+"(ESE_MAX)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+subject[i]+"(TOTAL)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+subject[i]+"(TOTAL_MAX)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+subject[i]+"_TH(Tot%)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+subject[i]+"_TH(Crd)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+subject[i]+"_TH(Grd)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+subject[i]+"_TH(Gpts)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+subject[i]+"_TH(Cpts)` VARCHAR(10) NULL DEFAULT NULL;");
                 }
                 con.close();
                if(tw>0){
                    request.getRequestDispatcher("/termwork.jsp").forward(request, response);
                }
                else if(pr>0){
                    request.getRequestDispatcher("/practical.jsp").forward(request, response);
                }
                else if(or>0){
                    request.getRequestDispatcher("/oral.jsp").forward(request, response);
                }
                else{
                    request.getRequestDispatcher("/message.jsp").forward(request, response);
                }
                } 
        catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(SubjectTH.class.getName()).log(Level.SEVERE, null, ex);
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
