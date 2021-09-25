package test;

import com.mysql.jdbc.ConnectionLifecycleInterceptor;
import com.sun.org.apache.bcel.internal.generic.Select;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Scanner;

@WebServlet("/generateKey")
public class GenerateSecreteKey extends HttpServlet {
    public static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

       String sid = request.getParameter("admin_id");
       int id = Integer.parseInt(sid);
       String role = request.getParameter("role");


        //String admin_user = request.getParameter("admin_user");


        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crud","root", "");
            pst = conn.prepareStatement("Select secret_key, role from admins WHERE admin_id=?");
            pst.setInt(1,id);
            rs = pst.executeQuery();
            HttpSession session = request.getSession();


            try {
                    if (rs.next()){

                        GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();

                        int isAdmin = rs.getInt("role");
                        String secret_key = rs.getString("secret_key");
                        String secKey = request.getParameter("securityKey");
                        int securityKey = Integer.parseInt(secKey);

                        if (isAdmin == 1){

                            if(googleAuthenticator.authorize(secret_key,securityKey)){
                                System.out.println("Security is valid");
                                session.setAttribute("isAdmin", role);
                                JSONObject obj = new JSONObject();
                                obj.put("success", true);
                                obj.put("reason", "Security code Valid");
                                obj.put("role", isAdmin);
                                out.println(obj);

                            }else {
                                System.out.println("Security is not valid");

                                JSONObject obj = new JSONObject();
                                obj.put("success", false);
                                obj.put("reason", "Security code is not valid");
                                obj.put("role", isAdmin);
                                out.println(obj);

                            }

                        }else if (isAdmin == 0){
                            JSONObject obj = new JSONObject();
                            obj.put("success", true);
                            obj.put("reason", "Welcome to User");
                            obj.put("role", isAdmin);
                            out.println(obj);

                        }else {
                            JSONObject obj = new JSONObject();
                            obj.put("success", false);
                            obj.put("reason", "Login Failed");
                            obj.put("role", isAdmin);
                            out.println(obj);
                        }




                    }


                }catch (Exception e){
                    e.printStackTrace();
                }

        } catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }finally {
            try{
                if (conn != null){
                    conn.close();
                }
            }catch (SQLException e){}

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