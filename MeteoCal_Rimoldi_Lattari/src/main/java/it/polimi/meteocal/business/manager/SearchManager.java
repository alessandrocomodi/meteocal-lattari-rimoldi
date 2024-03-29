package it.polimi.meteocal.business.manager;

import it.polimi.meteocal.entities.User;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Alessandro
 */
@Stateless
public class SearchManager {
    
    @PersistenceContext
    private EntityManager em;

    public List<User> searchUsers(String parameter) {
        String param = parameter;
        Query email= em.createNamedQuery("User.findByEmail");
        email.setParameter("email", param);
        Query name= em.createNamedQuery("User.findByName");
        name.setParameter("name", param);
        Query surname= em.createNamedQuery("User.findBySurname");
        surname.setParameter("surname", param);
        Query phone= em.createNamedQuery("User.findByPhone");
        phone.setParameter("phone", param);
        List<User> results = new ArrayList<>();
        results.addAll(email.getResultList());
        results.addAll(name.getResultList());
        results.addAll(surname.getResultList());
        results.addAll(phone.getResultList());
        return results;
    }

}
