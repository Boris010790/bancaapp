package rubrica;

import java.util.List;
import javax.ejb.Local;
import utente.Utente;


@Local
public interface RubricaFacadeLocal {

    void create(Rubrica rubrica);

    void edit(Rubrica rubrica);

    void remove(Rubrica rubrica);

    Rubrica find(Object id);

    List<Rubrica> findAll();

    List<Rubrica> findRange(int[] range);

    int count();
    
    List<Rubrica> findByUtente(Utente owner);
}
