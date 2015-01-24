/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.beans;

import it.polimi.meteocal.business.manager.EventManager;
import it.polimi.meteocal.business.manager.UserManager;
import it.polimi.meteocal.entities.User;
import it.polimi.meteocal.entities.Event;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.primefaces.model.StreamedContent;

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
   
    public Event getSelectedEvent() {
        return selectedEvent;
    }

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
        List<Event> myEvents2 = em.getEventOrganized(email);
        List<Event> joinedEvents = em.getJoinedEvent(um.getUserFromEmail(email));
        myEvents2.addAll(joinedEvents);
        if(eventModel.getEventCount() != 0){
            eventModel.clear();
        }
        for(Event e : myEvents2){
            String name;
            name = e.getName();
            if (e.getOwner().getEmail().equals(um.getLoggedUser().getEmail())) {
                name = e.getName();
            }
            System.out.println(e.getStarttime().toString());
            eventModel.addEvent(new DefaultScheduleEvent(name, getDate(e.getStarttime()), getDate(e.getEndtime()), e.getIdevent()));
        }
        
    }
    
    public void updateViewGenericUserCalendar(String email) {
        List<Event> myEvents2 = em.getEventOrganized(email);
        List<Event> joinedEvents = em.getJoinedEvent(um.getUserFromEmail(email));
        myEvents2.addAll(joinedEvents);
        if(eventModel.getEventCount() != 0){
            eventModel.clear();
        }
        for(Event e : myEvents2){
            String name;
            if (e.getPrivate1()  &&  (!e.getOwner().getEmail().equals(um.getLoggedUser().getEmail()) && !this.checkParticipantsEmail(e, um.getLoggedUser().getEmail()))) {
                name = "busy";
            } else {
                name = e.getName();
            }
            if (e.getOwner().getEmail().equals(um.getLoggedUser().getEmail())) {
                name = e.getName();
            }
            System.out.println(e.getStarttime().toString());
            eventModel.addEvent(new DefaultScheduleEvent(name, getDate(e.getStarttime()), getDate(e.getEndtime()), e.getIdevent()));
        }
    }

    public void resetView(){
        if(eventModel.getEventCount()!=0){
            eventModel.clear();
        }
    }
    
    public String visualizeCorrectHeader() {
        if (selectedEvent.getPrivate1()) {
            return "No available information for private events";
        } else {
            return "Event details";
        }
    }
    
    public boolean visualizeEventDetails() {
        return !selectedEvent.getPrivate1();
    }
    
    public Date getDate(Date eventTime) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.setTime(eventTime);
        
        System.out.println(eventTime);
        
        System.out.println(cal.getTime());
        
        return cal.getTime();
    }
    
    private boolean checkParticipantsEmail(Event event, String email) {
        for (User u : event.getUserCollection()) {
            if (u.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }
    
    public void onEventSelect(SelectEvent selectEvent) {
        event = (ScheduleEvent) selectEvent.getObject();
        System.out.println(event.getData().getClass().toString());
        this.selectedEvent = em.find(event.getData());
        if (selectedEvent.getPrivate1() &&  (!selectedEvent.getOwner().getEmail().equals(um.getLoggedUser().getEmail()) && !this.checkParticipantsEmail(selectedEvent, um.getLoggedUser().getEmail()))) {
            selectedEvent.setName("---");
            selectedEvent.setDescription("---");
            selectedEvent.setPlace("---");
            selectedEvent.getIndoorString();
        }
    }
    
    public ScheduleModel getEventModel() {
        return eventModel;
    }

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
    
    public String resultUserUrl(){
        if(selectedEvent != null){
            if(selectedEvent.getOwner().getEmail().equals(getCurrentUser().getEmail())){
                return "user_home3.xhtml";
            } else {
                return "user.xhtml?faces-redirect=true&user=" + this.selectedEvent.getOwner().getEmail();
            }
        } else {
            return "user.xhtml?faces-redirect=true&user=";
        }
        
    }
    
    public StreamedContent getUserAvatar(){
        if(selectedEvent != null){
          InputStream is = new ByteArrayInputStream(selectedEvent.getOwner().getAvatar());
          StreamedContent image = new DefaultStreamedContent(is, "image/png");
          return image; 
        } else {
          InputStream is = new ByteArrayInputStream(getCurrentUser().getAvatar());
          StreamedContent image = new DefaultStreamedContent(is, "image/png");
          return image; 
        }
    }
    
    /**
     * Creates a new instance of ScheduleView
     */
    public ScheduleView() {
    }
    
    public User getCurrentUser(){
        return um.getLoggedUser();
    }
    
    //al momento ritorna solo gli eventi organizzati, poi basterÃƒÂ  aggiungere anche gli eventi a cui si partecipa
    public List<Event> getOwnEvents() {
        return getCurrentUser().getEventOrganized();
    }
    
    public boolean isInTheParticipantsList(User u, Event e) {
        if (e == null) {
            return true;
        } else if (e.getUserCollection().contains(u)) {
            return false;
        }
        return true;
    }
    
    public boolean isTheOwner(User u, Event event) {
        if (event == null) {
            return true;
        }
        return event.getOwner().getEmail().equals(u.getEmail());
    }
    
    public String retrieveCorrectUrl(Event event3) {
        if (event3 == null || event3.getWeatherinfo().equals("No weather conditions available")) {
            return "./../images/Lol_question_mark.png";
        }
        return "http://openweathermap.org/img/w/" + event3.retriveWeatherIconNumber();
    }
    
    public String retrieveCorrectOutcome(String email){
        if(getCurrentUser().getEmail().equals(email)){
            return "user_home3?faces-redirect-true";
        } else { 
            return "user";
        }
    }
    
}
