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
import javax.ejb.embeddable.EJBContainer;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author Francesco
 */
public class NotificationManagerTest {
    
    private NotificationManager nm;
    
    private Notification notification;
    
    private Event event;
    
    private User user1;
    private User user2;
    
    private Query query;
    
    public NotificationManagerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        nm = new NotificationManager();
        nm.em = mock(EntityManager.class);
        
        notification = new Notification(1, new Date(2015, 02, 25, 5, 15), "INVITATION", "You have been invited to an event");
        event = new Event(50, false, false, new Date(2015, 02, 25, 5, 15), new Date(2015, 03, 26, 6, 00), "test", "2015-02-25:500:light rain:21.97:26.24", "Milano");
        user1 = new User("user@hotmail.it", "User", "UserSurname", "4c94485e0c21ae6c41ce1dfe7b6bfaceea5ab68e40a2476f50208e526f506080");
        user2 = new User("user2@hotmail.it", "User2", "UserSurname2", "4c94485e0c21ae6c41ce1dfe7b6bfaceea5ab68e40a2476f50208e526f506080");
        
        List<Notification> user1Not = new ArrayList<>();
        List<Notification> user2Not = new ArrayList<>();
        
        List<Notification> results = new ArrayList<>();
        results.add(notification);
        
        user1.setNotificationCollection(user1Not);
        user2.setNotificationCollection(user2Not);
        
        when(nm.em.find(Notification.class, notification.getIdnotification())).thenReturn(notification);
        when(nm.em.find(User.class, user1.getEmail())).thenReturn(user1);
        when(nm.em.find(User.class, user2.getEmail())).thenReturn(user2);
        when(nm.em.find(Event.class, event.getIdevent())).thenReturn(event);
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void createTest(){
        nm.create(notification);
        verify(nm.em, times(1)).persist(notification);
    }
    
    @Test
    public void deleteTest(){
        
        nm.remove(notification);
        verify(nm.em, times(1)).remove(nm.em.merge(notification));
        
    }
    
    @Test
    public void createEventDeletedNotificationTest(){
        //supponiamo che i partecipanti siano due
        List<User> participants = new ArrayList<>();
        participants.add(user1);
        participants.add(user2);
        
        nm.createEventDeletedNotification(participants, event.getIdevent());
        
        assertTrue(user1.getNotificationCollection().size()==1);
        assertTrue(user2.getNotificationCollection().size()==1);
        
        verify(nm.em, times(1)).merge(user1);
        verify(nm.em, times(1)).merge(user2);
        
    }
    
    @Test
    public void createWeatherNotificationForParticipantsTest(){
        //Due partecipanti
        List<User> participants = new ArrayList<>();
        participants.add(user1);
        participants.add(user2);
        
        //User1 ha già una notifica
        Notification newNotification = new Notification();
        user1.getNotificationCollection().add(newNotification);
        
        event.setUserCollection(participants);
        
        nm.createWeatherNotificationForParticipants(event);
        
        assertTrue(user1.getNotificationCollection().size()==2);
        assertTrue(user1.getNotificationCollection().contains(newNotification));
        assertTrue(user2.getNotificationCollection().size()==1);
        
        verify(nm.em, times(1)).merge(user1);
        verify(nm.em, times(1)).merge(user2);
                
    }
    
    @Test
    public void createWeatherNotificationForOwnerTest(){
        //user1 organizer
        event.setOwner(user1);
        
        nm.createWeatherNotificationForOwner(event);
        
        assertTrue(user1.getNotificationCollection().size()==1);
        verify(nm.em, times(1)).merge(user1);
    }
    
    @Test
    public void createSuggestionNotificationTest(){
        //user1 organizer
        event.setOwner(user2);
        
        nm.createWeatherNotificationForOwner(event);
        
        assertTrue(user2.getNotificationCollection().size()==1);
        verify(nm.em, times(1)).merge(user2);
    }
}
