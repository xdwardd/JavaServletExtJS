package test;

import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/delete")
public class Delete extends HttpServlet {

    public static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String sid = request.getParameter("admin_id");
        int id = Integer.parseInt(sid);
        int status = 0;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crud", "root", "");
            pst = conn.prepareStatement("DELETE FROM admins WHERE admin_id=?");
            pst.setInt(1, id);
            status = pst.executeUpdate();

            if (status != 0) {
                JSONObject object = new JSONObject();
                object.put("success", true);
                object.put("reason", "Successfully Deleted");
                out.println(object);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {

            }
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException e) {

            }
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {

            }

        }
        return;
    }
}