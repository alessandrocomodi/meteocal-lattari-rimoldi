/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.business.manager;

import it.polimi.meteocal.entities.Event;
import it.polimi.meteocal.entities.User;
import it.polimi.meteocal.utilities.PasswordEncrypter;
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
public class EventManagerIT {
    
    @EJB
    EventManager eventManager;
    
    @EJB
    UserManager userManager;
    
    @PersistenceContext
    EntityManager em;
    
    @Resource
    private UserTransaction utx;
    
    @Deployment
    public static WebArchive createArchiveAndDeploy() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(EventManager.class, UserManager.class, Event.class, User.class)
                .addPackage(Event.class.getPackage())
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    @Test
    public void correctOwnerAndEventOrganizedAfterCreation() {
        //I save a new user
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
        
        //User correctly created
        User returned = em.find(User.class, user.getEmail());
        assertNotNull(returned);
        assertEquals("user@gmail.com", returned.getEmail());
        assertEquals("user", returned.getName());
        assertEquals("surname", returned.getSurname());
        assertEquals(PasswordEncrypter.encryptPassword("password"), returned.getPassword());
        assertEquals("USERS", returned.getGroupName());
        
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
        
        Event returnedEvent = results.get(1);
        
        assertNotNull(returnedEvent);
        assertTrue(returnedEvent.getIdevent() == 2);
        assertTrue(returnedEvent.getOwner().getEmail().equals("user@gmail.com"));
    }
    
    @Test
    public void eventCorrectlyUpdate(){
        User user2 = new User("user2@gmail.com", "user", "surname", "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8");
        user2.setGroupName("USERS");
        List<Event> eventOrganized = new ArrayList<>();
        user2.setEventOrganized(eventOrganized);
        
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
        
        Event event2 = new Event();
        event2.setIndoor(false);
        event2.setPrivate1(false);
        event2.setStarttime(new Date(2015, 02, 25, 5, 15));
        event2.setEndtime(new Date(2015, 03, 26, 6, 00));
        event2.setName("test");
        event2.setDescription("testing..");
        event2.setPlace("Milano");
        event2.setLat("45");
        event2.setLon("9");
        
        eventManager.createEvent(event2, user2);
        
        Event eventTemp = new Event();
        eventTemp.setIndoor(false);
        eventTemp.setPrivate1(true);
        eventTemp.setStarttime(new Date(2015, 02, 25, 5, 15));
        eventTemp.setEndtime(new Date(2015, 03, 26, 6, 00));
        eventTemp.setName("test2");
        eventTemp.setDescription("testing..");
        eventTemp.setPlace("Milano");
        eventTemp.setLat("45");
        eventTemp.setLon("9");
        
        eventManager.update(em.find(Event.class, 1), eventTemp);
        
        Event afterUpdateEvent = em.find(Event.class, 1);
        
        assertFalse(afterUpdateEvent.getIndoor());
        assertTrue(afterUpdateEvent.getPrivate1());
        assertEquals(new Date(2015, 02, 25, 5, 15), afterUpdateEvent.getStarttime());
        assertEquals(new Date(2015, 03, 26, 6, 00), afterUpdateEvent.getEndtime());
        assertEquals("test2", afterUpdateEvent.getName());
        assertEquals("testing..", afterUpdateEvent.getDescription());
        assertEquals("Milano", afterUpdateEvent.getPlace());
        assertEquals("45", afterUpdateEvent.getLat());
        assertEquals("9", afterUpdateEvent.getLon());
    }
}
