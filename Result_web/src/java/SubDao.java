
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author BHAVESH
 */
public class SubDao {  
  
    public static Connection getConnection(){  
        Connection con=null;  
        try{  
            Class.forName("com.mysql.cj.jdbc.Driver");  
            con=DriverManager.getConnection("jdbc:mysql://localhost/result","root","");  
        }catch(Exception e){System.out.println(e);}  
        return con;  
    }  
    public static int save(Sub e,String tablename){  
        int status=0;  
        try{  
            Connection con=SubDao.getConnection();  
            PreparedStatement ps=con.prepareStatement(  
                         "insert into "+tablename+"(sub_name,sub_code,sub_type) values (?,?,?)");  
            ps.setString(1,e.getName());  
            ps.setString(2,e.getCode());  
            ps.setString(3,e.getType());
              
            status=ps.executeUpdate();  
              
            con.close();  
        }catch(Exception ex){ex.printStackTrace();}  
          
        return status;  
    }  
    public static int update(String code,Sub e,String tablename){  
        int status=0;  
        try{  
            Connection con=SubDao.getConnection();
            PreparedStatement ps=con.prepareStatement(  
                         "update "+tablename+" set sub_name=?,sub_code=?,sub_type=? where sub_code=?");  
            ps.setString(1,e.getName());  
            ps.setString(2,e.getCode());  
            ps.setString(3,e.getType());
            ps.setString(4,code);
              
            status=ps.executeUpdate();  
              
            con.close();  
        }catch(Exception ex){ex.printStackTrace();}  
          
        return status;  
    }  
    public static int delete(String sub_code,String tablename){  
        int status=0;  
        try{  
            Connection con=SubDao.getConnection();  
            PreparedStatement ps=con.prepareStatement("delete from "+tablename+" where sub_code=?");  
            ps.setString(1,sub_code);  
            status=ps.executeUpdate();  
              
            con.close();  
        }catch(Exception e){e.printStackTrace();}  
          
        return status;  
    }  
    public static Sub getSubById(String sub_code,String tablename){  
        Sub e=new Sub();  
          
        try{  
            Connection con=SubDao.getConnection();  
            PreparedStatement ps=con.prepareStatement("select * from "+tablename+" where sub_code=?");  
            ps.setString(1,sub_code);  
            ResultSet rs=ps.executeQuery();  
            if(rs.next()){  
                e.setName(rs.getString(1));  
                e.setCode(rs.getString(2));  
                e.setType(rs.getString(3));  
            }  
            con.close();  
        }catch(Exception ex){ex.printStackTrace();}  
          
        return e;  
    }  
    public static List<Sub> getAllSub(String tablename){  
        List<Sub> list=new ArrayList<Sub>();  
          
        try{  
            Connection con=SubDao.getConnection();  
            PreparedStatement ps=con.prepareStatement("select * from "+tablename);  
            ResultSet rs=ps.executeQuery();  
            while(rs.next()){  
                Sub e=new Sub();  
                e.setName(rs.getString(1));  
                e.setCode(rs.getString(2));  
                e.setType(rs.getString(3));    
                list.add(e);  
            }  
            con.close();  
        }catch(Exception e){e.printStackTrace();}  
          
        return list;  
    }  
}  
