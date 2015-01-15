/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.gui.security;

import it.polimi.meteocal.business.security.boundary.EventManager;
import it.polimi.meteocal.business.security.boundary.NotificationManager;
import it.polimi.meteocal.business.security.boundary.UserManager;
import it.polimi.meteocal.business.security.entity.User;
import it.polimi.meteocal.entities.Event;
import it.polimi.meteocal.entities.Notification;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Named;

/**
 *
 * @author Alessandro
 */
@ManagedBean
@RequestScoped
@Named("eb")
public class EventBean {

    @EJB
    EventManager em;
    
    @EJB
    private NotificationManager nm;
    
    @EJB
    private UserManager um;
    
    private Event event;

    private List<User> guests;

    public List<User> getGuests() {
        return guests;
    }

    public void setGuests(List<User> guests) {
        this.guests = guests;
    }

    
    
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
    
    public String createEvent(User user, List<User> invitedUsers) {
        Integer id = 0;
        em.createEvent(event, user);
        List<Event> result = em.getEventOrganized(user.getEmail());
        for (Event e : result) {
            if (e.getIdevent() > id) {
                id = e.getIdevent();
            }
        }
        nm.createInvitation(invitedUsers, id);
        Integer id2 = 0;
        List<Notification> result2 = nm.getLastInvitation(id);
        for (Notification n : result2) {
            if (n.getIdnotification() > id2 && n.getType().equals("INVITATION")) {
            id2 = n.getIdnotification();
            }
        }
        for (User u : invitedUsers) {
            u.getNotificationCollection().add(nm.find(id2));
            um.updateUserNotificationList(u);
        }
        return "calendar_page?feces-redirect=true";
    }
    
    public void addGuest(User user) {
        this.guests.add(user);
    }
}
