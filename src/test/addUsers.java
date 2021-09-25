package test;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import org.json.JSONObject;
import test.utils.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;


@WebServlet("/addusers")
public class addUsers extends HttpServlet{

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException{
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String admin_user = request.getParameter("admin_user");
        String admin_password = request.getParameter("admin_password");

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crud", "root", "");
            pst = conn.prepareStatement("INSERT INTO admins (admin_user,admin_password,role,secret_key) VALUES (?,?,?,?)");
            pst.setString(1, admin_user);


            // hash admin_password
            String pass = BCrypt.hashpw(admin_password, BCrypt.gensalt());
            pst.setString(2,pass);

            pst.setInt(3,0);

            //Generate Security key
            GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
            final GoogleAuthenticatorKey googleAuthenticatorKey = googleAuthenticator.createCredentials();
            String secret_key = googleAuthenticatorKey.getKey();
            pst.setString(4,secret_key);

            pst.executeUpdate();

                        JSONObject object = new JSONObject();
                        object.put("success", true);
                        object.put("reason", "Successfully Added");
                        out.println(object);




        }catch (ClassNotFoundException | SQLException e){
                e.printStackTrace();
        }finally {
            try {
                if(conn != null){
                    conn.close();
                }
            }catch (SQLException e){

            }try {
                if(pst != null){
                    pst.close();
                }
            }catch (SQLException e){

            }try{
                if(rs != null) {
                    rs.close();
                }
            }catch (SQLException e){

            }

        }
        return;

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    }
}
