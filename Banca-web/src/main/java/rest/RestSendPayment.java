package rest;

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
import utility.Utility;


public class RestSendPayment extends HttpServlet {

    private String json;
    @EJB
    utente.UtenteFacadeLocal utenteFL;
    @EJB
    attivita.AttivitaFacadeLocal attivitaFL;
    @EJB
    rubrica.RubricaFacadeLocal rubricaFL;

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
        HttpSession session = request.getSession();
        if (session.getAttribute("utente") == null) {
            json = "{\"error\":\"Sessione non valida\"}";
            processRequest(request, response);
            return;
        }
        utente.Utente owner = (utente.Utente) session.getAttribute("utente");

        String importoS = request.getParameter("importo");
        String choose = request.getParameter("choose");
        if (choose == null || (!choose.equals("Rubrica") && !choose.equals("Email"))) {
            json = "{\"error\":\"Compilare il campo choose\"}";
            processRequest(request, response);
            return;
        }
        List<utente.Utente> rubrica = getRubrica(owner);
        session.setAttribute("rubrica", rubrica);
        if (importoS == null) {
            json = "{\"error\":\"Importo non valido\"}";
            processRequest(request, response);
            return;
        }
        if (!Utility.isDouble(importoS)) {
            json = "{\"error\":\"Importo non valido\"}";
            processRequest(request, response);
            return;
        }

        Double importo = Double.parseDouble(importoS);

        if (importo < 0) {
            json = "{\"error\":\"Importo non può essere negativo\"}";

            processRequest(request, response);
            return;
        }

        if (importo == 0) {
            json = "{\"error\":\"Iserisci un importo valido\"}";

            processRequest(request, response);
            return;
        }

        String identDest = null;
        utente.Utente destinatario = null;
        if (choose.equals("Rubrica")) {
            identDest = request.getParameter("RubricaSelect");
            if (!Utility.isDouble(identDest)) {
                json = "{\"error\":\"Contatto non valido\"}";

                processRequest(request, response);
                return;
            }
            destinatario = utenteFL.find(Long.parseLong(identDest));
        } else if (choose.equals("Email")) {
            identDest = request.getParameter("emailInput").toUpperCase();
            if (!Utility.isValidEmail(identDest)) {
                json = "{\"error\":\"Email non valida\"}";

                processRequest(request, response);
                return;
            }
            List<utente.Utente> lista = utenteFL.findByEmail(identDest);
            if (lista == null || lista.size() == 0 || lista.get(0) == null) {
                json = "{\"error\":\"Destinatario non registrato\"}";

                processRequest(request, response);
                return;
            } else {
                destinatario = lista.get(0);
            }
        }

        if (destinatario == null) {
            json = "{\"error\":\"Destinatario non registrato\"}";
            processRequest(request, response);
            return;
        }

        if (destinatario.equals(owner)) {
            json = "{\"error\":\"Non puoi autopagarti\"}";
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
            json = "{\"error\":\"La causale deve essere compresa tra 5 e 25 caratteri\"}";
            processRequest(request, response);
            return;
        }

        json = "{\"success\":\"OK\"}";

        pagamento(owner, destinatario, importo, causale);
        processRequest(request, response);
        return;

    }

    public void pagamento(utente.Utente mittente, utente.Utente destinatario, Double importo, String causale) {

        //Prendo il saldo aggiornato
        mittente = utenteFL.find(mittente.getId());
        System.out.println(" SALDO " + mittente.getSaldo());
        if (mittente.getSaldo() - importo < 0) {
            json = "{\"error\":\"saldo non sufficiente\"}";
            return;
        }

        mittente.setSaldo(mittente.getSaldo() - importo);
        destinatario.setSaldo(destinatario.getSaldo() + importo);
        utenteFL.edit(mittente);
        utenteFL.edit(destinatario);
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
            List<utente.Utente> listaUtenti = new ArrayList<>();
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


}
