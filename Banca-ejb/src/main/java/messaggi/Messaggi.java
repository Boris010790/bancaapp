package messaggi;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;
import org.springframework.web.util.HtmlUtils;

@NamedQueries({
    @NamedQuery(name = "Messaggi.getMessaggiNonLetti", query = "SELECT m FROM Messaggi m Where m.destinatario = :utente and m.letto = false and m.destinatarioIsDelete = false")
    ,
    @NamedQuery(name = "Messaggi.getMessaggiRicevuti", query = "SELECT m FROM Messaggi m Where m.destinatario = :utente and m.destinatarioIsDelete = false")
    ,
    @NamedQuery(name = "Messaggi.getMessaggiInviati", query = "SELECT m FROM Messaggi m Where m.mittente = :utente and m.mittenteIsDelete = false")
})

/**
 *
 * @author Boris Tagliarino Classe che permette lo scambio di Messaggi tra gli
 * utenti.
 */
@Entity
@XmlRootElement
public class Messaggi implements Serializable, Comparable<Messaggi> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String data;
    private String testo;
    private boolean letto;
    private String oggetto;
    private boolean mittenteIsDelete;
    private boolean destinatarioIsDelete;

    @ManyToOne
    private utente.Utente mittente;
    @ManyToOne
    private utente.Utente destinatario;

    /**
     * Ritorna l'identificativo del record
     *
     * @return l'identificativo del record
     */
    public Long getId() {
        return id;
    }

    /**
     * Restituisce la codifica hash dell'oggetto
     *
     * @return la codifica hash dell'oggetto
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /**
     * Indica se l'oggetto passato come parametro è uguale a questa istanza
     *
     * @param object l'oggetto che si vuole confrontare
     * @return true se l'object è di tipo Messaggi e hanno lo stesso id, false in
     * tutti gli altri casi
     */
    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (!(object instanceof Messaggi)) {
            return false;
        }
        Messaggi other = (Messaggi) object;
        if (other.id == null && id == null) {
            return true;
        }
        if (other.id == null && id != null || other.id != null && id == null) {
            return false;
        }
        return other.id == id;
    }

    /**
     * Fornisce una rappresentazione dell'oggetto
     *
     * @return una stringa che rappresenta l'oggetto.
     */
    @Override
    public String toString() {
        return "messaggi.Messaggi[ id=" + id + " ]";
    }

    /**
     * Restituisce la data di creazione del messaggio
     * @return la data di creazione del messaggio
     */
    public String getData() {
        return data;
    }

    /**
     * Permette di impostare la data di creazione del messaggio
     * @param data la data da impostare al messaggio
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * Ritorna il body del Messaggio
     * @return il testo del messaggio
     */
    public String getTesto() {
        return testo;
    }

    /**
     * Permette di settare il testo del messaggio
     * @param il testo che si vuole settare al messaggio.
     */
    public void setTesto(String testo) {
        this.testo = HtmlUtils.htmlEscape(testo);
    }

    /**
     * Permette di stabile se il messaggio è stato letto dal destinatario.
     * @return true se ilmessaggio è stato letto, false altrimenti.
     */
    public boolean isLetto() {
        return letto;
    }

    /**
     * Consente di settare il valore del messaggio.
     * @param letto false se il messaggio non è stato letto, true altrimenti.
     */
    public void setLetto(boolean letto) {
        this.letto = letto;
    }

    /**
     * restituisce l'utenza mittente legata al messaggio.
     * @return il mittente che ha mandato il messaggio
     */
    public utente.Utente getMittente() {
        return mittente;
    }

    /**
     * Consente di settare il mittente del messaggio
     * @param mittente l'utente da collegare al messaggio
     */
    public void setMittente(utente.Utente mittente) {
        this.mittente = mittente;
    }

    /**
     * Restituisce l'utenza destinatario legata al messaggio
     * @return l'utente che ha ricevuto il messaggio
     */
    public utente.Utente getDestinatario() {
        return destinatario;
    }

    /**
     * Consente di settare il destinatario del messaggio
     * @param destinatario l'utente da collegare al messaggio
     */
    public void setDestinatario(utente.Utente destinatario) {
        this.destinatario = destinatario;
    }

    
     /**
     * Attraverso la chiave data, permette di stabile la relazione d'ordine tra gli elementi di tipo Messaggi
     * @param o il messaggio che si vuole mettere in relazione
     * @return -1 se la data è maggiore del messaggio del parametro, 0 se sono equivalenti, 1 altrimenti
     */
    @Override
    public int compareTo(Messaggi o) {

        Messaggi att = (Messaggi) o;
        if (o == null) {
            return -1;
        }

        String orario1 = data.split(" ")[1];
        String date = data.split(" ")[0];

        String orario2 = att.data.split(" ")[1];
        String date2 = att.data.split(" ")[0];

        String[] att1 = date.split("-");
        String[] att2 = date2.split("-");

        //Confronto le date
        if (Integer.parseInt(att1[2]) < Integer.parseInt(att2[2])) {
            return 1;
        }
        if (Integer.parseInt(att1[2]) > Integer.parseInt(att2[2])) {
            return -1;
        }

        if (Integer.parseInt(att1[1]) < Integer.parseInt(att2[1])) {
            return 1;
        }
        if (Integer.parseInt(att1[1]) > Integer.parseInt(att2[1])) {
            return -1;
        }

        if (Integer.parseInt(att1[0]) < Integer.parseInt(att2[0])) {
            return 1;
        }
        if (Integer.parseInt(att1[0]) > Integer.parseInt(att2[0])) {
            return -1;
        }

        //Confronto gli orari
        String[] oraList1 = orario1.split(":");
        String[] oraList2 = orario2.split(":");

        if (Integer.parseInt(oraList1[0]) < Integer.parseInt(oraList2[0])) {
            return 1;
        }
        if (Integer.parseInt(oraList1[0]) > Integer.parseInt(oraList2[0])) {
            return -1;
        }

        if (Integer.parseInt(oraList1[1]) < Integer.parseInt(oraList2[1])) {
            return 1;
        }
        if (Integer.parseInt(oraList1[1]) > Integer.parseInt(oraList2[1])) {
            return -1;
        }

        if (Integer.parseInt(oraList1[2]) < Integer.parseInt(oraList2[2])) {
            return 1;
        }
        if (Integer.parseInt(oraList1[2]) > Integer.parseInt(oraList2[2])) {
            return -1;
        }

        return 0;
    }

    
    /**
     * Restituisce l'oggetto del messaggio
     * @return l'oggetto del messaggio
     */
    public String getOggetto() {
        return HtmlUtils.htmlEscape(oggetto);
    }

    /**
     * Permette di impostare l'oggetto per questo messaggio
     * @param oggetto l'oggetto da impostare a questo messaggio
     */
    public void setOggetto(String oggetto) {
        this.oggetto = oggetto;
    }

    /**
     * Stabilisce se il mittente ha cancellato il messaggio.
     * @return true se l'utente ha cancellato il messaggio, false altrimenti.
     */
    public boolean isMittenteIsDelete() {
        return mittenteIsDelete;
    }

    /**
     * Permette di settare un messaggio a cancellato
     * @param mittenteIsDelete true se si vuole settare il messaggio del mittente come cancellato, false altrimenti
     */
    public void setMittenteIsDelete(boolean mittenteIsDelete) {
        this.mittenteIsDelete = mittenteIsDelete;
    }

   /**
     * Stabilisce se il destinatario ha cancellato il messaggio.
     * @return true se l'utente ha cancellato il messaggio, false altrimenti.
     */
    public boolean isDestinatarioIsDelete() {
        return destinatarioIsDelete;
    }

    /**
     * Permette di settare un messaggio a cancellato
     * @param destinatarioIsDelete true se si vuole settare il messaggio del mittente come cancellato, false altrimenti
     */
    public void setDestinatarioIsDelete(boolean destinatarioIsDelete) {
        this.destinatarioIsDelete = destinatarioIsDelete;
    }

}
