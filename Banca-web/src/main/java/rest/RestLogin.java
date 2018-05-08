package rest;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import utility.Utility;


public class RestLogin extends HttpServlet {

    @EJB
    private login.LoginFacadeLocal loginFL;

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
        json = "{\"error\":\"Metodo Get non permesso\"}";
        processRequest(request, response);
    }

   
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || email.length() == 0 || !Utility.isValidEmail(email) || password == null || password.length() < 6) {
            json = ("{\"error\":\"campiVuoti\"}");
            processRequest(request, response);
            return;
        }
        email = email.toUpperCase();
        ArrayList lista = new ArrayList(loginFL.findByEmail(email));

        if (lista.size() == 0) {
            json = "{\"error\":\"Email o password errate\"}";
            processRequest(request, response);
            return;
        }

        for (int i = 0; i < lista.size();) {
            if (lista.get(i) == null) {
                lista.remove(i);
            } else {
                i++;
            }
        }

        login.Login l = (login.Login) lista.get(0);
        String passMD5 = Utility.codificaInMD5(password);
        String passLogin = l.getPassword();

        if (passLogin != null && passLogin.equals(passMD5)) {
            utente.Utente u = l.getUtente();

            HttpSession session = request.getSession();
            session.setAttribute("utente", u);
            json = "{\"success\":\"" + session.getId() + "\"}";
            processRequest(request, response);
            return;
        }

        json = "{\"error\":\"Nome utente o password Errate\"}";

        processRequest(request, response);

    }
}
