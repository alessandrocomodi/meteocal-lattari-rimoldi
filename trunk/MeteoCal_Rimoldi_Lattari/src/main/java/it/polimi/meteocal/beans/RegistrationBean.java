/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.beans;

import it.polimi.meteocal.business.manager.UserManager;
import it.polimi.meteocal.entities.User;
import java.io.IOException;
import java.io.InputStream;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

/**
 *
 * @author Alessandro
 */
@ManagedBean
@Named
@RequestScoped
public class RegistrationBean {

    @EJB
    private UserManager um;

    private User user;

    public RegistrationBean() {
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

    public String register() throws IOException {
        InputStream iStream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/images/icon-user-default.png");
        User u = um.getUserFromEmail(user.getEmail());
        if(u==null){
            um.save(user, iStream);
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Address already in use", null));
            return "registration?faces-redirect-true";
        }
        return "home?faces-redirect=true";
    }

}
