/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.gui.security;

import it.polimi.meteocal.business.security.boundary.UserManager;
import it.polimi.meteocal.business.security.entity.User;
import it.polimi.meteocal.entities.Notification;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Alessandro
 */
@Named(value = "notificationView")
@ViewScoped
public class NotificationView implements Serializable{
    
    @EJB
    private UserManager um;
    
    private List<Notification> myNotifications;

    public List<Notification> getMyNotifications() {
        return myNotifications;
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
    
    @PostConstruct
    public void init() {
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
    
}
