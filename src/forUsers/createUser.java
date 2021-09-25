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

@WebServlet("/createUser")

public class createUser extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try{

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crud", "root","");
            pst = conn.prepareStatement("INSERT INTO users (firstname,lastname,email,phone) VALUES (?,?,?,?)");
            pst.setString(1, firstname);
            pst.setString(2, lastname);
            pst.setString(3, email);
            pst.setString(4, phone);
            pst.executeUpdate();

            JSONObject object = new JSONObject();
            object.put("success", true);
            object.put("reason", "Successfully Added");
            out.println(object);


        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }finally {
            try{
                if(conn != null){
                    conn.close();
                }
            }catch (SQLException e){}

            try {
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
