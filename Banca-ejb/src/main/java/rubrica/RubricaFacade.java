package rubrica;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import utente.Utente;


@Stateless
public class RubricaFacade extends AbstractFacade<Rubrica> implements RubricaFacadeLocal {

    @PersistenceContext(unitName = "com.mycompany_Banca-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RubricaFacade() {
        super(Rubrica.class);
    }

    @Override
    public List<Rubrica> findByUtente(Utente owner) {
        return em.createNamedQuery("Rubrica.findByUtente", Rubrica.class).setParameter("owner", owner).getResultList();
    }

}
