/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.business.security.boundary;

import it.polimi.meteocal.business.security.entity.User;
import it.polimi.meteocal.entities.Event;
import it.polimi.meteocal.entities.Notification;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author Francesco
 */
@Stateless
public class NotificationManager {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    private EntityManager em;
    


    
    public void create(Notification entity) {
        em.persist(entity);
    }

    public void remove(Notification entity) {
        em.remove(em.merge(entity));
    }

    public Notification find(Object id) {
        return em.find(Notification.class, id);
    }

    public List<Notification> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Notification.class));
        return em.createQuery(cq).getResultList();
    }
    
    
    //metodo di prova per vedere se il salvataggio della notifica nel db avviene bene
    public void inviteUser(Notification notification) {
        notification.setText("Sei stato invitato");
        notification.setType("INVITO");
        notification.setTimestamp(new Date(System.currentTimeMillis() % 1000));
        notification.setSender(new User());
        notification.setEvent(new Event());
        em.persist(notification);
    }
    
}
