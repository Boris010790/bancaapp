package utente;

import java.io.Serializable;
import java.text.DecimalFormat;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;
import org.springframework.web.util.HtmlUtils;

/**
 *
 * @author Boris Tagliarino Classe che permette la gestione delle utenze.
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Utente.findByEmail", query = "SELECT u FROM Utente u Where u.email = :email")
})

@XmlRootElement
public class Utente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Long id;
    private String nome;
    private String cognome;
    private String email;
    private String tipo;
    private double saldo;

    /**
     * Il saldo disponibile per l'utente
     *
     * @return l'importo disponibile
     */
    public double getSaldo() {
        return saldo;
    }

    /**
     * Permette di settare un nuovo saldo per l'utente
     *
     * @param saldo importo che si vuole modificare
     */
    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

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
     * @return true se l'object è di tipo Utente e hanno lo stesso id, false in
     * tutti gli altri casi
     */
    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (!(object instanceof Utente)) {
            return false;
        }
        Utente other = (Utente) object;
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
        return "utente.Utente[ id=" + id + " ]";
    }

    /**
     * Restituisce il nome dell'utente.
     *
     * @return il nome dell'utente
     */
    public String getNome() {
        return capitalize(nome);
    }

    /**
     * Setta il nome per quell'utenza
     *
     * @param nome che si vuole dare all'utenza.
     */
    public void setNome(String nome) {
        this.nome = HtmlUtils.htmlEscape(nome);
    }

    /**
     * Restituisce il cognome dell'utente.
     *
     * @return il cognome dell'utente
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Setta il cognome per quell'utenza
     *
     * @param cognome che si vuole settare all'utenza.
     */
    public void setCognome(String cognome) {
        this.cognome = HtmlUtils.htmlEscape(cognome);
    }
    
    /**
     Restituisce la mail dove l'utente vuole ricevere i pagamenti
     * @return la mail dove l'utente vuole ricevere i pagamenti
     */

    public String getEmail() {
        return email;
    }

    /**
     Permette di settare la email per questa utenza
     * @param email il valore che si vuole dare all'utenza
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Restituisce il tipo di utenza
     * @return Client se è una normale utenza, Admin altrimenti.
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Permette di settare un tipo di utenza
     * @param tipo la tipologia  che si vuole settare per l'utenza.
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Restituisce una stringa con il primo carattere maiuscolo seguita da tutti gli altri in minuscolo
     * @param s la stringa che si vuole trasformare
     * @return una stringa con il primo carattere maiuscolo seguita da tutti gli altri in minuscolo
     */
    private static String capitalize(String s) {
        return s.charAt(0) + "".toUpperCase() + "" + s.substring(1, s.length()).toLowerCase();
    }

}
