package rest;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class RestGetRubrica extends HttpServlet {

    private String json;

    @EJB
    private rubrica.RubricaFacadeLocal rubricaFL;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println(json);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        utente.Utente u = (utente.Utente) session.getAttribute("utente");
        if (u == null) {
            json = "{\"error\":\"Sessione non valida\"}";
            processRequest(request, response);
            return;
        }
        
        List<rubrica.Rubrica> rubrica = rubricaFL.findByUtente(u);
        if(rubrica.size() == 0){
             json = "{\"rubrica\":\"\"}";
            processRequest(request, response);
            return;
        }
        ArrayList<utente.Utente> listaRub = new ArrayList();
        for(rubrica.Rubrica r : rubrica) listaRub.add(r.getContatto());
        json = new Gson().toJson(listaRub);
        json="{\"success\": "+json+"}";
        processRequest(request, response);
      
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
          json = "{\"error\":\"Metodo POST non permesso\"}";
        processRequest(request, response);
    }


}
