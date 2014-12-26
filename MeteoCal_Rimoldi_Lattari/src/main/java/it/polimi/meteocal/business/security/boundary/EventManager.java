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

    public void createEvent(Event event, String email) {
        User u = em.find(User.class, email);
        event.setOwner(u);
        List<Event> eventOrganized = u.getEventOrganized();
        eventOrganized.add(event);
        u.setEventOrganized(eventOrganized);
        em.persist(event);
    }
}
