package forUsers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/getimage")
public class getImage extends HttpServlet{

    public static final long serialVersionUID = 1L;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException{
    //    PrintWriter out = response.getWriter();
        int id = Integer.parseInt(request.getParameter("user_id"));
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;


            try{
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection("jdbc:mysql://localhost/crud", "root", "");
                pst = conn.prepareStatement("Select photo FROM sampleuser WHERE user_id=?");

                pst.setInt(1, id);
                rs = pst.executeQuery();
                if(rs.next()){
                    Blob blob = rs.getBlob("photo");
                    byte byteArray[] = blob.getBytes(1, (int)blob.length());
                    response.setContentType("image/jpeg");
                    OutputStream os = response.getOutputStream();
                    os.write(byteArray);
                    os.flush();
                    os.close();
                }

        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();

        }finally {
            try {
                if (conn != null){
                    conn.close();
                }
            }catch(SQLException e){}

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
