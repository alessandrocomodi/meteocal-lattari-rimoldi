/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.gui.security;

import it.polimi.meteocal.business.security.boundary.UserManager;
import it.polimi.meteocal.business.security.entity.User;
import it.polimi.meteocal.entities.Event;
import it.polimi.meteocal.entities.Notification;
import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.DefaultScheduleEvent;

/**
 *
 * @author Francesco
 */
@Named(value = "notificationsView")
@SessionScoped
public class NotificationsView {

    @EJB
    private UserManager um;
    
    private List<Notification> myNotifications;
    
    private Notification selectedNotification;

    
    @PostConstruct
    public void init() {
        this.myNotifications = getOwnNotifications();
    }
    
    public void setSelectedNotification(Notification selectedNotification) {
        this.selectedNotification = selectedNotification;
    }
    
    public Notification getMySelectedNotificatio() {
        return selectedNotification;
    }

    
    public Notification getSelectedNotification() {
        return selectedNotification;
    }

    
    public List<Notification> getMyNotifications() {
        return myNotifications;
    }

    
    public void setMyNotifications(List<Notification> myNotifications) {
        this.myNotifications = myNotifications;
    }
    
    
    /**
     * Creates a new instance of NotificationsView
     */
    public NotificationsView() {
    }
    
    public User getCurrentUser(){
        return um.getLoggedUser();
    }
    
    //ritorna le notifiche che sono indirizzate a me
    public List<Notification> getOwnNotifications() {
        return getCurrentUser().getNotificationCollection();
    }
    
}
