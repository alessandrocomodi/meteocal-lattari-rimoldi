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
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Alessandro
 */
@ManagedBean
@SessionScoped
@Named
public class ModificationBean implements Serializable{

    @EJB
    private UserManager um;

    @EJB
    private EventManager em;
    
    @EJB 
    private NotificationManager nm;
    
    private User user;
    
    private Event event;

    private String parameter;

    public void eventSet(){
        this.event = em.find(Integer.parseInt(parameter));
    }
    
    /**
     * Get the value of parameter
     *
     * @return the value of parameter
     */
    public String getParameter() {
        return parameter;
    }

    /**
     * Set the value of parameter
     *
     * @param parameter new value of parameter
     */
    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    
    /**
     * Get the value of event
     *
     * @return the value of event
     */
    public Event getEvent() {
        return event;
    }

    /**
     * Set the value of event
     *
     * @param event new value of event
     */
    public void setEvent(Event event) {
        this.event = event;
    }

    
    private UploadedFile file;
    
    @PostConstruct
    public void init() {
        this.user = this.getCurrentUser();
    }
    
    public ModificationBean() {
    }
 
    public UploadedFile getFile() {
        return file;
    }
 
    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public User getUser() {
        if (user == null) {
            user = new User();
        }
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public String update() throws IOException {
        um.update(this.getCurrentUser(),user,file);
        return "user_home3?faces-redirect=true";
    }
    
    public String updateEvent(List<User> invitedUsers){
        em.update(em.find(Integer.parseInt(parameter)),this.event);
        if(invitedUsers != null){
            boolean duplicate = false;
            for(User u : invitedUsers){
                for(Notification n : u.getNotificationCollection()){
                    if(n.getEvent().getIdevent().equals(this.event.getIdevent())){
                        FacesContext context = FacesContext.getCurrentInstance();
                        FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "User: " + u.getEmail() + " already invited!");
                        context.addMessage(":eventForm:boh", fm);
                        duplicate = true;
                    }
                }
            }
            if(duplicate == true) {
                return "" ;
            }
            nm.createInvitation(invitedUsers, this.event.getIdevent());
            Integer id2 = 0;
            List<Notification> result2 = nm.getLastInvitation(this.event.getIdevent());
            for (Notification n : result2) {
                if (n.getIdnotification() > id2 && n.getType().equals("INVITATION")) {
                id2 = n.getIdnotification();
                }
            }
            for (User u : invitedUsers) {
                u.getNotificationCollection().add(nm.find(id2));
                um.updateUserNotificationList(u);
            }
        }
        return "calendar_page?faces-redirect=true";
    }
    
      public void upload() {
        if(file != null) {
            FacesMessage message = new FacesMessage("Succesful", file.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
    
    public User getCurrentUser(){
        return um.getLoggedUser();
    }
}