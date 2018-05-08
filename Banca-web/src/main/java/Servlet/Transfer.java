package Servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import utility.Utility;


public class Transfer extends HttpServlet {

  
    @EJB
    private utente.UtenteFacadeLocal utente;
    @EJB
    private rubrica.RubricaFacadeLocal rubricaFL;
    @EJB
    private attivita.AttivitaFacadeLocal attivitaFL;

    private String messaggio;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher dispatcher;
        System.out.println("Messaggio " + messaggio);

        if (messaggio != null && messaggio.length() > 0) {
            request.setAttribute("messaggio", messaggio);
            dispatcher = request.getRequestDispatcher("transfer.jsp");
            dispatcher.forward(request, response);
            return;
        } else {
            HttpSession session = request.getSession();
            messaggio = "Operazione avvenuta con successo";
            //Aggiorno
            utente.Utente u = (utente.Utente) session.getAttribute("utente");
            u = utente.find(u.getId());
            session.setAttribute("utente", u);

            session.setAttribute("messaggio", messaggio);
            response.sendRedirect("home");
        }
    }

 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("GET");
        HttpSession session = request.getSession();
        if (session.getAttribute("utente") == null) {
            messaggio = "Sessione non valida";
            processRequest(request, response);
            return;
        }

        utente.Utente u = (utente.Utente) session.getAttribute("utente");
        List<utente.Utente> rubrica = getRubrica(u);
        session.setAttribute("rubrica", rubrica);

        RequestDispatcher dispatcher;
        dispatcher = request.getRequestDispatcher("transfer.jsp");
        dispatcher.forward(request, response);

    }


    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("utente") == null) {
            messaggio = "Sessione non valida";
            processRequest(request, response);
            return;
        }

        utente.Utente owner = (utente.Utente) session.getAttribute("utente");

        String importoS = request.getParameter("importo");

        List<utente.Utente> rubrica = getRubrica(owner);
        session.setAttribute("rubrica", rubrica);
        String choose = request.getParameter("choose");
        if (importoS == null) {
            messaggio = "Importo non valido";
            processRequest(request, response);
            return;
        }
        if (!Utility.isDouble(importoS)) {
            messaggio = "Importo non valido";
            processRequest(request, response);
            return;
        }

        Double importo = Double.parseDouble(importoS);

        if (importo < 0) {
            messaggio = "Importo non può essere negativo";
            processRequest(request, response);
            return;
        }

        if (importo == 0) {
            messaggio = "Inserisci un importo valido";
            processRequest(request, response);
            return;
        }

        String identDest = null;
        utente.Utente destinatario = null;
        if (choose.equals("Rubrica")) {
            identDest = request.getParameter("RubricaSelect");
            if (!Utility.isDouble(identDest)) {
                messaggio = "Contatto non valido";
                processRequest(request, response);
                return;
            }
            destinatario = utente.find(Long.parseLong(identDest));
        } else if (choose.equals("Email")) {
            identDest = request.getParameter("emailInput").toUpperCase();
            if (!Utility.isValidEmail(identDest)) {
                messaggio = "Email non valida";
                processRequest(request, response);
                return;
            }
            List<utente.Utente> lista = utente.findByEmail(identDest);
            if (lista == null || lista.size() == 0 || lista.get(0) == null) {
                messaggio = "Destinatario non risulta registrato";
                processRequest(request, response);
                return;
            } else {
                destinatario = lista.get(0);
            }
        }

        if (destinatario == null) {
            messaggio = "Destinatario non risulta registrato";
            processRequest(request, response);
            return;
        }

        if (destinatario.equals(owner)) {
            messaggio = "Non puoi autopagarti!";
            processRequest(request, response);
            return;
        }

        if (request.getParameter("checkbox") != null && request.getParameter("checkbox").equals("on")) {
            List<rubrica.Rubrica> rubricaList = rubricaFL.findByUtente(owner);
            boolean isExist = false;
            for (rubrica.Rubrica r : rubricaList) {
                if (r.getContatto().equals(destinatario)) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                rubrica.Rubrica rub = new rubrica.Rubrica();
                rub.setOwner(owner);
                rub.setContatto(destinatario);
                rubricaFL.create(rub);
            }

        }

        String causale = request.getParameter("causale");
        System.out.println("PROVA CAUSALE " + causale);

        if (causale == null || causale.length() > 25 || causale.length() < 5) {
            messaggio = "La causale deve essere compresa tra i 5 e i 25 caratteri";
            processRequest(request, response);
            return;
        }

        messaggio = "";
        pagamento(owner, destinatario, importo, causale);
        processRequest(request, response);
        return;

    }

    public void pagamento(utente.Utente mittente, utente.Utente destinatario, Double importo, String causale) {

        //Prendo il saldo aggiornato
        mittente = utente.find(mittente.getId());
        System.out.println(" SALDO " + mittente.getSaldo());
        if (mittente.getSaldo() - importo < 0) {
            messaggio = "Saldo non sufficiente";
            return;
        }

        mittente.setSaldo(mittente.getSaldo() - importo);
        destinatario.setSaldo(destinatario.getSaldo() + importo);
        utente.edit(mittente);
        utente.edit(destinatario);
        createAttivita(causale, mittente, destinatario, importo);
        return;

    }

    public List<utente.Utente> getRubrica(utente.Utente owner) {
        if (owner.getTipo().equals("ADMIN")) {
            List<rubrica.Rubrica> listaRub = rubricaFL.findAll();
            for (int i = 0; i < listaRub.size(); i++) {
                if (listaRub.get(i).getContatto().getId().equals(owner.getId())) {
                    listaRub.remove(i);
                    break;
                }
            }
            List<utente.Utente> listaUtenti = new ArrayList<utente.Utente>();
            for (rubrica.Rubrica r : listaRub) {
                listaUtenti.add(r.getContatto());
            }
            return listaUtenti;
        }
        List<rubrica.Rubrica> listaRub = rubricaFL.findByUtente(owner);
        List<utente.Utente> listaUtenti = new ArrayList<utente.Utente>();
        for (rubrica.Rubrica r : listaRub) {
            listaUtenti.add(r.getContatto());
        }
        return listaUtenti;
    }

    public void createAttivita(String causale, utente.Utente mittente, utente.Utente destinatario, double importo) {
        attivita.Attivita att = new attivita.Attivita();
        att.setCausale(causale);
        att.setImporto(importo);
        att.setMittente(mittente);
        att.setData(Utility.getActualDate());
        if (destinatario != null) {
            att.setDestinatario(destinatario);
        }
        attivitaFL.create(att);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
