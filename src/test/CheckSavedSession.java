package test;




import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/checksavedsession")
public class CheckSavedSession extends HttpServlet{
    private static final long serialVersionUID = 1L;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
       if (session != null){
            session.getAttribute("isAdmin");
            JSONObject object = new JSONObject();
            object.put("success", true);
            object.put("reason", "There are session saved");
            out.println(object);
        }

        else {

            JSONObject object = new JSONObject();
            object.put("success", false);
            object.put("reason", "No saved sessions");
            out.println(object);
        }
    }




//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
}

