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

    public List<User> searchUsers(String parameter) {
        //al momento il parametro passato è sempre null (da fixare), quindi per
        //testare la query gli faccio cercare sempre Ciccio (che sul mio db esiste)
        //ed effettivamente trova il giusto risultato
        String param = "Alessandro";
        parameter = param;
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
