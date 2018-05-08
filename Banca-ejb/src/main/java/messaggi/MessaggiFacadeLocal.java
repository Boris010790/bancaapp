package messaggi;

import java.util.List;
import javax.ejb.Local;


@Local
public interface MessaggiFacadeLocal {

    void create(Messaggi messaggi);

    void edit(Messaggi messaggi);

    void remove(Messaggi messaggi);

    Messaggi find(Object id);

    List<Messaggi> findAll();

    List<Messaggi> findRange(int[] range);

    int count();

    List<Messaggi> getMessaggiNonLetti(utente.Utente utente);

    List<Messaggi> getMessaggiRicevuti(utente.Utente utente);

    List<Messaggi> getMessaggiInviati(utente.Utente utente);

}
