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

@WebServlet("/uploadGrid")


public class uploadGrid extends HttpServlet {

    public static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crud", "root", "");
            pst = conn.prepareStatement("SELECT * FROM sampleuser");
            rs = pst.executeQuery();

            try{

                ArrayList <JSONObject> arrayList = new ArrayList<>();
                JSONObject object = new JSONObject();

                while (rs.next()){
                    object.put("user_id", rs.getInt(1));
                    object.put("fname", rs.getString(2));
                    object.put("lname", rs.getString(3));
                    object.put("mi", rs.getString(4));
                    object.put("birthdate", rs.getString(5));
                    object.put("photo", rs.getString(6));
                    object.put("username", rs.getString(7));
                    object.put("password",rs.getString(8));
                    object.put("created_by", rs.getInt(9));

                    arrayList.add(object);
                    object = new JSONObject();
                }

                out = response.getWriter();
                out.println(arrayList);

            }catch (Exception e){
                e.printStackTrace();
            }

        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }finally {
            try {
                if (conn != null){
                    conn.close();
                }
            }catch(SQLException e){}

            try{
                if (pst != null){
                    pst.close();
                }
            }catch (SQLException e){}

            try {
                if(rs != null){
                    rs.close();
                }
            }catch (SQLException e){}
        }
        return;

    }
}
