package it.polimi.meteocal.business.security.boundary;

import it.polimi.meteocal.business.security.entity.User;
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

    public List<String> searchUsers(String parameter) {
        String param = "Ciccio";
        parameter = param;
        Query email= em.createNamedQuery("User.findByEmail");
        email.setParameter("email", param);
        Query name= em.createNamedQuery("User.findByName");
        name.setParameter("name", param);
        Query surname= em.createNamedQuery("User.findBySurname");
        surname.setParameter("surname", param);
        Query phone= em.createNamedQuery("User.findByPhone");
        phone.setParameter("phone", param);
        List<String> results = new ArrayList<>();
        results.addAll(email.getResultList());
        results.addAll(name.getResultList());
        results.addAll(surname.getResultList());
        results.addAll(phone.getResultList());
//        ArrayList<User> users = new ArrayList<>();
//        for(int i=0; i<results.size(); i++){
//            User u = em.find(User.class, results.get(i));
//            users.add(u);
//        }
        return results;
    }

}
