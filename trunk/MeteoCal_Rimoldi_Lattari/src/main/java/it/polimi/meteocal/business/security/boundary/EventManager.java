/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.business.security.boundary;

import it.polimi.meteocal.business.security.entity.User;
import it.polimi.meteocal.entities.Event;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
    
    public Event find(Object id) {
        return em.find(Event.class, id);
    }
}
