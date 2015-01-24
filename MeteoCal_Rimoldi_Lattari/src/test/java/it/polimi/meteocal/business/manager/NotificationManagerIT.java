/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.business.manager;

import it.polimi.meteocal.entities.Event;
import it.polimi.meteocal.entities.Notification;
import it.polimi.meteocal.entities.User;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

/**
 *
 * @author Francesco
 */
@RunWith(Arquillian.class)
public class NotificationManagerIT {
    
    @EJB
    NotificationManager nm;
    
    @EJB
    EventManager eventManager;
    
    @EJB
    UserManager um;
    
    @PersistenceContext
    EntityManager em;
    
    @Resource
    private UserTransaction utx;
    
    @Deployment
    public static WebArchive createArchiveAndDeploy() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(NotificationManager.class,EventManager.class, UserManager.class, Event.class, User.class, Notification.class)
                .addPackage(Notification.class.getPackage())
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    @Test
    public void invitationCorrectlyCreatedPlusRemoveTest(){
        //Creo due utenti: user è l'organizzatore e user2 è l'invitato
        User user = new User("user@gmail.com", "user", "surname", "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8");
        user.setGroupName("USERS");
        List<Event> eventOrganized = new ArrayList<>();
        user.setEventOrganized(eventOrganized);
        
        try {
            utx.begin();
        } catch (NotSupportedException | SystemException ex) {
            Logger.getLogger(EventManagerIT.class.getName()).log(Level.SEVERE, null, ex);
        }
        em.persist(user);
        try {
            utx.commit();
        } catch (RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException | SystemException ex) {
            Logger.getLogger(EventManagerIT.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        User user2 = new User("user2@gmail.com", "user2", "surname", "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8");
        user2.setGroupName("USERS");
        List<Event> eventOrganizedUser2 = new ArrayList<>();
        user2.setEventOrganized(eventOrganizedUser2);
        
        try {
            utx.begin();
        } catch (NotSupportedException | SystemException ex) {
            Logger.getLogger(EventManagerIT.class.getName()).log(Level.SEVERE, null, ex);
        }
        em.persist(user2);
        try {
            utx.commit();
        } catch (RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException | SystemException ex) {
            Logger.getLogger(EventManagerIT.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Event event = new Event();
        event.setIndoor(false);
        event.setPrivate1(false);
        event.setStarttime(new Date(2015, 02, 25, 5, 15));
        event.setEndtime(new Date(2015, 03, 26, 6, 00));
        event.setName("test");
        event.setDescription("testing..");
        event.setPlace("Milano");
        event.setLat("45");
        event.setLon("9");
        
        eventManager.createEvent(event, user);
        
        //Check if the event is created and if it has the correct owner
        List<Event> results = new ArrayList<>();
        javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Event.class));
        results = em.createQuery(cq).getResultList();
        
        Event returnedEvent = results.get(0);
        
        User temp = um.getUserFromEmail("user2@gmail.com");
        List<User> invited = new ArrayList<>();
        invited.add(temp);
        
        nm.createInvitation(invited, returnedEvent.getIdevent());
        
        List<Notification> notifications = new ArrayList<>();
        javax.persistence.criteria.CriteriaQuery cq2 = em.getCriteriaBuilder().createQuery();
        cq2.select(cq2.from(Notification.class));
        notifications = em.createQuery(cq2).getResultList();
        
        Notification not = notifications.get(0);
        
        assertTrue(notifications.size()==1);
        assertTrue(notifications.contains(not));
        assertEquals("You have been invited to an event", not.getText());
        assertEquals("INVITATION", not.getType());
        assertNotNull(not.getTimestamp());
        assertTrue(not.getUserCollection().size()==1);
        
        User user2Temp = um.getUserFromEmail("user2@gmail.com");
        
        assertTrue(not.getUserCollection().contains(user2Temp));
        
        Event eventTemp = em.find(Event.class, 1);
        
        assertTrue(not.getEvent().getIdevent().equals(eventTemp.getIdevent()));
        
        nm.remove(not);
        
        List<Notification> notificationsAfterDelete = new ArrayList<>();
        javax.persistence.criteria.CriteriaQuery cq3 = em.getCriteriaBuilder().createQuery();
        cq3.select(cq3.from(Notification.class));
        notificationsAfterDelete = em.createQuery(cq3).getResultList();
        
        assertTrue(notificationsAfterDelete.isEmpty());
    }
}

