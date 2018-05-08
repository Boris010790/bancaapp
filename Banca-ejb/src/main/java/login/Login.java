package login;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;
import utente.Utente;

@Entity

@NamedQueries({
    @NamedQuery(name = "Login.findByEmail", query = "SELECT l FROM Login l Where l.email = :email")
//@NamedQuery(name="Login.createLogin", query="INSERT INTO LOGIN(EMAIL, PASSWORD, UTENTE) VALUES(:email, :password, :utente)")

})
/**
 *
 * @author Boris Tagliarino
 * Classe che permette di tracciare le credenziali di accesso per il singolo utente.
 */


@XmlRootElement
public class Login implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    private String password;
    @ManyToOne
    private Utente utente;
    private String provider;

    
    /**
     * Restituisce il Provider che autentica l'utente.
     * @return il Provider che autentica l'utente.
     */
    public String getProvider() {
        return provider;
    }

    /**
     * Permette di settare il provider che autentica l'utente
     * @param provider  colui che garantisce chi l'utente dice di essere.
     */
    public void setProvider(String provider) {
        this.provider = provider;
    }

    /**
     * Restituisce la mail con la quale l'utente ha effettuato l'accesso.
     * @return la mail dell'utente
     */
    public String getEmail() {
        return email;
    }

      /**
     * Setta l'email dell'utente in fase di registrazione o di primo accesso con login esterno.
     * @param email la mail da settare all'oggetto Login
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Ritorna la password dell'utente codificata.
     * @return la password dell'utente codificata.
     */
       
    
    public String getPassword() {
        return password;
    }
    
    
/**
 * Setta la password per l'oggetto Login.
 * @param password la password che si vuole settare.
 */
    public void setPassword(String password) {
        this.password = password;
    }

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
     * @return true se l'object è di tipo Login e hanno lo stesso id, false in tutti gli altri casi
     */
    @Override
    public boolean equals(Object object) {
        if(object == null) return false;
        if (!(object instanceof Login)) {
            return false;
        }
        Login other = (Login) object;
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
        return "bean.Login[ id=" + id + " ]";
    }
    
    /**
     * Ritorna l'utenza collegata al Login
     * @return l'utenza collegata al Login
     */

    public Utente getUtente() {
        return utente;
    }

    /**
     * Consente di collegare l'utenza al Login
     * @param utente  l'utenza da collegare al Login
     */
    public void setUtente(Utente utente) {
        this.utente = utente;
    }

}
