/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.business.security.boundary;

import it.polimi.meteocal.business.security.entity.User;
import it.polimi.meteocal.entities.Event;
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
public class EventManager {

    @PersistenceContext
    private EntityManager em;

    public void createEvent(Event event, User user) {
        event.setOwner(user);
        List<Event> eventOrganized = user.getEventOrganized();
        eventOrganized.add(event);
        user.setEventOrganized(eventOrganized);
        em.persist(event);
    }
    
    public void updateWeatherInfo(Event event) {
        em.merge(event);
    }
    
    public void update(Event selectedEvent, Event event){
        selectedEvent.setName(event.getName());
        selectedEvent.setIndoor(event.getIndoor());
        selectedEvent.setDescription(event.getDescription());
        selectedEvent.setPlace(event.getPlace());
        selectedEvent.setPrivate1(event.getPrivate1());
        selectedEvent.setStarttime(event.getStarttime());
        selectedEvent.setEndtime(event.getEndtime());
        selectedEvent.setWeatherinfo(event.getWeatherinfo());
        em.merge(selectedEvent);
              
    }
    
    public Event find(Object id) {
        return em.find(Event.class, id);
    }
    
    public List<Event> findAllOutdoorEvents() {
        Query query = em.createNamedQuery("Event.findByIndoor");
        query.setParameter("indoor", false);
        List<Event> results = new ArrayList<>();
        results.addAll(query.getResultList());
        return results;
    }
    
    public List<Event> getEventOrganized(String email) {
        Query query1= em.createNamedQuery("Event.findByOrganizer");
        query1.setParameter("owner", email);
        List<Event> results = new ArrayList<>();
        results.addAll(query1.getResultList());
        return results;
    }
    
    public List<Event> getJoinedEvent(User u) {
        Query events = em.createNamedQuery("Event.findAll");
        List<Event> allEvents = new ArrayList<>();
        allEvents.addAll(events.getResultList());
        List<Event> results = new ArrayList<>();
        for (Event e : allEvents) {
            if (e.getUserCollection().contains(u)) {
                results.add(e);
            }
        }
        return results;
    }

    public void addParticipant(Event e, User u) {
        e.getUserCollection().add(u);
        em.merge(e);
        u.getEventCollection().add(e);
        em.merge(u);
    }
    
    public void removeParticipant(Event e, User u) {
        e.getUserCollection().remove(u);
        em.merge(e);
        u.getEventCollection().remove(e);
        em.merge(u);
    }

    public void deleteEvent(Event eventToBeDeleted) {
        Event e = find(eventToBeDeleted.getIdevent());
        if (e!=null) {
            em.remove(e);
        }
    }

    public void clearParticipantsList(Event event, List<User> participants) {
        List<User> participants2 = new ArrayList();
        event.setUserCollection(participants2);
        em.merge(event);
        for (User u : participants) {
            u.getEventCollection().remove(event);
            em.merge(u);
        }
    }
}
