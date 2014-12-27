/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.gui.security;

import it.polimi.meteocal.business.security.boundary.EventManager;
import it.polimi.meteocal.business.security.entity.User;
import it.polimi.meteocal.entities.Event;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author Alessandro
 */
@RequestScoped
@Named("eb")
public class EventBean {

    @EJB
    EventManager em;
    
    private Event event;

    public EventBean() {
    }

    public Event getEvent() {
        if (event == null) {
            event = new Event();
        }
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
    
    public String createEvent(User user) {
        em.createEvent(event, user);
        return "user_home";
    }
    
    
    
}
