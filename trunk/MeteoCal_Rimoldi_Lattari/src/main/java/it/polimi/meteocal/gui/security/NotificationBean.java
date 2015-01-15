/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.gui.security;

import it.polimi.meteocal.business.security.boundary.NotificationManager;
import it.polimi.meteocal.entities.Notification;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Francesco
 */
@ManagedBean
@Named(value = "nb")
@RequestScoped
public class NotificationBean {

    /**
     * Creates a new instance of NotificationBean
     */
    public NotificationBean() {
    }
    
    @EJB
    private NotificationManager nm;
    
    private Notification notification;

    public Notification getNotification() {
        if (notification == null) {
            notification = new Notification();
        }
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }
}
