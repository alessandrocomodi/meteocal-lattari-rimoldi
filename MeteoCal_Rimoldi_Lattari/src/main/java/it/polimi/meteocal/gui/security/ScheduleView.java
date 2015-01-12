/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.gui.security;

import it.polimi.meteocal.business.security.boundary.EventManager;
import it.polimi.meteocal.business.security.boundary.UserManager;
import it.polimi.meteocal.business.security.entity.User;
import it.polimi.meteocal.entities.Event;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author Francesco
 */
@Named(value = "scheduleView")
@SessionScoped
public class ScheduleView implements Serializable{
    
    @EJB
    private UserManager um;
    
    @EJB
    private EventManager em;
    
    private ScheduleModel eventModel;

    private ScheduleEvent event = new DefaultScheduleEvent();
    
    private Event selectedEvent;

    /**
     * Get the value of selectedEvent
     *
     * @return the value of selectedEvent
     */
    public Event getSelectedEvent() {
        return selectedEvent;
    }

    /**
     * Set the value of selectedEvent
     *
     * @param selectedEvent new value of selectedEvent
     */
    public void setSelectedEvent(Event selectedEvent) {
        this.selectedEvent = selectedEvent;
    }


    
    public ScheduleEvent getEvent() {
        return event;
    }

    
    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }
 
     
    @PostConstruct
    public void init() {
        eventModel = new DefaultScheduleModel();
    }
    
    public void updateView(String email){
        List<Event> myEvents = em.getEventOrganized(email);
//        List<Event> myEvents = getOwnEvents();
        if(eventModel.getEventCount() != 0){
            eventModel.clear();
        }
        for(Event e : myEvents){
            System.out.println(e.getStarttime().toString());
            eventModel.addEvent(new DefaultScheduleEvent(e.getName(), getDate(e.getStarttime()), getDate(e.getEndtime()), e.getIdevent()));
        }
    }

    public void resetView(){
        if(eventModel.getEventCount()!=0){
            eventModel.clear();
        }
    }
    
    public Date getDate(Date eventTime) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.setTime(eventTime);
        
        System.out.println(eventTime);
        
        System.out.println(cal.getTime());
        
        return cal.getTime();
    }
    
    public void onEventSelect(SelectEvent selectEvent) {
        event = (ScheduleEvent) selectEvent.getObject();
        System.out.println(event.getData().getClass().toString());
        this.selectedEvent = em.find(event.getData());
        
    }
    
    /**
     * 
     * Get the value of eventModel
     *
     * @return the value of eventModel
     */
    public ScheduleModel getEventModel() {
        return eventModel;
    }

    /**
     * Set the value of eventModel
     *
     * @param eventModel new value of eventModel
     */
    public void setEventModel(ScheduleModel eventModel) {
        this.eventModel = eventModel;
    }

    public boolean visualizeModifyButton() {
        if (this.selectedEvent != null) {
            return (this.selectedEvent.getOwner().getEmail().equals(getCurrentUser().getEmail()));
        } else {
            return true;
        }
    } 
    
    public String resultUrl(){
        return "modify_event?faces-redirect=true&par=" + this.selectedEvent.getIdevent();
    }
    
    /**
     * Creates a new instance of ScheduleView
     */
    public ScheduleView() {
    }
    
    public User getCurrentUser(){
        return um.getLoggedUser();
    }
    
    //al momento ritorna solo gli eventi organizzati, poi basterÃ  aggiungere anche gli eventi a cui si partecipa
    public List<Event> getOwnEvents() {
        return getCurrentUser().getEventOrganized();
    }
    
    
    
}
