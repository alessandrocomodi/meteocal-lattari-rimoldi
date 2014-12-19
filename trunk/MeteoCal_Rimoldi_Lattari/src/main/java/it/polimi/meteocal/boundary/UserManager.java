/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.boundary;

//import it.polimi.meteocal.entities.Group;
import it.polimi.meteocal.entities.User;
import java.security.Principal;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author miglie
 */
@Stateless
public class UserManager {

    @PersistenceContext
    EntityManager em;
    
    @Inject
    Principal principal;

    public void save(User user) {
       //s user.setGroupName(Group.user);
        em.persist(user);
    }

    public void unregister() {
        em.remove(getLoggedUser());
    }

    public User getLoggedUser() {
        return em.find(User.class, principal.getName());
    }
    
}
