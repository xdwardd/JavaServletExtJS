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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet("/login")
public class Login extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println(request.getRemoteAddr());

        response.setContentType("application/json");

        String admin_user = request.getParameter("admin_user");
        PrintWriter out = response.getWriter();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crud", "root", "");
          //  pst = conn.prepareStatement("Select admin_password from admins where admin_user=?");
            pst = conn.prepareStatement("SELECT admin_password, admin_id from admins where admin_user=? ");
            //pst.setString(1, admin_user);
            pst.setString(1, admin_user);
            rs = pst.executeQuery();

            try {

                if (rs.next()){
                    //Matching the bcrypt password with the plaintext password
                   //  String admin_user = rs.getString("admin_user");
                     String bcryptHash = rs.getString("admin_password");
                     String password = request.getParameter("admin_password");
                     int admin_id = rs.getInt("admin_id");


                   if (isPasswordValid(password)) {

                        BCrypt.hashpw(password, BCrypt.gensalt());

                            if (BCrypt.checkpw(password, bcryptHash)) {
                                System.out.println("Password valid");
                                JSONObject object = new JSONObject();
                                object.put("success", true);
                                object.put("reason", "Successfully Logged in");
                                object.put("admin_id", admin_id);
                                out.println(object);


                            } else {
                                System.out.println("Password invalid");
                                JSONObject object = new JSONObject();
                                object.put("success", false);
                                object.put("reason", "Login Failed");
                                out.println(object);
                            }
                    } else {
                        JSONObject object = new JSONObject();
                        object.put("success", false);
                        object.put("reason", "Password is not valid");
                        out.println(object);
                    }
                } else {

                    JSONObject object = new JSONObject();      // failed if both of them are invalid
                    object.put("success", false);
                    object.put("reason", "Login failed.");
                    out.println(object);
                }

            } catch (Exception e) {
                e.printStackTrace();
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

//Creating Methods
/*
   Check if the password contains special character
 */
public boolean isPasswordValid(String password){
    Pattern pattern = Pattern.compile("((?=.*[a-z])(?=.*d)(?=.*[@#$%])(?=.*[A-Z]).{8,16})");
    Matcher matcher = pattern.matcher(password);

    if(matcher.matches()){
        System.out.println("Password Valid");
        return true;
    }else {
        System.out.println("Password Invalid");
        return false;
    }
}


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", false);
        jsonObject.put("reason", "Request not allowed");
        response.getWriter().println(jsonObject);
    }
}