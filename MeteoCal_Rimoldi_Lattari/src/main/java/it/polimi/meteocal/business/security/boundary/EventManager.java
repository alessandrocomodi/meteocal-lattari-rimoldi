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
    
    public List<Event> getEventOrganized(String email) {
        Query query1= em.createNamedQuery("Event.findByOrganizer");
        query1.setParameter("owner", email);
        List<Event> results = new ArrayList<>();
        results.addAll(query1.getResultList());
        return results;
    }
}
