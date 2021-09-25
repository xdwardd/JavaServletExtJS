package forUsers;


import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/sampleServlet")
public class sampleServlet extends HttpServlet{

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request1, HttpServletResponse response1) throws ServletException, IOException
    {
        response1.setContentType("application/json");
        PrintWriter out=response1.getWriter();
        String spageid1=request1.getParameter("page");
        int pageid1=Integer.parseInt(spageid1);
        int total1=4;
        if(pageid1==1){}
        else{
            pageid1=pageid1-1;
            pageid1=pageid1*total1+1;
        }
        List<userData> list=getRecords(pageid1,total1);


        for(userData e:list){

            out.println(e.getUser_id());
            out.println(e.getFirstname());
            out.println(e.getLastname());
            out.println(e.getEmail());
            out.println(e.getPhone());
        }

        out.close();
    }


    public static List<userData> getRecords(int start1,int total1){
        List<userData> list=new ArrayList<userData>();
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crud", "root", "");
            PreparedStatement pst=conn.prepareStatement("select * from users limit "+(start1-1)+","+total1);
            ResultSet rs=pst.executeQuery();

           while(rs.next()){
                userData e=new userData();
                e.setUser_id(rs.getInt(1));
                e.setFirstname(rs.getString(2));
                e.setLastname(rs.getString(3));
                e.setEmail(rs.getString(4));
                e.setPhone(rs.getString(5));
                list.add(e);
            }

            conn.close();
        }catch(Exception e){System.out.println(e);}
        return list;
    }

}
