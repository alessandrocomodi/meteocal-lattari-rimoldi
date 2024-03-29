/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.beans;

import it.polimi.meteocal.business.manager.UserManager;
import it.polimi.meteocal.entities.User;
import it.polimi.meteocal.entities.Event;
import it.polimi.meteocal.entities.Notification;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Alessandro
 */
@Named(value = "notificationView")
@SessionScoped
public class NotificationView implements Serializable{
    
    @EJB
    private UserManager um;
    
    private List<Notification> myNotifications;

    public List<Notification> getMyNotifications() {
        List<Notification> revertedNotifications;
        revertedNotifications = myNotifications;
        Collections.reverse(revertedNotifications);
        return revertedNotifications;
    }

    public void setMyNotifications(List<Notification> myNotifications) {
        this.myNotifications = myNotifications;
    }
    
    private Notification selectedNotification;

    public Notification getSelectedNotification() {
        return selectedNotification;
    }

    public void setSelectedNotification(Notification selectedNotification) {
        this.selectedNotification = selectedNotification;
    }
    
    public void updateView(){
        this.myNotifications = getOwnNotifications();
    }
    
    /**
     * Creates a new instance of NotificationView
     */
    public NotificationView() {
    }

    private List<Notification> getOwnNotifications() {
        return getCurrentUser().getNotificationCollection();
    }
    
    private User getCurrentUser() {
        return um.getLoggedUser();
    }
    
    public StreamedContent getUserAvatar() {
        if(selectedNotification != null){
          InputStream is = new ByteArrayInputStream(selectedNotification.getEvent().getOwner().getAvatar());
          StreamedContent image = new DefaultStreamedContent(is, "image/png");
          return image; 
        } else {
          InputStream is = new ByteArrayInputStream(getCurrentUser().getAvatar());
          StreamedContent image = new DefaultStreamedContent(is, "image/png");
          return image; 
        }
    }
    
    public String resultUrl(){
        if(selectedNotification != null){
            return "user.xhtml?faces-redirect=true&user=" + this.selectedNotification.getEvent().getOwner().getEmail();
        } else {
            return "user.xhtml?faces-redirect=true&user=g";
        }
    }
    
    public boolean showEventDeletedDetails() {
        return selectedNotification.getEvent() != null;
    }
    
    public String retrieveCorrectUrl(Event event3) {
        if (event3 == null || event3.getWeatherinfo().equals("No weather conditions available")) {
            return "./../images/Lol_question_mark.png";
        }
        return "http://openweathermap.org/img/w/" + event3.retriveWeatherIconNumber();
    }
}
