package utente;

import java.util.List;
import javax.ejb.Local;


@Local
public interface UtenteFacadeLocal {

    void create(Utente utente);

    void edit(Utente utente);

    void remove(Utente utente);

    Utente find(Object id);

    List<Utente> findAll();

    List<Utente> findRange(int[] range);

    int count();
    
    public List<Utente> findByEmail(String email);
}
