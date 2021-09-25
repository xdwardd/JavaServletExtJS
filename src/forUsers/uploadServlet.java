package forUsers;



import org.json.JSONObject;
import test.utils.BCrypt;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import java.io.*;

import java.sql.*;

import static sun.rmi.transport.TransportConstants.Magic;

@WebServlet("/uploadServlet")
@MultipartConfig(fileSizeThreshold=1024*1024*3, maxFileSize = 1024*1024*5, maxRequestSize = 1024*1024*10)
public class uploadServlet extends HttpServlet{

    public static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        String fname = request.getParameter("fname");
        String lname = request.getParameter("lname");
        String mi = request.getParameter("mi");
        String birthdate = request.getParameter("birthdate");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        InputStream inputStream = null;//input stream of upload file
        Part filePart = request.getPart("photo");
        if (filePart != null){
            System.out.println(filePart.getName());
            System.out.println(filePart.getSize());
            System.out.println(filePart.getContentType());
            System.out.println(filePart.getSubmittedFileName());


            inputStream = filePart.getInputStream();


         }
            if(inputStream == null){
                System.out.println("InputStream is null");
            }else {
                System.out.println("InputStream is not null");
            }

        try{

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crud", "root", "");
            pst = conn.prepareStatement("INSERT INTO sampleuser (fname,lname,mi,birthdate,username,password,photo,created_by) VALUES (?,?,?,?,?,?,?,?)");

            pst.setString(1, fname);
            pst.setString(2, lname);
            pst.setString(3, mi);
            pst.setString(4, birthdate);
            pst.setString(5, username);
            pst.setString(6, password);

            String pass = BCrypt.hashpw(password, BCrypt.gensalt());
            pst.setString(6,pass);
            pst.setBlob(7, inputStream);
            pst.setInt(8, 0);


            if (filePart.getContentType().equals("image/jpeg") && filePart.getSize() <= 512000)
            {
                pst.executeUpdate();

                JSONObject object = new JSONObject();
                object.put("success", true);
                object.put("reason", "Successfully Saved");
                out.println(object);
            } else {
                JSONObject object = new JSONObject();
                object.put("success", false);
                object.put("reason", "File Must be image/jpeg");
                out.println(object);
            }


        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }finally {
            try {
                if (conn != null){
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
        }
        return;

    }


}






