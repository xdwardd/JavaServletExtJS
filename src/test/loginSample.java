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

@WebServlet("/loginSample")
public class loginSample extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException{
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String user, password, role;
        user = request.getParameter("admin_user");
        password = request.getParameter("admin_password");
        role = request.getParameter("role");

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crud","root", "");
            conn.prepareStatement("SELECT from admins WHERE admin_user=? AND admin_password=? AND role=?");


            pst.setString(1, user);
            pst.setString(2, password);
            pst.setString(3, role);

            rs = pst.executeQuery();
            HttpSession session = request.getSession();

            try{
                if(rs.next()){
                    String bcryptHash = rs.getString("admin_password");
                    String pass = request.getParameter("admin_password");

                    if(isPasswordValid(pass)){
                        BCrypt.checkpw(pass, BCrypt.gensalt());

                        if(BCrypt.checkpw(pass,bcryptHash)){
                            session.setAttribute("isAdmin",role);
                            JSONObject object = new JSONObject();
                            object.put("success", true);
                            object.put("reason", "Successfully Login");
                            out.println(object);
                        }else{
                            JSONObject object = new JSONObject();
                            object.put("success", true);
                            object.put("reason", "Login Failed");
                        }

                    }else {
                        JSONObject object = new JSONObject();
                        object.put("success", false);
                        object.put("reason", "Password is not valid");
                        out.println(object);
                    }

                }else {
                    JSONObject object = new JSONObject();      // failed if both of them are invalid
                    object.put("success", false);
                    object.put("reason", "Login failed.");
                    out.println(object);
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }finally {
            try {
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
                if (rs != null){
                    rs.close();
                }
            }catch (SQLException e){}

        }return;
    }


    public boolean isPasswordValid(String password){
        Pattern pattern = Pattern.compile("((?=.*[a-z])(?=.*d)(?=.*[@#$%])(?=.*[A-Z]).{8,16})");
        Matcher matcher = pattern.matcher(password);
        if(matcher.matches()){
            System.out.println("Password Valid");
            return true;
        }else{
            System.out.println("Password Invalid");
            return false;
        }
    }


}