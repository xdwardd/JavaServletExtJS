package test;
import org.json.JSONObject;
import test.utils.BCrypt;

import java.io.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import java.sql.*;
import java.util.ArrayList;

@WebServlet("/ajaxServlet")
public class ajaxServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

//Getting the data from database
protected void doGet(HttpServletRequest request, HttpServletResponse response)

    throws ServletException, IOException {
    response.setContentType("text/json; charset=utf-8");
    PrintWriter out = response.getWriter();


    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    try {

        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crud?useSSL=false", "root", "");
        pst = conn.prepareStatement("SELECT * FROM admins"); //Create a Statement object
        rs = pst.executeQuery();
        try {
            ArrayList<JSONObject> arrayList = new ArrayList<>();
            JSONObject jsonobj = new JSONObject();
            while (rs.next()) {
                jsonobj.put("admin_id", rs.getInt(1));
                jsonobj.put("admin_user", rs.getString(2));
                jsonobj.put("admin_password", rs.getString(3));
                jsonobj.put("created_at", rs.getTimestamp(4));
                jsonobj.put("role",rs.getString(5));
                jsonobj.put("secret_key",rs.getString(6));
                arrayList.add(jsonobj);
                jsonobj = new JSONObject();
            }
            out = response.getWriter();
            out.println(arrayList);
        } catch (Exception e) {
            out.print("connection error!");
            e.printStackTrace();
        }

    } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
    }finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {}

            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException e) {}

            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {}
        }
        return;
    }
}