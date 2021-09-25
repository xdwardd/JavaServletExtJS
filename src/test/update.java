package test;

import org.json.JSONObject;
import test.utils.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/update")
public class update extends HttpServlet{

    public static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String sid = request.getParameter ("admin_id");
        int id = Integer.parseInt (sid);
        String admin_user = request.getParameter("admin_user");
        String admin_password = request.getParameter("admin_password");


        users e = new users();
        e.setAdmin_id(id);
        e.setAdmin_user(admin_user);
        String pass = BCrypt.hashpw(admin_password, BCrypt.gensalt());
        e.setAdmin_password(pass);

        int status = update(e);

        if (status > 0 )
        {
            JSONObject object = new JSONObject();
            object.put("success", true);
            object.put("reason", "Successfully Updated");
            out.println(object);
        }else {
            JSONObject object = new JSONObject();
            object.put("success", false);
            object.put("reason", "Update Failed");
            out.println(object);
        }

    }

    public static int update (users e)
    {


        int status =0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crud", "root", "");
            PreparedStatement ps = conn.prepareStatement("UPDATE admins SET admin_user=?, admin_password=? WHERE admin_id=?");
            ps.setString(1, e.getAdmin_user());
            ps.setString(2, e.getAdmin_password());
            ps.setInt(3, e.getAdmin_id());

            status = ps.executeUpdate();
            conn.close();

        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return status;
    }



}
