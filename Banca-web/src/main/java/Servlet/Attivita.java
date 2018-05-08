
package Servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Boris
 * Servlet che gestisce le Attività nella piattaforma, cioè fornisce dei metodi per tenere la tracciabilità.
 */
public class Attivita extends HttpServlet {

    @EJB
    private attivita.AttivitaFacadeLocal attivitaFL;

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        utente.Utente user = (utente.Utente) session.getAttribute("utente");

        if (user == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        
        RequestDispatcher dispatcher;
        dispatcher = request.getRequestDispatcher("attivita.jsp");
        dispatcher.forward(request, response);
        return;
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
         HttpSession session = request.getSession();
        utente.Utente user = (utente.Utente) session.getAttribute("utente");
        
        ArrayList<attivita.Attivita> listaAttivitaRicevute = new ArrayList(attivitaFL.ricevute(user));
        ArrayList<attivita.Attivita> listaAttivitaEffettuate = new ArrayList(attivitaFL.effettuate(user));
        ArrayList<attivita.Attivita> listaAttivita = new ArrayList<>();
        
        listaAttivita.addAll(listaAttivitaRicevute);
        listaAttivita.addAll(listaAttivitaEffettuate);
        
        Collections.sort(listaAttivita);
     
        session.setAttribute("attivita", listaAttivita);
        
        processRequest(request, response);
    }

   
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

}
