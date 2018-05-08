package Servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Home extends HttpServlet {

    @EJB
    attivita.AttivitaFacadeLocal attivita;
    @EJB
    messaggi.MessaggiFacadeLocal messaggiFL;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        utente.Utente user = (utente.Utente) session.getAttribute("utente");

        if (user == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        List<messaggi.Messaggi> messaggiNonLetti = messaggiFL.getMessaggiNonLetti(user);
        List<messaggi.Messaggi> messaggiRicevuti = messaggiFL.getMessaggiRicevuti(user);
        ArrayList<attivita.Attivita> listaAttivita = new ArrayList(attivita.recenti(user));
        Collections.sort(listaAttivita);
        session.setAttribute("attivita", listaAttivita);
        session.setAttribute("messaggiNonLetti", messaggiNonLetti);
        session.setAttribute("messaggiRicevuti", messaggiRicevuti);
        RequestDispatcher dispatcher;
        dispatcher = request.getRequestDispatcher("home.jsp");
        dispatcher.forward(request, response);
        return;

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String page = request.getParameter("page");

        if (page != null && page.equals("logout")) {

            HttpSession session = request.getSession();

            if (session != null) {
                session.setAttribute("utente", null);
            }
            response.sendRedirect("index.jsp");
            return;
        }
        processRequest(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
 
}
