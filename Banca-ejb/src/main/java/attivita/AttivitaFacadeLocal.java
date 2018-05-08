
package attivita;

import java.util.List;
import javax.ejb.Local;

@Local
public interface AttivitaFacadeLocal {

    void create(Attivita attivita);

    void edit(Attivita attivita);

    void remove(Attivita attivita);

    Attivita find(Object id);

    List<Attivita> findAll();

    List<Attivita> findRange(int[] range);

    int count();

    public abstract List<Attivita> ricevute(utente.Utente utente);

    public abstract List<Attivita> effettuate(utente.Utente utente);

    public abstract List<Attivita> recenti(utente.Utente utente);

}
