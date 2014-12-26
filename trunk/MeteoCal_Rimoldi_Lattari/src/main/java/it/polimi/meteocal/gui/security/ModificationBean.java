/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.gui.security;

import it.polimi.meteocal.business.security.boundary.UserManager;
import it.polimi.meteocal.business.security.entity.User;
import java.io.IOException;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Alessandro
 */
@ManagedBean(name="mb")
@RequestScoped
public class ModificationBean {

    @EJB
    private UserManager um;
    
    private User user;

    public ModificationBean() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public String update() throws IOException {
        um.update(this.getCurrentUser());
        return "user_home?faces-redirect=true";
    }
    
    public User getCurrentUser(){
        return um.getLoggedUser();
    }

    
}
