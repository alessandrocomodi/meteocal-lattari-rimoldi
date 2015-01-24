/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.business.manager;

import it.polimi.meteocal.entities.Event;
import it.polimi.meteocal.entities.User;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.embeddable.EJBContainer;
import javax.persistence.EntityManager;
import static org.hamcrest.CoreMatchers.is;
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
public class EventManagerTest {
    
    private UserManager um;
    
    private EventManager eventManager;
    
    private User user;
    private User user2;
    private User user4;
    private Event event;
    
    public EventManagerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        eventManager = new EventManager();
        eventManager.em = mock(EntityManager.class);
        event = new Event(50, false, false, new Date(2015, 02, 25, 5, 15), new Date(2015, 03, 26, 6, 00), "test", "2015-02-25:500:light rain:21.97:26.24", "Milano");
        event.setLat("45.4654220");
        event.setLon("9.1859240");
        user = new User("user@hotmail.it", "User", "UserSurname", "4c94485e0c21ae6c41ce1dfe7b6bfaceea5ab68e40a2476f50208e526f506080");
        user2 = new User("user2@hotmail.it", "User2", "UserSurname2", "4c94485e0c21ae6c41ce1dfe7b6bfaceea5ab68e40a2476f50208e526f506080");
        user4 = new User("user4@hotmail.it", "User4", "UserSurname4", "4c94485e0c21ae6c41ce1dfe7b6bfaceea5ab68e40a2476f50208e526f506080");
        List<User> participants = new ArrayList<>();
        participants.add(user2);
        event.setUserCollection(participants);
        List<Event> eventOrganized = new ArrayList<>();
        user.setEventOrganized(eventOrganized);
        event.setOwner(user);
        when(eventManager.find(50)).thenReturn(event);
    }
    
    @After
    public void tearDown() {
    }
    
    //L'evento deve essere salvato una sola volta nel database
    // e l'utente passato al metodo deve contenerlo fra i suoi eventi organizzati
    @Test
    public void createEventTest(){
        Event newEvent = new Event();
        eventManager.createEvent(newEvent, user);
        assertThat(newEvent.getOwner(), is(user));
        assertTrue(newEvent.getOwner().getEventOrganized().contains(newEvent));
        verify(eventManager.em,times(1)).persist(newEvent);
    }
    
    @Test
    public void updateWeatherinfoTest(){
        //Aggiungo una nuova previsione all'evento
        Event returnedEvent = eventManager.find(event.getIdevent());
        returnedEvent.setWeatherinfo("2015-02-25:700:snow:21.97:26.24");
        eventManager.updateWeatherInfo(returnedEvent);
        
        assertEquals("2015-02-25:700:snow:21.97:26.24", returnedEvent.getWeatherinfo());
        
        Event afterUpdateEvent = eventManager.find(event.getIdevent());
        
        assertEquals("2015-02-25:700:snow:21.97:26.24", afterUpdateEvent.getWeatherinfo());
        
        verify(eventManager.em,times(1)).merge(returnedEvent);
    }
    
    @Test
    public void updateTest(){
        eventManager.createEvent(event, user);
        
        Event resultEvent = eventManager.find(event.getIdevent());
        
        assertEquals(resultEvent.getOwner(),user);
        
        //caso peggiore cambio tutto e aggiungo un partecipante
        Event tempEvent = new Event();
        tempEvent.setIndoor(true);
        tempEvent.setDescription("testing update..");
        tempEvent.setName("UpdateTest");
        tempEvent.setStarttime(new Date(2015, 03, 25, 6, 15));
        tempEvent.setEndtime(new Date(2015, 03, 25, 8, 15));
        tempEvent.setPrivate1(true);
        tempEvent.setLat("42.4654220");
        tempEvent.setLon("22.4654220");
        tempEvent.setWeatherinfo("2015-03-25:500:light rain:21.97:26.24");
        tempEvent.setPlace("Roma");
        
        eventManager.update(resultEvent, tempEvent);
        
        assertTrue(resultEvent.getIndoor());
        assertEquals("testing update..", resultEvent.getDescription());
        assertEquals("UpdateTest", resultEvent.getName());
        assertTrue(resultEvent.getPrivate1());
        assertEquals("Roma", resultEvent.getPlace());
        assertEquals(new Date(2015, 03, 25, 6, 15), resultEvent.getStarttime());
        assertEquals(new Date(2015, 03, 25, 8, 15), resultEvent.getEndtime());
        assertEquals("42.4654220", resultEvent.getLat());
        assertEquals("22.4654220", resultEvent.getLon());
        assertEquals("2015-03-25:500:light rain:21.97:26.24", resultEvent.getWeatherinfo());
        assertEquals("Roma", resultEvent.getPlace());
        
        verify(eventManager.em,times(1)).merge(resultEvent);
    }
    
    @Test
    public void addParticipantTest(){
        User user3 = new User("user3@hotmail.it", "User3", "UserSurname3", "4c94485e0c21ae6c41ce1dfe7b6bfaceea5ab68e40a2476f50208e526f506080");
        List<Event> events = new ArrayList<>();
        user3.setEventCollection(events);
        eventManager.addParticipant(event, user3);
        
        assertTrue(event.getUserCollection().size()==2);
        assertTrue(event.getUserCollection().contains(user3));
        verify(eventManager.em,times(1)).merge(event);
        
        assertTrue(user3.getEventCollection().size()==1);
        assertTrue(user3.getEventCollection().contains(event));
        verify(eventManager.em,times(1)).merge(event);
    }
    
    @Test 
    public void removeParticipantTest(){
        List<Event> events = new ArrayList<>();
        events.add(event);
        Event newEvent = new Event(51, false, false, new Date(2015, 02, 25, 5, 15), new Date(2015, 03, 26, 6, 00), "test", "2015-02-25:500:light rain:21.97:26.24", "Milano");
        events.add(newEvent);
        user2.setEventCollection(events);
        User newUser = new User("user4@hotmail.it", "User4", "UserSurname4", "4c94485e0c21ae6c41ce1dfe7b6bfaceea5ab68e40a2476f50208e526f506080");
        List<Event> events2 = new ArrayList<>();
        newUser.setEventCollection(events2);
        eventManager.addParticipant(event, newUser);
        
        eventManager.removeParticipant(event, user2);
        
        assertTrue(event.getUserCollection().size()==1);
        assertFalse(event.getUserCollection().contains(user2));
        assertTrue(event.getUserCollection().contains(newUser));
        verify(eventManager.em,times(2)).merge(event);
        
        assertTrue(user2.getEventCollection().size()==1);
        assertFalse(user2.getEventCollection().contains(event));
        assertTrue(user2.getEventCollection().contains(newEvent));
        verify(eventManager.em,times(1)).merge(user2);
    }
    
    @Test
    public void deleteEventTest(){
        
        eventManager.deleteEvent(event);
        verify(eventManager.em,times(1)).remove(event);
    }
    
    @Test
    public void clearParticipantsListTest(){
        User user3 = new User("user3@hotmail.it", "User3", "UserSurname3", "4c94485e0c21ae6c41ce1dfe7b6bfaceea5ab68e40a2476f50208e526f506080");
        List<Event> events2 = new ArrayList<>();
        user3.setEventCollection(events2);
        
        eventManager.addParticipant(event, user3);
        
        List<Event> events = new ArrayList<>();
        events.add(event);
        user2.setEventCollection(events);
        
        List<User> participants = new ArrayList<>();
        participants.add(user2);
        participants.add(user3);
        
        eventManager.clearParticipantsList(event, participants);
        
        assertTrue(event.getUserCollection().isEmpty());
        verify(eventManager.em,times(2)).merge(event);
        
        assertTrue(user2.getEventCollection().isEmpty());
        assertTrue(user3.getEventCollection().isEmpty());
        verify(eventManager.em,times(1)).merge(user2);
        verify(eventManager.em,times(2)).merge(user3);
        
    }
    
}