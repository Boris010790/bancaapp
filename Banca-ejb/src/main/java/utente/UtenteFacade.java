package utente;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class UtenteFacade extends AbstractFacade<Utente> implements UtenteFacadeLocal {

    @PersistenceContext(unitName = "com.mycompany_Banca-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UtenteFacade() {
        super(Utente.class);
    }
    
    @Override
    public List<Utente> findByEmail(String email){
       return em.createNamedQuery("Utente.findByEmail", Utente.class).setParameter("email",email).getResultList();
   }
 
}
