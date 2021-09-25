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
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.*;

// use this annotation if there is a file requested or else you will get java.lang.NumberFormatException: null error response
                //                  3 mb                    5 mb                            10 mb
@MultipartConfig(fileSizeThreshold=1024*1024*3, maxFileSize = 1024*1024*5, maxRequestSize = 1024*1024*10)

@WebServlet("/updateServlet")
public class updateServlet extends HttpServlet{

    public static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();


        String sid = request.getParameter ("user_id");
        int id = Integer.parseInt (sid);
        String fname = request.getParameter("fname");
        String lname = request.getParameter("lname");
        String mi = request.getParameter("mi");
        String birthdate = request.getParameter("birthdate");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        String screated_by = request.getParameter("created_by");
        int created_by = Integer.parseInt(screated_by);

        InputStream inputStream = null;
        Part filePart = request.getPart("photo");

        if (filePart != null){
            System.out.println(filePart.getName());
            System.out.println(filePart.getSize());
            System.out.println(filePart.getContentType());

            inputStream = filePart.getInputStream();

        }
        if(inputStream == null){
            System.out.println("InputStream is null");
        }else {
            System.out.println("InputStream is not null");
        }

        userSample e = new userSample();
        e.setUser_id(id);
        e.setFname(fname);
        e.setLname(lname);
        e.setMi(mi);
        e.setBirthdate(birthdate);
        e.setPhoto(inputStream);
        e.setUsername(username);

        String pass = BCrypt.hashpw(password, BCrypt.gensalt());
        e.setPassword(pass);

        e.setCreated_by(created_by);

        int status = update(e);

        if (status != 0 )
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

        out.close();
    }

    public static int update (userSample e)
    {


        int status = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crud", "root", "");
            PreparedStatement pst = conn.prepareStatement("UPDATE sampleuser SET fname=?,lname=?,mi=?,birthdate=?,photo=?,username=?,password=?,created_by=? WHERE user_id=?");

            pst.setString(1, e.getFname());
            pst.setString(2, e.getLname());
            pst.setString(3, e.getMi());
            pst.setString(4, e.getBirthdate());
            pst.setBlob(5, e.getPhoto());
            pst.setString(6, e.getUsername());
            pst.setString(7, e.getPassword());
            pst.setInt(8, 0);
            pst.setInt(9, e.getUser_id());


            status = pst.executeUpdate();
            conn.close();

        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return status;
    }



}
