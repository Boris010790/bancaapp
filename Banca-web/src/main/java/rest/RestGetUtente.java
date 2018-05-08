package rest;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class RestGetUtente extends HttpServlet {

    @EJB
    private utente.UtenteFacadeLocal utenteFL;
    private String json;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {
            out.println(json);
           
        }
    }

   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        utente.Utente u = (utente.Utente) session.getAttribute("utente");
          if(u==null || u.getId() == null){
            json = "{\"error\":\"Sessione non valida\"}";
             json = "{\"error\":\"Sessione non valida\"}";
             processRequest(request, response);
            return;
        }
            
        u = utenteFL.find(u.getId());
        session.setAttribute("utente",u);
      
           
       json = new Gson().toJson(u);
        
        processRequest(request, response);
    }

   
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         json = "{\"error\":\"Metodo POST non permesso\"}";
        processRequest(request, response);
    }

}
