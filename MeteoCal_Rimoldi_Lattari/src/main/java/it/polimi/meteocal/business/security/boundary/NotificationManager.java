/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.business.security.boundary;

import it.polimi.meteocal.business.security.entity.User;
import it.polimi.meteocal.entities.Event;
import it.polimi.meteocal.entities.Notification;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Francesco
 */
@Stateless
public class NotificationManager {

    @PersistenceContext
    private EntityManager em;
    


    
    public void create(Notification entity) {
        em.persist(entity);
    }
    
    public void createInvitation(List<User> invitedUsers, Integer event){
        Notification notification = new Notification();
        Event event2 = em.find(Event.class, event);
        notification.setEvent(event2);
        notification.setSender(event2.getOwner());
        notification.setType("INVITATION");
        Calendar cal = Calendar.getInstance();
        notification.setTimestamp(cal.getTime());
        System.out.println(cal.getTime());
        notification.setText("You have been invited to an event");
        notification.setUserCollection(invitedUsers);
        em.persist(notification);
    }

    public void remove(Notification entity) {
        em.remove(em.merge(entity));
    }
    
    public List<Notification> getLastInvitation(Integer idevent) {
        Query query1= em.createNamedQuery("Notification.findByEvent");
        query1.setParameter("idevent", idevent);
        List<Notification> results = new ArrayList<>();
        results.addAll(query1.getResultList());
        return results;
    }
    
    public Notification find(Object id) {
        return em.find(Notification.class, id);
    }

    public List<Notification> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Notification.class));
        return em.createQuery(cq).getResultList();
    }

    public void createEventDeletedNotification(List<User> userCollection, Integer event) {
        Notification notification = new Notification();
        Event event2 = em.find(Event.class, event);
        notification.setSender(event2.getOwner());
        notification.setType("DELETION");
        Calendar cal = Calendar.getInstance();
        notification.setTimestamp(cal.getTime());
        System.out.println(cal.getTime());
        notification.setText("The event was deleted");
        notification.setUserCollection(userCollection);
        em.persist(notification);
    }

    public void removeEveryReletedNotification(Integer idevent) {
        Query query = em.createNamedQuery("Notification.findByEvent");
        query.setParameter("idevent", idevent);
        List<Notification> results = new ArrayList<>();
        results.addAll(query.getResultList());
        for (Notification n : results) {
            this.remove(n);
        }
    }
}
