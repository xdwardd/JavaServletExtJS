package forUsers;


import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/userServlet")
public class userServlet extends HttpServlet{

    private static final long serialVersionUID = 1L;

    //get users data

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        int page = Integer.parseInt(request.getParameter("page"));
        int start = Integer.parseInt(request.getParameter("start"));
        int limit = Integer.parseInt(request.getParameter("limit"));

        try{
             Class.forName("com.mysql.jdbc.Driver");
             conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crud?useSSL=false", "root", "");

             System.out.println(start);
             System.out.println(limit);



             //count row in database
             int rowCount = 0;
             pst = conn.prepareStatement("SELECT COUNT(*) FROM users");
             rs = pst.executeQuery();
             while (rs.next()){
                rowCount = rs.getInt(1);
            }


                                                                // LIMIT ?,?
             pst = conn.prepareStatement("SELECT * FROM users LIMIT ?,?");
             pst.setInt(1, start);
             pst.setInt(2, limit);
             rs = pst.executeQuery();


            try{


                 ArrayList<JSONObject> arrayList = new ArrayList<>();
                 JSONObject object = new JSONObject();

                 while (rs.next()){

                     object.put("user_id", rs.getInt(1));
                     object.put("firstname", rs.getString(2));
                     object.put("lastname", rs.getString(3));
                     object.put("email", rs.getString(4));
                     object.put("phone", rs.getString(5));

                     object.put("start", start);
                     object.put("limit", limit);
                     object.put("page",page);

                     arrayList.add(object);
                     object = new JSONObject();
                 }

                 JSONObject responseJson = new JSONObject();
                 System.out.println(rowCount);
                 responseJson.put("total",rowCount);//  9 items
                 responseJson.put("data", arrayList);
                 out.println(responseJson);





            /*
             if (page <= 1) {
                responseJson.put("data", arrayList.subList(0, 3));  //  get list from index 0 to 2
                }
              if (page == 3) {
                  responseJson.put("data", arrayList.subList(3, 9));  //  get list from index 6 to 8
              }

              if (page == 2) {
                  responseJson.put("data", arrayList.subList(3, 6));  //  get list from index 3 to 5

              }if (page == 4){
                  responseJson.put("data", arrayList.subList(9,12));
             }*/



             }catch (Exception e){
                 out.print("connection error!");
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
                if(pst != null){
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



    /*
    // Pagination Sample Code

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //System.out.println(request.getRemoteAddr());

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        ArrayList<JSONObject> arrayList = new ArrayList<>();
        JSONObject object;

        //  9 items
        for (int i = 1; i < 10; i++) {
            object = new JSONObject();

            object.put("user_id", i);
            object.put("firstname", "myfirstname"+i);
            object.put("lastname", "mylastname"+i);
            object.put("email", "myemail"+i);
            object.put("phone", "myphone"+i);

            arrayList.add(object);
        }

        int page = Integer.parseInt(request.getParameter("page"));
        int start = Integer.parseInt(request.getParameter("start"));
        int limit = Integer.parseInt(request.getParameter("limit"));

        JSONObject responseJson = new JSONObject();
        responseJson.put("total", arrayList.size());    //  9 items

        if (page <= 1) {
            responseJson.put("data", arrayList.subList(0, 3));  //  get list from index 0 to 2
        } else if (page >= 3) {
            responseJson.put("data", arrayList.subList(6, 9));  //  get list from index 6 to 8
        } else {
            responseJson.put("data", arrayList.subList(3, 6));  //  get list from index 3 to 5
        }

        out.println(responseJson);
    }
*/

}
