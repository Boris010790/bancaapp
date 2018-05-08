package attivita;

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

/**
 *
 * @author Boris Tagliarino
 * Classe che ha lo scopo di tracciare lo storico delle transazioni effettuate e/o ricevute.
 */
@NamedQueries({
    @NamedQuery(name = "Attivita.ricevute", query = "SELECT a FROM Attivita a Where a.destinatario = :utente")
    ,
    @NamedQuery(name = "Attivita.effettuate", query = "SELECT a FROM Attivita a Where a.mittente = :utente")
    ,
    @NamedQuery(name = "Attivita.recenti", query = "SELECT a FROM Attivita a Where a.mittente = :utente or a.destinatario = :utente ")
})

@Entity
@XmlRootElement
public class Attivita implements Serializable, Comparable<Attivita> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String causale;
    @ManyToOne
    private utente.Utente mittente;
    @ManyToOne
    private utente.Utente destinatario;
    private double importo;
    private String data;

    
    /**
     * Ritorna l'identificativo del record
     * @return l'identificativo del record
     */
    public Long getId() {
        return id;
    }

    /**
     * Restituisce la codifica hash dell'oggetto
     * @return la codifica hash dell'oggetto
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /**
     *  Indica se l'oggetto passato come parametro è uguale a questa istanza
     * @param object l'oggetto che si vuole confrontare
     * @return true se l'object è di tipo Attività e hanno lo stesso id, false in tutti gli altri casi
     */
    @Override
    public boolean equals(Object object) {
        if(object == null) return false;
        if (!(object instanceof Attivita)) {
            return false;
        }
        Attivita other = (Attivita) object;
        if(other.id==null && id == null) return true;
        if(other.id == null && id != null || other.id !=null && id ==null) return false;
        return other.id == id;
    }

    /**
     * Fornisce una rappresentazione dell'oggetto
     * @return una stringa che rappresenta l'oggetto.
     */
     
    @Override
    public String toString() {
        return "attivita.Attivita[ id=" + id + " ]";
    }

    /**
     * Ritorna il valore della causale
     * @return il valore della causale
     */
    public String getCausale() {
        return causale;
    }

    /**
     * Permette di valorizzare il campo causale
     * @param causale è il valore da settare all'istanza.
     */
    public void setCausale(String causale) {
        this.causale = HtmlUtils.htmlEscape(causale);
    }
    
    /**
     * Ritorna il valore del mittente
     * @return colui che ha effettuato la transazione
     */
    public utente.Utente getMittente() {
        return mittente;
    }

     /**
     * Permette di valorizzare l'utente che ha effettuato la transazione
     * @param mittente l'utente che ha effettuato la transazione
     */
    public void setMittente(utente.Utente mittente) {
        this.mittente = mittente;
    }

    /**
     * Ritorna il valore del destinatario
     * @return colui che ha ricevuto la transazione
     */
    public utente.Utente getDestinatario() {
        return destinatario;
    }

    /**
     * Permette di valorizzare l'utente che ha ricevuto la transazione
     * @param destinatario l'utente che ha ricevuto la transazione
     */
    public void setDestinatario(utente.Utente destinatario) {
        this.destinatario = destinatario;
    }

    /**
     * Restituisce l'importo della transazione
     * @return l'importo della transazione
     */
    public double getImporto() {
        return importo;
    }

    /**
     * Permette di valorizzare l'importo all'oggetto attività
     * @param importo l'importo che si vuole settare per questa attività
     */
    public void setImporto(double importo) {
        this.importo = importo;
    }

    /**
     * Restituisce la data della transazione
     * @return data della transazione
     */
    public String getData() {
        return data;
    }

    /**
     * Permette di settare la data della transazione
     * @data data la data che si vuole settare per questa transazione
     */
    public void setData(String data) {
        this.data = data;
    }

    
    /**
     * Attraverso la chiave data, permette di stabile la relazione d'ordine tra gli elementi di tipo Attivita
     * @param o l'attività che si vuole mettere in relazione
     * @return -1 se la data è maggiore dell'attività passata da parametro, 0 se sono equivalenti, 1 altrimenti
     */
    @Override
    public int compareTo(Attivita o) {
        Attivita att = (Attivita) o;
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

}
