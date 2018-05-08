package messaggi;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import login.Login;
import utente.Utente;


@Stateless
public class MessaggiFacade extends AbstractFacade<Messaggi> implements MessaggiFacadeLocal {

    @PersistenceContext(unitName = "com.mycompany_Banca-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MessaggiFacade() {
        super(Messaggi.class);
    }

    @Override
    public List<Messaggi> getMessaggiNonLetti(Utente utente) {
           return em.createNamedQuery("Messaggi.getMessaggiNonLetti", Messaggi.class).setParameter("utente",utente).getResultList();
    }

    @Override
    public List<Messaggi> getMessaggiRicevuti(Utente utente) {
           return em.createNamedQuery("Messaggi.getMessaggiRicevuti", Messaggi.class).setParameter("utente",utente).getResultList();
    }

    @Override
    public List<Messaggi> getMessaggiInviati(Utente utente) {
           return em.createNamedQuery("Messaggi.getMessaggiInviati", Messaggi.class).setParameter("utente",utente).getResultList();
    }
    

    
}
