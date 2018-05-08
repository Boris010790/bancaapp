package rest;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class RestLogout extends HttpServlet {

    private String json;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
          out.println(json);
        }
    }

   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         json = "{\"error\":\"Metodo Get non permesso\"}";
        processRequest(request, response);
    }

   
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
       
        HttpSession session = request.getSession();
       
        Cookie []c = request.getCookies();
        String sessionClient = c[0].getValue();
        if(!sessionClient.equals(session.getId())){
             json ="{\"error\":\"sessione non valida\"}";
            processRequest(request, response);
            return;
        }
        session.invalidate();
       json ="{\"success\":\"sessione distrutta\"}";
        
        
        
        processRequest(request, response);
    }

}
