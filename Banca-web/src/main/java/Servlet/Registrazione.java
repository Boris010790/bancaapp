
package Servlet;

import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utility.Utility;


public class Registrazione extends HttpServlet {

    @EJB
    private utente.UtenteFacadeLocal utente;
    @EJB
    private login.LoginFacadeLocal login;

    private String messaggio;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        
        RequestDispatcher dispatcher;
        request.setAttribute("messaggio", messaggio);
        System.out.println(messaggio+"Debug");
        if (messaggio.length() == 0) {
            //dispatcher = request.getRequestDispatcher("/home");
            dispatcher = getServletContext().getRequestDispatcher("/home");
        } else {
            dispatcher = request.getRequestDispatcher("/registrazione.jsp");
        }

        dispatcher.forward(request, response);

    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("messaggio", messaggio);
        RequestDispatcher dispatcher = request.getRequestDispatcher("registrazione.jsp");
        dispatcher.forward(request, response);
        //processRequest(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String tipoLogin = request.getParameter("tipoLogin");
        if (tipoLogin.equals("standard")) {
            String nome = request.getParameter("nome");
            String cognome = request.getParameter("cognome");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String conferma = request.getParameter("conferma");
            String tipo = "CLIENTE";

            //Controllo se ci sono campi vuoti
            if (nome.length() == 0 || cognome.length() == 0 || email.length() == 0 || password.length() == 0 || conferma.length() == 0) {
                messaggio = "Completa tutti i campi";
                processRequest(request, response);
                return;
            }

            //Controllo la lunghezza della password
            if (password.length() < 6) {
                messaggio = "La password deve essere almeno di 6 caratteri";
                processRequest(request, response);
                return;
            }

            //Controllo se la password corrisponde al conferma
            if (!Utility.codificaInMD5(password).equals(Utility.codificaInMD5(conferma))) {
                messaggio = "La password non corrisponde";
                processRequest(request, response);
                return;
            }

            //Controllo email valida
            if (!Utility.isValidEmail(email)) {
                messaggio = "Email non valida";
                processRequest(request, response);
                return;
            }

            //Devo controllare che non esista una email con una password
            email = email.toUpperCase();
            List<login.Login> lista = login.findByEmail(email);
             utente.Utente u = new utente.Utente();
             if(lista.size() > 0)u = lista.get(0).getUtente();
            //Controllo che abbia la password 
         
            for(int i=0; i<lista.size();){
                if(lista.get(i).getPassword()== null ){
                  lista.remove(i);
                }
                else i++;
            }
            
            if (lista.size() > 0) {
                messaggio = "Email risulta già registrata";
                processRequest(request, response);
                return;
            }
            
            //Creo record utente
           
            if (u.getId() == null) {
                nome = nome.toUpperCase();
                cognome = cognome.toUpperCase();

                tipo = tipo.toUpperCase();
                u.setNome(nome.toUpperCase());
                u.setCognome(cognome.toUpperCase());
                u.setEmail(email.toUpperCase());
                u.setTipo(tipo.toUpperCase());
                utente.create(u);
            }

            //Creo record Login
            login.Login l = new login.Login();
            l.setEmail(email.toUpperCase());
            l.setPassword(Utility.codificaInMD5(password));

            //Recupero id utente inserito precedentemente
            l.setUtente(u);
            login.create(l);
            messaggio = "";

            processRequest(request, response);
        }
    }


}
