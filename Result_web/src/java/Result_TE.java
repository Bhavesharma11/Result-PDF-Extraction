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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author BHAVESH
 */
public class Result_TE extends HttpServlet {

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
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `result`.`"+result_tablename+"` ( `PRN` VARCHAR(20) NOT NULL , `Seat_No` VARCHAR(20) NULL DEFAULT NULL , `Student_Name` VARCHAR(60) NULL DEFAULT NULL) ENGINE = InnoDB;");
            
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
        String part[] = null;
        String part1[]= PDF.split("..........CONFIDENTIAL- FOR VERIFICATION AND RECORD ONLY AT COLLEGE, NOT FOR DISTRIBUTION...........");
         System.out.println("Splitted");
         
         int c=0;
         
         String sub_code,sub_type,sub_name;
         String ise_sub,ese_sub,total_sub,tw_sub,pr_sub,or_sub;
         String ise_marks[]=null,ese_marks[]=null,total_marks[]=null,tw_marks[]=null,pr_marks[]=null,or_marks[]=null;
         String msg ="NA";
         
         for(String s:part1)
         {
             //FINAL CLEANING
             firstcharloc = s.indexOf("T1505");
             String temp = s.substring(firstcharloc);
             finalpart.add(temp.trim());
             System.out.println("\n\n"+finalpart.get(c));
             System.out.println("Executing "+c);
             
             //EXTRACTING SEAT NO, NAME, PRN, SGPA, CREDIT
             String templines[] = finalpart.get(c).split("\\r?\\n");
             String temp1 = templines[0];
             System.out.println(temp1);
             
                String[] tempwords = temp1.split("\\s{2,}");
                for(String e:tempwords)
                    System.out.println(e);
                
                String seatno=tempwords[0];
                String name=tempwords[1];
                String prn=tempwords[tempwords.length-1];
                Pattern p = Pattern.compile("[1-9]+");
                Matcher m = p.matcher(prn);
                if (m.find()) {
                    int position = m.start();
                    prn = prn.substring(position,prn.indexOf("IND")).trim();
                }
                
                int sp = finalpart.get(c).indexOf("SGPA");
                String sgpa=finalpart.get(c).substring(sp+6,finalpart.get(c).indexOf(',',sp));
                
                int sp1 = finalpart.get(c).indexOf("EARNED");
                String credits=finalpart.get(c).substring(sp1+8);
                
                System.out.println("seatno "+seatno);
                System.out.println("name "+name);
                System.out.println("prn "+prn);
                System.out.println("sgpa "+sgpa);
                System.out.println("credits "+credits);
                
                
                String query = "INSERT INTO `"+result_tablename+"` (`PRN`,`Seat_No`,`Student_Name`,`(sgpa)`,`(credit)`) VALUES (?,?,?,?,?)";
                System.out.println("going to insert");
                pst = con.prepareStatement(query);
                pst.setString(1, prn);
                pst.setString(2, seatno);
                pst.setString(3, name);
                pst.setString(4, sgpa);
                pst.setString(5, credits);
                pst.execute();
                System.out.println("inserted");
             
             
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
                    if(sub_type.equalsIgnoreCase("pr") && sub_code.equals("310257") && line.contains(sub_code))
                    {
                        flag++;
                        if(flag==2)
                            break;
                    }
                    
                    else if(sub_type.equalsIgnoreCase("or") && sub_code.equals("310246") && line.contains(sub_code))
                    {
                        flag++;
                        if(flag==2)
                            break;
                    }
                            
                    else if(line.contains(sub_code)){
                        break;
                    }
                }
                
                if(cnt==lines.length)
                {
                    //DO NOTHING AND MARK FIELD AS NA(NOT APPLICABLE)
                    if(sub_type.equalsIgnoreCase("th"))
                    {
                    pst=con.prepareStatement("UPDATE `"+result_tablename+"` SET `"+sub_name+"(ISE)`='"+ msg+"',`"+sub_name+"(ISE_MAX)`='"+ msg+"',`"+sub_name+"(ESE)`='"+ msg+"',`"+sub_name+"(ESE_MAX)`='"+ msg+"',`"+sub_name+"(TOTAL)`='"+ msg+"',`"+sub_name+"(TOTAL_MAX)`='"+ msg+"' WHERE Seat_No='"+seatno+"'");
                    pst.executeUpdate();
                    pst=con.prepareStatement("UPDATE `"+result_tablename+"` SET `"+sub_name+"_TH(Crd)`='"+ msg+"',`"+sub_name+"_TH(Grd)`='"+ msg+"',`"+sub_name+"_TH(Gpts)`='"+ msg+"', `"+sub_name+"_TH(Cpts)`='"+ msg+"' WHERE Seat_No='"+seatno+"'");
                    pst.executeUpdate();
                    }
                
                    if(sub_type.equalsIgnoreCase("tw"))
                    {
                    pst=con.prepareStatement("UPDATE `"+result_tablename+"` SET `"+sub_name+"(TW)`='"+ msg+"',`"+sub_name+"(TW_MAX)`='"+ msg+"' WHERE Seat_No='"+seatno+"'");
                    pst.executeUpdate();
                    pst=con.prepareStatement("UPDATE `"+result_tablename+"` SET `"+sub_name+"_TW(Crd)`='"+ msg+"',`"+sub_name+"_TW(Grd)`='"+ msg+"',`"+sub_name+"_TW(Gpts)`='"+ msg+"', `"+sub_name+"_TW(Cpts)`='"+ msg+"' WHERE Seat_No='"+seatno+"'");
                    pst.executeUpdate();
                    }
                
                    if(sub_type.equalsIgnoreCase("pr"))
                    {
                    pst=con.prepareStatement("UPDATE `"+result_tablename+"` SET `"+sub_name+"(PR)`='"+ msg+"',`"+sub_name+"(PR_MAX)`='"+ msg+"' WHERE Seat_No='"+seatno+"'");
                    pst.executeUpdate();
                    pst=con.prepareStatement("UPDATE `"+result_tablename+"` SET `"+sub_name+"_PR(Crd)`='"+ msg+"',`"+sub_name+"_PR(Grd)`='"+ msg+"',`"+sub_name+"_PR(Gpts)`='"+ msg+"', `"+sub_name+"_PR(Cpts)`='"+ msg+"' WHERE Seat_No='"+seatno+"'");
                    pst.executeUpdate(); 
                    }
                    
                    if(sub_type.equalsIgnoreCase("or"))
                    {
                    pst=con.prepareStatement("UPDATE `"+result_tablename+"` SET `"+sub_name+"(OR)`='"+ msg+"',`"+sub_name+"(OR_MAX)`='"+ msg+"' WHERE Seat_No='"+seatno+"'");
                    pst.executeUpdate();
                    pst=con.prepareStatement("UPDATE `"+result_tablename+"` SET `"+sub_name+"_OR(Crd)`='"+ msg+"',`"+sub_name+"_OR(Grd)`='"+ msg+"',`"+sub_name+"_OR(Gpts)`='"+ msg+"', `"+sub_name+"_OR(Cpts)`='"+ msg+"' WHERE Seat_No='"+seatno+"'");
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
		if(elements[i]==null || elements[i].equals('\0') || elements[i].equals(sub_code) || elements[i].equals("*"))
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
                    pst=con.prepareStatement("UPDATE `"+result_tablename+"` SET `"+sub_name+"(ISE)`='"+ ise_marks[0]+"',`"+sub_name+"(ISE_MAX)`='"+ ise_marks[1]+"',`"+sub_name+"(ESE)`='"+ ese_marks[0]+"',`"+sub_name+"(ESE_MAX)`='"+ ese_marks[1]+"',`"+sub_name+"(TOTAL)`='"+ total_marks[0]+"',`"+sub_name+"(TOTAL_MAX)`='"+ total_marks[1]+"' WHERE Seat_No='"+seatno+"'");
                    pst.executeUpdate();
                    pst=con.prepareStatement("UPDATE `"+result_tablename+"` SET `"+sub_name+"(TOTAL_%)_TH`='"+ marks.get(7)+"',`"+sub_name+"_TH(Crd)`='"+ marks.get(8)+"',`"+sub_name+"_TH(Grd)`='"+ marks.get(9)+"',`"+sub_name+"_TH(Gpts)`='"+ marks.get(10)+"', `"+sub_name+"_TH(Cpts)`='"+ marks.get(11)+"' WHERE Seat_No='"+seatno+"'");
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
                    
                    pst=con.prepareStatement("UPDATE `"+result_tablename+"` SET `"+sub_name+"(TW)`='"+ tw_marks[0]+"',`"+sub_name+"(TW_MAX)`='"+ tw_marks[1]+"' WHERE Seat_No='"+seatno+"'");
                    pst.executeUpdate();
                    pst=con.prepareStatement("UPDATE `"+result_tablename+"` SET `"+sub_name+"(TOTAL_%)_TW`='"+ marks.get(7)+"',`"+sub_name+"_TW(Crd)`='"+ marks.get(8)+"',`"+sub_name+"_TW(Grd)`='"+ marks.get(9)+"',`"+sub_name+"_TW(Gpts)`='"+ marks.get(10)+"', `"+sub_name+"_TW(Cpts)`='"+ marks.get(11)+"' WHERE Seat_No='"+seatno+"'");
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
                    pst=con.prepareStatement("UPDATE `"+result_tablename+"` SET `"+sub_name+"(PR)`='"+ pr_marks[0]+"',`"+sub_name+"(PR_MAX)`='"+ pr_marks[1]+"' WHERE Seat_No='"+seatno+"'");
                    pst.executeUpdate();
                    pst=con.prepareStatement("UPDATE `"+result_tablename+"` SET `"+sub_name+"(TOTAL_%)_PR`='"+ marks.get(7)+"',`"+sub_name+"_PR(Crd)`='"+ marks.get(8)+"',`"+sub_name+"_PR(Grd)`='"+ marks.get(9)+"',`"+sub_name+"_PR(Gpts)`='"+ marks.get(10)+"', `"+sub_name+"_PR(Cpts)`='"+ marks.get(11)+"' WHERE Seat_No='"+seatno+"'");
                    pst.executeUpdate(); 
                    System.out.println("\n pr value updated");
                }
                
                if(sub_type.equalsIgnoreCase("or"))
                {
                    or_sub = marks.get(6);
                    if(or_sub.contains("/"))
                    {
                        or_marks = or_sub.split("/");
                    }
                    pst=con.prepareStatement("UPDATE `"+result_tablename+"` SET `"+sub_name+"(OR)`='"+ or_marks[0]+"',`"+sub_name+"(OR_MAX)`='"+ or_marks[1]+"' WHERE Seat_No='"+seatno+"'");
                    pst.executeUpdate();
                    pst=con.prepareStatement("UPDATE `"+result_tablename+"` SET `"+sub_name+"(TOTAL_%)_OR`='"+ marks.get(7)+"',`"+sub_name+"_OR(Crd)`='"+ marks.get(8)+"',`"+sub_name+"_OR(Grd)`='"+ marks.get(9)+"',`"+sub_name+"_OR(Gpts)`='"+ marks.get(10)+"', `"+sub_name+"_OR(Cpts)`='"+ marks.get(11)+"' WHERE Seat_No='"+seatno+"'");
                    pst.executeUpdate(); 
                    System.out.println("\n or value updated");
                }
                }
                
            }
                
             c++;
         }
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
