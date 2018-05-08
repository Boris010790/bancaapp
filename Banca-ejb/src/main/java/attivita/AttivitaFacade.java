
package attivita;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Stateless
public class AttivitaFacade extends AbstractFacade<Attivita> implements AttivitaFacadeLocal {

    @PersistenceContext(unitName = "com.mycompany_Banca-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AttivitaFacade() {
        super(Attivita.class);
    }

    @Override
    public List<Attivita> ricevute(utente.Utente utente) {
       return em.createNamedQuery("Attivita.ricevute", Attivita.class).setParameter("utente",utente).getResultList();
    }

    @Override
    public List<Attivita> effettuate(utente.Utente utente) {
       return em.createNamedQuery("Attivita.effettuate", Attivita.class).setParameter("utente",utente).getResultList();
    }

    @Override
    public List<Attivita> recenti(utente.Utente utente) {
       return em.createNamedQuery("Attivita.recenti", Attivita.class).setParameter("utente",utente).getResultList();
    }
    
    
    
}
