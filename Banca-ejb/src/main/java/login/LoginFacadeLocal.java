package login;

import java.util.List;
import javax.ejb.Local;

@Local
public interface LoginFacadeLocal {

    void create(Login login);

    void edit(Login login);

    void remove(Login login);

    Login find(Object id);

    List<Login> findAll();

    List<Login> findRange(int[] range);

    int count();

    public List<Login> findByEmail(String email);

}
