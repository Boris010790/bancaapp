/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rubrica;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Boris Tagliarino
 * Classe che permette di collegare i contatti più usati nelle transazioni.
 */
@Entity
@NamedQueries({
    @NamedQuery(name="Rubrica.findByUtente", query="SELECT r FROM Rubrica r Where r.owner = :owner")
})
@XmlRootElement
public class Rubrica implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private utente.Utente owner;
    @ManyToOne
    private utente.Utente contatto;

    
     /**
     * Ritorna l'identificativo del record
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
     * @return true se l'object è di tipo Rubrica e hanno lo stesso id, false in
     * tutti gli altri casi
     */
    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (!(object instanceof Rubrica)) {
            return false;
        }
        Rubrica other = (Rubrica) object;
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
        return "rubrica.Rubrica[ id=" + id + " ]";
    }
    
    
    /**
     * Restituisce l'utente proprietario del contatto
     * @return l'utente proprietario del contatto
     */
    public utente.Utente getOwner() {
        return owner;
    }

    /**
     * Setta l'utente proprietario del contatto
     * @owner assegna l'utente come proprietario del contatto
     */
    public void setOwner(utente.Utente owner) {
        this.owner = owner;
    }

    /**
     * Restituisce l'utenza salvata in rubrica
     * @return l'utenza salvata in rubrica
     */
    public utente.Utente getContatto() {
        return contatto;
    }

    /**
     * Setta l'utente nella rubrica
     @param contatto l'utenza settata nella rubrica.
     */
    public void setContatto(utente.Utente contatto) {
        this.contatto = contatto;
    }
    
}
