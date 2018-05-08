
package Servlet;

import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class Rubrica extends HttpServlet {

    @EJB
    private rubrica.RubricaFacadeLocal rubricaFL;

    private String messaggio;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        utente.Utente user = (utente.Utente) session.getAttribute("utente");

        if (user == null) {
            response.sendRedirect("index.jsp");
            return;
        }
        session.setAttribute("messaggio", messaggio);
        RequestDispatcher dispatcher;
        dispatcher = request.getRequestDispatcher("rubrica.jsp");
        dispatcher.forward(request, response);
        return;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("GET");
        HttpSession session = request.getSession();
        utente.Utente user = (utente.Utente) session.getAttribute("utente");
        List<rubrica.Rubrica> lista = rubricaFL.findByUtente(user);
        session.setAttribute("rubrica", lista);
        processRequest(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
  System.out.println("Rubrica  POST");
        HttpSession session = request.getSession();
        utente.Utente user = (utente.Utente) session.getAttribute("utente");
        //Elimina
        String ident = (String) request.getParameter("eliminaCont");
        System.out.println("ident " +ident+" user"+user.getId());
        if (ident != null) {
            rubrica.Rubrica rub = rubricaFL.find(Long.parseLong(ident));
            if (user.getId().equals(rub.getOwner().getId())) {
                rubricaFL.remove(rub);
                messaggio = "Contatto eliminato";
                List<rubrica.Rubrica> lista = rubricaFL.findByUtente(user);
                session.setAttribute("rubrica", lista);
                processRequest(request, response);
                return;
            } else {
                messaggio = "Contatto non valido";
                processRequest(request, response);
                return;
            }

        }

        messaggio = "Richiesta non valida";

        processRequest(request, response);
    }

}
