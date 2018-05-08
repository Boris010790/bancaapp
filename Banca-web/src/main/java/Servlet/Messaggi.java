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
import utility.Utility;


public class Messaggi extends HttpServlet {

    @EJB
    private messaggi.MessaggiFacadeLocal messaggiFL;
    @EJB
    private rubrica.RubricaFacadeLocal rubricaFL;
    @EJB
    private utente.UtenteFacadeLocal utenteFL;

    private String messaggio;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("Process Request");
        HttpSession session = request.getSession();
        utente.Utente user = (utente.Utente) session.getAttribute("utente");

        if (user == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        ArrayList<messaggi.Messaggi> listaMessaggiRicevuti = new ArrayList(messaggiFL.getMessaggiRicevuti(user));
        ArrayList<messaggi.Messaggi> listaMessaggiInviati = new ArrayList(messaggiFL.getMessaggiInviati(user));
        //elimino il messaggio

        List<messaggi.Messaggi> messaggiNonLetti = messaggiFL.getMessaggiNonLetti(user);

        Collections.sort(listaMessaggiRicevuti);
        Collections.sort(listaMessaggiInviati);

        session.setAttribute("inviati", listaMessaggiInviati);
        session.setAttribute("ricevuti", listaMessaggiRicevuti);
        session.setAttribute("messaggiNonLetti", messaggiNonLetti);

        RequestDispatcher dispatcher;
        System.out.println("REQUEST " + messaggio);
        if (messaggio != null && messaggio.length() > 0) {
            request.setAttribute("messaggio", messaggio);
            //messaggio = null;
            dispatcher = request.getRequestDispatcher("messaggi.jsp");
            dispatcher.forward(request, response);
            return;
        } else {
            dispatcher = request.getRequestDispatcher("messaggi.jsp");
            dispatcher.forward(request, response);
        }
        return;

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        utente.Utente user = (utente.Utente) session.getAttribute("utente");

        List<utente.Utente> rubrica = getRubrica(user);
        session.setAttribute("rubrica", rubrica);
//
        String messaggiUrl = request.getParameter("messaggi");
        if (messaggiUrl != null && messaggiUrl.equals("ricevuti")) {
            List<messaggi.Messaggi> messaggiNonLetti = messaggiFL.getMessaggiNonLetti(user);
            for (messaggi.Messaggi mess : messaggiNonLetti) {
                mess.setLetto(true);
                messaggiFL.edit(mess);
            }
        }
        messaggio = null;
        processRequest(request, response);
        return;

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("POST");
        messaggio = null;
        HttpSession session = request.getSession();
        utente.Utente mittente = (utente.Utente) session.getAttribute("utente");

        ArrayList<messaggi.Messaggi> listaMessaggiRicevuti = new ArrayList(messaggiFL.getMessaggiRicevuti(mittente));
        ArrayList<messaggi.Messaggi> listaMessaggiInviati = new ArrayList(messaggiFL.getMessaggiInviati(mittente));

        Collections.sort(listaMessaggiRicevuti);
        Collections.sort(listaMessaggiInviati);

        session.setAttribute("inviati", listaMessaggiInviati);
        session.setAttribute("ricevuti", listaMessaggiRicevuti);

           if (mittente == null) {
            response.sendRedirect("index.jsp");
            return;
        }
        
        
        String elimina = request.getParameter("elimina");
        System.out.println("ELIMINA 1");
        if (elimina != null) {
            String md = request.getParameter("md");
            if (md != null && md.equals("mittente")) {
                System.out.println("ELIMINA Mittente 1");
                messaggi.Messaggi mess = messaggiFL.find(Long.parseLong(elimina));
                mess.setMittenteIsDelete(true);
                messaggiFL.edit(mess);
                listaMessaggiInviati = new ArrayList(messaggiFL.getMessaggiInviati(mittente));
                messaggio = "Messaggio Eliminato";
                Collections.sort(listaMessaggiInviati);
                session.setAttribute("inviati", listaMessaggiInviati);
                processRequest(request, response);
                return;
            } else if (md != null && md.equals("destinatario")) {
                 System.out.println("ELIMINA Destinatario 1");
                messaggi.Messaggi mess = messaggiFL.find(Long.parseLong(elimina));
                mess.setDestinatarioIsDelete(true);
                messaggiFL.edit(mess);
                listaMessaggiRicevuti = new ArrayList(messaggiFL.getMessaggiRicevuti(mittente));
                messaggio = "Messaggio Eliminato";
                Collections.sort(listaMessaggiRicevuti);
                session.setAttribute("ricevuti", listaMessaggiRicevuti);
                processRequest(request, response);
                return;
            } else {
                messaggio = "Richiesta non valida";
                processRequest(request, response);
                return;
            }
        }

     

        String choose = request.getParameter("choose");

        System.out.println("CHOOSE " + choose);
        if (choose == null) {
            messaggio = "Richiesta non valida";
            processRequest(request, response);
            return;
        }
        //EMAIL O RUBRICA?
        String identDest = null;

        utente.Utente destinatario = null;
        if (choose.equals("Rubrica")) {
            identDest = request.getParameter("RubricaSelect");
            if (!Utility.isDouble(identDest)) {
                messaggio = "Contatto non valido";
                processRequest(request, response);
                return;
            }

            destinatario = utenteFL.find(Long.parseLong(identDest));
        } else if (choose.equals("Email")) {
            identDest = request.getParameter("emailInput").toUpperCase();
            if (!Utility.isValidEmail(identDest)) {
                messaggio = "Email non valida";
                processRequest(request, response);
                return;
            }

            List<utente.Utente> lista = utenteFL.findByEmail(identDest);
            if (lista == null || lista.size() == 0 || lista.get(0) == null) {
                messaggio = "Destinatario non risulta registrato";
                processRequest(request, response);
                return;
            } else {
                destinatario = lista.get(0);
                if (request.getParameter("checkbox") != null && request.getParameter("checkbox").equals("on")) {
                    List<rubrica.Rubrica> rubricaList = rubricaFL.findByUtente(mittente);
                    boolean isExist = false;
                    for (rubrica.Rubrica r : rubricaList) {
                        if (r.getContatto().equals(destinatario)) {
                            isExist = true;
                            break;
                        }
                    }
                    if (!isExist) {
                        rubrica.Rubrica rub = new rubrica.Rubrica();
                        rub.setOwner(mittente);
                        rub.setContatto(destinatario);
                        rubricaFL.create(rub);
                        List<utente.Utente> rubrica = getRubrica(mittente);
                        session.setAttribute("rubrica", rubrica);
                    }

                }
            }
        }

        if (destinatario == null) {
            messaggio = "Destinatario non risulta registrato";
            processRequest(request, response);
            return;
        }

        if (destinatario.equals(mittente)) {
            messaggio = "Non puoi autoinviarti messaggi!";
            processRequest(request, response);
            return;
        }

        //OGGETTO
        String oggetto = request.getParameter("oggetto");

        if (oggetto == null || oggetto.length() < 5 || oggetto.length() > 25) {
            messaggio = "Oggetto: La lunghezza deve essere compresa tra 5 e 25 caratteri";
            processRequest(request, response);
            return;
        }
        //MESSAGGIO
        String messaggioPost = request.getParameter("messaggio");
        if (messaggioPost == null || messaggioPost.length() < 10 || messaggioPost.length() > 255) {
            messaggioPost = "Messaggio: La lunghezza deve essere compresa tra 10 e 255 caratteri";
            processRequest(request, response);
            return;
        }

        //INVIO IL MESSAGGIO
        messaggi.Messaggi mess = new messaggi.Messaggi();
        mess.setMittente(mittente);
        mess.setDestinatario(destinatario);
        mess.setOggetto(oggetto);
        mess.setData(Utility.getActualDate());
        mess.setTesto(messaggioPost);
        mess.setLetto(false);
        messaggiFL.create(mess);
        messaggio = "Messaggio Inviato";
        System.out.println("CHeckpoint messaggio " + messaggio);
        processRequest(request, response);
        return;

    }

    public List<utente.Utente> getRubrica(utente.Utente owner) {
        List<rubrica.Rubrica> listaRub = rubricaFL.findByUtente(owner);
        List<utente.Utente> listaUtenti = new ArrayList<utente.Utente>();
        for (rubrica.Rubrica r : listaRub) {
            listaUtenti.add(r.getContatto());
        }
        return listaUtenti;
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
}
