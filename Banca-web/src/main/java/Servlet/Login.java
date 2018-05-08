package Servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import static javax.ws.rs.core.HttpHeaders.USER_AGENT;
import utility.Utility;

public class Login extends HttpServlet {

    /**
     * La variabile "code" è un codice di status che in base al valore fornisce
     * un messaggio all'utente code < -1 VEDERE SERVLET REGISTRAZIONE
     * code = -1 LOGIN ERRATO
     * code =  0 REGISTRAZIONE OK
     * code > 0 LOGGATO
     */
    @EJB
    private login.LoginFacadeLocal loginFacade;
    @EJB
    private utente.UtenteFacadeLocal utente;
    private String messaggio;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher dispatcher;
        request.setAttribute("messaggio", messaggio);
        HttpSession session = request.getSession();
        utente.Utente user = (utente.Utente) session.getAttribute("utente");
        if ((messaggio == null || messaggio.length() == 0) && user != null) {
            response.sendRedirect("home");
          
        } else {
            dispatcher = request.getRequestDispatcher("/login");
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher;
        System.out.println("GET");

        dispatcher = request.getRequestDispatcher("/login");
        dispatcher.forward(request, response);
        System.out.println("Esco GEt");

        return;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("POST");

        String tipoLogin = request.getParameter("login");
        System.out.println("TipoLogin " + tipoLogin);
        if (tipoLogin != null && tipoLogin.equals("gmail")) {
            RequestDispatcher dispatcher;
            String token = request.getParameter("tokenGmail");

            String risp = loginGmail(token);
            if (risp == null || risp.equalsIgnoreCase("Error")) {
                //Caso di errore
                response.sendRedirect("/login");
                return; 
            }

            ObjectMapper mapper = new ObjectMapper();
            HashMap<String, String> q = mapper.readValue(risp, HashMap.class);
            String email = q.get("email");
            String name = request.getParameter("firstname");
            String lastname = request.getParameter("lastname");

            List<login.Login> lista = loginFacade.findByEmail(email.toUpperCase());
            utente.Utente u = new utente.Utente();
            if (lista.size() > 0) {
                u = lista.get(0).getUtente();
            }

            for (int i = 0; i < lista.size();) {
                if (lista.get(i).getPassword() == null) {
                    i++;
                } else {
                    lista.remove(i);
                }
            }

            //creo l'utente se non lo trovo
            if (u.getId() == null) {

                name = name.substring(0, name.indexOf(lastname)).toUpperCase();
                u.setNome(name.toUpperCase());
                u.setCognome(lastname.toUpperCase());
                u.setEmail(email.toUpperCase());
                u.setTipo("CLIENTE");
                //Creo utente
                utente.create(u);
            }

            if ((lista == null || lista.size() == 0)) {
                //Creo Login
                login.Login l = new login.Login();
                l.setEmail(email.toUpperCase());
                l.setUtente(u);
                l.setProvider("GMAIL");
                loginFacade.create(l);
            }
            HttpSession session = request.getSession();
            session.setAttribute("utente", u);
            processRequest(request, response);  
            return;

        }

        //Standard Login
        String email = request.getParameter("email").toUpperCase();
        String password = request.getParameter("password");

        if (email == null || email.length() == 0 || !Utility.isValidEmail(email) || password == null || password.length() < 6) {
            messaggio = "Email o password errate";
            processRequest(request, response);
            return;
        }

        List<login.Login> lista = loginFacade.findByEmail(email);
        

        if (lista == null || lista.size() == 0) {
            messaggio = "Email o password errate";
            processRequest(request, response);
            return;
        }
        for (int i = 0; i < lista.size();) {
            if (lista.get(i).getPassword() == null) {
                lista.remove(i);
            } else {
                i++;
            }
        }

        login.Login l = lista.get(0);
        String passMD5 = Utility.codificaInMD5(password);
        String passLogin = l.getPassword();

        if (passLogin.equals(passMD5)) {
            utente.Utente u = l.getUtente();
            if (u != null) {
                HttpSession session = request.getSession();
                System.out.println("SessionID "+session.getId());
                session.setAttribute("utente", u);
            } else {
                messaggio = "Error 500";
            }
        }

        processRequest(request, response);
    }

    public static String loginGmail(String token) {
        System.out.println("GMAIL");

        try {
            URL obj = new URL("https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=" + token);
            System.out.println("TOKEN " + token);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);

            int responseCode = con.getResponseCode();
            if (responseCode != 200) {
                return "Error";
            }

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result

            return response.toString();
        } catch (Exception f) {
            System.out.println("Exc" + f);
            return "Error";
        }

    }

    @Override
    public String getServletInfo() {
        return "Login Servlet";
    }// </editor-fold>

}
