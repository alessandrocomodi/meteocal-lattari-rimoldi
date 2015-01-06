/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.gui.security;

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
    
}
