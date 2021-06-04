/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author BHAVESH
 */
public class Result_FE extends HttpServlet {

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
        PrintWriter out=response.getWriter();
        
        String from=request.getParameter("from");
        String to=request.getParameter("to");
        String path=request.getParameter("fileup");
        
        HttpSession session=request.getSession(false);
        String tablename = (String) session.getAttribute("tablename");
        String result_tablename = (String) session.getAttribute("result_tablename");
        
        out.println("<h3><a href='LogoutServlet'>Logout</a></h3>");
        out.println("<hr/>");
        out.println("<br><br><br><br><br>");
        out.println("<center><h1>Result stored successfully</h1></center>");
        
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");  
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost/result","root","");
            
            Statement stmt=con.createStatement();
            
            stmt.executeUpdate("USE result");
            stmt.executeUpdate("DROP TABLE IF EXISTS "+result_tablename);
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `result`.`"+result_tablename+"` ( `PRN` VARCHAR(20) NOT NULL , `Seat_No` VARCHAR(20) NULL DEFAULT NULL , `Student_Name` VARCHAR(60) NULL DEFAULT NULL , `Mother_Name` VARCHAR(60) NULL DEFAULT NULL ) ENGINE = InnoDB;");
            
            //CREATING MARKS FIELDS FOR THEORY 
            PreparedStatement ps1=con.prepareStatement("select sub_name from "+tablename+" where sub_type=?");
            ps1.setString(1, "th");
            ResultSet rs1 = ps1.executeQuery();
            while(rs1.next())
            {
                String name = rs1.getString("sub_name");
                stmt.executeUpdate("ALTER TABLE `"+result_tablename+"` ADD `"+name+"(ISE)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+name+"(ISE_MAX)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+name+"(ESE)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+name+"(ESE_MAX)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+name+"(TOTAL)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+name+"(TOTAL_MAX)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+name+"(TOTAL_%)_TH` VARCHAR(10) NULL DEFAULT NULL,ADD `"+name+"_TH(Crd)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+name+"_TH(Grd)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+name+"_TH(Gpts)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+name+"_TH(Cpts)` VARCHAR(10) NULL DEFAULT NULL;");
            }
            
            //CREATING MARKS FIELDS FOR TERMWORK
            PreparedStatement ps2=con.prepareStatement("select sub_name from "+tablename+" where sub_type=?");
            ps2.setString(1, "tw");
            ResultSet rs2 = ps2.executeQuery();
            while(rs2.next())
            {
                String name = rs2.getString("sub_name");
                stmt.executeUpdate("ALTER TABLE `"+result_tablename+"` ADD `"+name+"(TW)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+name+"(TW_MAX)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+name+"(TOTAL_%)_TW` VARCHAR(10) NULL DEFAULT NULL,ADD `"+name+"_TW(Crd)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+name+"_TW(Grd)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+name+"_TW(Gpts)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+name+"_TW(Cpts)` VARCHAR(10) NULL DEFAULT NULL;");
            }
            
            //CREATING MARKS FIELDS FOR PRACTICAL
            PreparedStatement ps3=con.prepareStatement("select sub_name from "+tablename+" where sub_type=?");
            ps3.setString(1, "pr");
            ResultSet rs3 = ps3.executeQuery();
            while(rs3.next())
            {
                String name = rs3.getString("sub_name");
                stmt.executeUpdate("ALTER TABLE `"+result_tablename+"` ADD `"+name+"(PR)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+name+"(PR_MAX)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+name+"(TOTAL_%)_PR` VARCHAR(10) NULL DEFAULT NULL,ADD `"+name+"_PR(Crd)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+name+"_PR(Grd)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+name+"_PR(Gpts)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+name+"_PR(Cpts)` VARCHAR(10) NULL DEFAULT NULL;");
            }
            
            //CREATING MARKS FIELDS FOR ORAL
            PreparedStatement ps4=con.prepareStatement("select sub_name from "+tablename+" where sub_type=?");
            ps4.setString(1, "or");
            ResultSet rs4 = ps4.executeQuery();
            while(rs4.next())
            {
                String name = rs4.getString("sub_name");
                stmt.executeUpdate("ALTER TABLE `"+result_tablename+"` ADD `"+name+"(OR)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+name+"(OR_MAX)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+name+"(TOTAL_%)_OR` VARCHAR(10) NULL DEFAULT NULL,ADD `"+name+"_OR(Crd)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+name+"_OR(Grd)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+name+"_OR(Gpts)` VARCHAR(10) NULL DEFAULT NULL,ADD `"+name+"_OR(Cpts)` VARCHAR(10) NULL DEFAULT NULL;");
            }
            
           stmt.executeUpdate("ALTER TABLE `"+result_tablename+"` ADD `(sgpa)` VARCHAR(10) NULL DEFAULT NULL;");
           stmt.executeUpdate("ALTER TABLE `"+result_tablename+"` ADD `(credit)` VARCHAR(10) NULL DEFAULT NULL;");
            
            
           // -------------------MAIN EXTRACTION LOGIC-----------------
           
        String PDF = "";
        ArrayList<String> finalpart=new ArrayList<String>();
        int firstcharloc,secondcharloc;
        String lin=System.lineSeparator();
        PdfReader pdfReader = new PdfReader(path);
        int pages = pdfReader.getNumberOfPages();
        String pageContent = null;
        for (int i = 1; i <= pages; i++) {
            pageContent = PdfTextExtractor.getTextFromPage(pdfReader, i);
            String s = pageContent+lin;
            PDF = PDF + s;
        }
         System.out.println("Reading complete");
        PreparedStatement pst = null;
        String part1[]= PDF.split("..........CONFIDENTIAL- FOR VERIFICATION AND RECORD ONLY AT COLLEGE, NOT FOR DISTRIBUTION........................................");
         System.out.println("Splitted");
         
         for(String s : part1)
         {
             System.out.println(s);
             System.out.println("next");
         }
         
        
         int c=0;
         String sub_code,sub_type,sub_name;
         String ise_sub,ese_sub,total_sub,tw_sub,pr_sub;
         String ise_marks[]=null,ese_marks[]=null,total_marks[]=null,tw_marks[]=null,pr_marks[]=null;
         String ise_max="30",ese_max="70",total_max="100",tw_max="25",pr_max="25",msg="NA";
            for (String s : part1) {
                //REDUCING AND TRIMMING INDIVIDUAL STUDENT RECORD
                firstcharloc = s.indexOf("SEAT");
                String temp = s.substring(firstcharloc);
                finalpart.add(temp.trim());
                System.out.println("\n\n"+finalpart.get(c));
                System.out.println("Executing "+c);
                
                //EXTRACTING PRN
                int sp1=finalpart.get(c).indexOf("PRN");
                int endp=finalpart.get(c).indexOf("CLG");
                String key1=finalpart.get(c).substring(sp1+5,endp);
                key1 = key1.trim();
                int chkindex1 = PDF.indexOf(key1);
                
                //EXTRACTING SEAT NO.
                int sp2=finalpart.get(c).indexOf("SEAT");
                String key2=finalpart.get(c).substring(sp2+9,sp2+20);
                key2 = key2.trim();
                int chkindex2 = PDF.indexOf(key2);
                
                //EXTRACTING NAME AND MOTHER'S NAME
                int sp3=finalpart.get(c).indexOf("NAME");
                int sp4 = finalpart.get(c).indexOf("MOTHER");
                String key3=finalpart.get(c).substring(sp3+6,sp4-1);
                String key4=finalpart.get(c).substring(sp4+8,sp1-1);
                key3 = key3.trim();
                key4 = key4.trim();
                int chkindex3 = PDF.indexOf(key3);
                int chkindex4 = PDF.indexOf(key4);
                
                
                //EXTRACTING SGPA AND CREDITS
                int sp5 = finalpart.get(c).indexOf("SGPA");
                String key5=finalpart.get(c).substring(sp5+6,finalpart.get(c).indexOf(',',sp5));
                int sp6 = finalpart.get(c).indexOf("EARNED");
                String key6=finalpart.get(c).substring(sp6+8);
                key5 = key5.trim();
                key6 = key6.trim();
                int chkindex5 = PDF.indexOf(key5);
                int chkindex6 = PDF.indexOf(key6);
                
        
            if (chkindex1 != -1 && chkindex2 != -1 && chkindex3 != -1 && chkindex4 != -1 && chkindex5 != -1 && chkindex6 != -1) {
                String query = "INSERT INTO `"+result_tablename+"` (`PRN`,`Seat_No`,`Student_Name`,`Mother_Name`,`(sgpa)`,`(credit)`) VALUES (?,?,?,?,?,?)";
                System.out.println("going to insert");
                pst = con.prepareStatement(query);
                pst.setString(1, key1);
                pst.setString(2, key2);
                pst.setString(3, key3);
                pst.setString(4, key4);
                pst.setString(5, key5);
                pst.setString(6, key6);
                pst.execute();
                System.out.println("inserted");
            }
            
            ResultSet rs=stmt.executeQuery("select * from "+tablename);
            while(rs.next())
            {
                sub_code=rs.getString("sub_code");
                sub_type=rs.getString("sub_type");
                sub_name=rs.getString("sub_name");
                 System.out.println("\n Data read from table");
                int cnt=0,flag=0;
                String lines[] = finalpart.get(c).split("\\r?\\n");
                System.out.println("\n Splitted by individual lines");
                for (String line : lines) {
                            cnt++;
                    if(sub_type.equalsIgnoreCase("th") &&  line.contains(sub_code)){
                        break;
                    }
                    
                    else if(sub_code.equals("111006") && line.contains(sub_code)){
                        break;
                    }
                    
                    else if(sub_code.equals("110013") && line.contains(sub_code)){
                        break;
                    }
                    
                    else if(line.contains(sub_code))
                    {
                        flag++;
                        if(flag==2)
                            break;
                    }
                }
                
                if(cnt==lines.length)
                {
                    //DO NOTHING AND MARK FIELD AS NA(NOT APPLICABLE)
                    if(sub_type.equalsIgnoreCase("th"))
                    {
                    pst=con.prepareStatement("UPDATE `"+result_tablename+"` SET `"+sub_name+"(ISE)`='"+ msg+"',`"+sub_name+"(ISE_MAX)`='"+ msg+"',`"+sub_name+"(ESE)`='"+ msg+"',`"+sub_name+"(ESE_MAX)`='"+ msg+"',`"+sub_name+"(TOTAL)`='"+ msg+"',`"+sub_name+"(TOTAL_MAX)`='"+ msg+"' WHERE Seat_No='"+key2+"'");
                    pst.executeUpdate();
                    pst=con.prepareStatement("UPDATE `"+result_tablename+"` SET `"+sub_name+"_TH(Crd)`='"+ msg+"',`"+sub_name+"_TH(Grd)`='"+ msg+"',`"+sub_name+"_TH(Gpts)`='"+ msg+"', `"+sub_name+"_TH(Cpts)`='"+ msg+"' WHERE Seat_No='"+key2+"'");
                    pst.executeUpdate();
                    }
                
                    if(sub_type.equalsIgnoreCase("tw"))
                    {
                    pst=con.prepareStatement("UPDATE `"+result_tablename+"` SET `"+sub_name+"(TW)`='"+ msg+"',`"+sub_name+"(TW_MAX)`='"+ msg+"' WHERE Seat_No='"+key2+"'");
                    pst.executeUpdate();
                    pst=con.prepareStatement("UPDATE `"+result_tablename+"` SET `"+sub_name+"_TW(Crd)`='"+ msg+"',`"+sub_name+"_TW(Grd)`='"+ msg+"',`"+sub_name+"_TW(Gpts)`='"+ msg+"', `"+sub_name+"_TW(Cpts)`='"+ msg+"' WHERE Seat_No='"+key2+"'");
                    pst.executeUpdate();
                    }
                
                    if(sub_type.equalsIgnoreCase("pr"))
                    {
                    pst=con.prepareStatement("UPDATE `"+result_tablename+"` SET `"+sub_name+"(PR)`='"+ msg+"',`"+sub_name+"(PR_MAX)`='"+ msg+"' WHERE Seat_No='"+key2+"'");
                    pst.executeUpdate();
                    pst=con.prepareStatement("UPDATE `"+result_tablename+"` SET `"+sub_name+"_PR(Crd)`='"+ msg+"',`"+sub_name+"_PR(Grd)`='"+ msg+"',`"+sub_name+"_PR(Gpts)`='"+ msg+"', `"+sub_name+"_PR(Cpts)`='"+ msg+"' WHERE Seat_No='"+key2+"'");
                    pst.executeUpdate(); 
                    }
                }
                else
                {
                    String[] elements = lines[cnt-1].split("\\s+");
                 System.out.println("\n Splitted by spaces");
                 
                 ArrayList<String> marks = new ArrayList<String>();
                for(int i=0;i<elements.length;i++)
                {
                    System.out.println(i+"="+elements[i]);
		if(elements[i]==null || elements[i].equals('\0') || elements[i].equals(sub_code) || elements[i].equals("&") || elements[i].equals("*") || elements[i].equals("IN") || elements[i].equals("I") || elements[i].equals("II") || elements[i].matches("[a-zA-z\\.]{3,}"))
		{
			//DO NOTHING
		}
		else
		{
			marks.add(elements[i].trim());
		}
                }
                 
                
                for(int i=0;i<marks.size();i++)
                    System.out.println(i+"="+marks.get(i));
                
                if(sub_type.equalsIgnoreCase("th"))
                {
                    ise_sub = marks.get(1);
                    ese_sub = marks.get(2);
                    total_sub = marks.get(3);
                    
                    if(ise_sub.contains("/"))
                    {
                        ise_marks = ise_sub.split("/");
                    }
                    if(ese_sub.contains("/"))
                    {
                        ese_marks = ese_sub.split("/");
                    }
                    if(total_sub.contains("/"))
                    {
                        total_marks = total_sub.split("/");
                    }
                    pst=con.prepareStatement("UPDATE `"+result_tablename+"` SET `"+sub_name+"(ISE)`='"+ ise_marks[0]+"',`"+sub_name+"(ISE_MAX)`='"+ ise_marks[1]+"',`"+sub_name+"(ESE)`='"+ ese_marks[0]+"',`"+sub_name+"(ESE_MAX)`='"+ ese_marks[1]+"',`"+sub_name+"(TOTAL)`='"+ total_marks[0]+"',`"+sub_name+"(TOTAL_MAX)`='"+ total_marks[1]+"' WHERE Seat_No='"+key2+"'");
                    pst.executeUpdate();
                    pst=con.prepareStatement("UPDATE `"+result_tablename+"` SET `"+sub_name+"(TOTAL_%)_TH`='"+ marks.get(marks.size()-7)+"',`"+sub_name+"_TH(Crd)`='"+ marks.get(marks.size()-6)+"',`"+sub_name+"_TH(Grd)`='"+ marks.get(marks.size()-5)+"',`"+sub_name+"_TH(Gpts)`='"+ marks.get(marks.size()-4)+"', `"+sub_name+"_TH(Cpts)`='"+ marks.get(marks.size()-3)+"' WHERE Seat_No='"+key2+"'");
                    pst.executeUpdate();
                    System.out.println("\n th value updated");
                }
                
                if(sub_type.equalsIgnoreCase("tw"))
                {
                    tw_sub = marks.get(4);
                    if(tw_sub.contains("/"))
                    {
                        tw_marks = tw_sub.split("/");
                    }
                    
                    pst=con.prepareStatement("UPDATE `"+result_tablename+"` SET `"+sub_name+"(TW)`='"+ tw_marks[0]+"',`"+sub_name+"(TW_MAX)`='"+ tw_marks[1]+"' WHERE Seat_No='"+key2+"'");
                    pst.executeUpdate();
                    pst=con.prepareStatement("UPDATE `"+result_tablename+"` SET `"+sub_name+"(TOTAL_%)_TW`='"+ marks.get(marks.size()-7)+"',`"+sub_name+"_TW(Crd)`='"+ marks.get(marks.size()-6)+"',`"+sub_name+"_TW(Grd)`='"+ marks.get(marks.size()-5)+"',`"+sub_name+"_TW(Gpts)`='"+ marks.get(marks.size()-4)+"', `"+sub_name+"_TW(Cpts)`='"+ marks.get(marks.size()-3)+"' WHERE Seat_No='"+key2+"'");
                    pst.executeUpdate();
                    System.out.println("\n tw value updated");
                }
                
                if(sub_type.equalsIgnoreCase("pr"))
                {
                    pr_sub = marks.get(5);
                    if(pr_sub.contains("/"))
                    {
                        pr_marks = pr_sub.split("/");
                    }
                    pst=con.prepareStatement("UPDATE `"+result_tablename+"` SET `"+sub_name+"(PR)`='"+ pr_marks[0]+"',`"+sub_name+"(PR_MAX)`='"+ pr_marks[1]+"' WHERE Seat_No='"+key2+"'");
                    pst.executeUpdate();
                    pst=con.prepareStatement("UPDATE `"+result_tablename+"` SET `"+sub_name+"(TOTAL_%)_PR`='"+ marks.get(marks.size()-7)+"',`"+sub_name+"_PR(Crd)`='"+ marks.get(marks.size()-6)+"',`"+sub_name+"_PR(Grd)`='"+ marks.get(marks.size()-5)+"',`"+sub_name+"_PR(Gpts)`='"+ marks.get(marks.size()-4)+"', `"+sub_name+"_PR(Cpts)`='"+ marks.get(marks.size()-3)+"' WHERE Seat_No='"+key2+"'");
                    pst.executeUpdate(); 
                    System.out.println("\n pr value updated");
                }
                }
                
            }
                c++;
            }
            
            con.close();
           
        }
        catch(Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
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
