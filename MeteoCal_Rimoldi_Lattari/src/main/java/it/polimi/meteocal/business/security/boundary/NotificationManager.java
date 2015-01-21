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
import javax.faces.bean.ManagedProperty;
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
        Notification temp = em.find(Notification.class, entity.getIdnotification());
        em.remove(em.merge(temp));
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
        notification.setText("The event: " + event2.getName() + " in date: " + event2.getStarttime() + " was deleted");
        notification.setUserCollection(userCollection);
        em.persist(notification);
        for(User u : userCollection){
            u.getNotificationCollection().add(notification);
            em.merge(u);
        }
    }

    public void removeEveryReletedNotification(Integer idevent) {
        Query query = em.createNamedQuery("Notification.findByEvent");
        query.setParameter("idevent", idevent);
        List<Notification> results = new ArrayList<>();
        results.addAll(query.getResultList());
        for (Notification n : results) {
            for(User u : n.getUserCollection()){
                u.getNotificationCollection().remove(n);
                em.merge(u);
            }
            this.remove(n);
        }
    }

    public void createEventModifiedNotification(List<User> participants, Integer idevent) {
        Notification notification = new Notification();
        Event event2 = em.find(Event.class, idevent);
        notification.setEvent(event2);
        notification.setSender(event2.getOwner());
        notification.setType("MODIFICATION");
        Calendar cal = Calendar.getInstance();
        notification.setTimestamp(cal.getTime());
        notification.setText("The details of an event have been changed");
        notification.setUserCollection(participants);
        em.persist(notification);
    }
    

    public void createWeatherNotificationForParticipants(Event e) {
        if (!e.getUserCollection().isEmpty()) {
            Notification notification = new Notification();
            notification.setEvent(e);
            notification.setSender(e.getOwner());
            notification.setText("Bad weather conditions for one of your joined events");
            notification.setType("WEATHERALERTFORALL");
            Calendar cal = Calendar.getInstance();
            notification.setTimestamp(cal.getTime());
            notification.setUserCollection(e.getUserCollection());
            em.persist(notification);  
        }
         
    }

    public void createWeatherNotificationForOwner(Event e) {
        Notification notification = new Notification();
        notification.setEvent(e);
        notification.setSender(e.getOwner());
        notification.setText("Bad weather conditions for one of your organized events");
        notification.setType("WEATHERALERTFOROWNER");
        Calendar cal = Calendar.getInstance();
        notification.setTimestamp(cal.getTime());
        List<User> owner = new ArrayList<>();
        owner.add(e.getOwner());
        notification.setUserCollection(owner);
        em.persist(notification);
    }

    public void createSuggestionNotification(String suggestion, Event e) {
        Notification notification = new Notification();
        notification.setText(suggestion);
        Calendar cal = Calendar.getInstance();
        notification.setTimestamp(cal.getTime());
        notification.setType("SUGGESTION");
        List<User> owner = new ArrayList<>();
        owner.add(e.getOwner());
        notification.setUserCollection(owner);
        em.persist(notification);
    }
}
