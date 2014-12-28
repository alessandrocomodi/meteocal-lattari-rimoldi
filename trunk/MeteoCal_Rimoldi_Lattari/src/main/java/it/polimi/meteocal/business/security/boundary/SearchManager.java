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

    public ArrayList<User> searchUsers(String parameter) {
        Query email= em.createNamedQuery("User.findByEmail");
        email.setParameter("email", ""+parameter+"");
        Query name= em.createNamedQuery("User.findByName");
        name.setParameter("name", ""+parameter+"");
        Query surname= em.createNamedQuery("User.findBySurname");
        surname.setParameter("surname", ""+parameter+"");
        Query phone= em.createNamedQuery("User.findByPhone");
        phone.setParameter("phone", ""+parameter+"");
        ArrayList results= new ArrayList<String>();
        results.addAll(email.getResultList());
        results.addAll(name.getResultList());
        results.addAll(surname.getResultList());
        results.addAll(phone.getResultList());
        ArrayList users= new ArrayList<User>();
        for(int i=0; i<results.size(); i++){
            User u = em.find(User.class, results.get(i));
            users.add(u);
        }
        return users;
    }

}
