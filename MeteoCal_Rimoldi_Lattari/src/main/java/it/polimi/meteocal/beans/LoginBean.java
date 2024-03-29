/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.beans;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author miglie
 */
@ManagedBean
@Named()
@RequestScoped
public class LoginBean {
    
    @Inject
    private Logger logger;
    

    private String username;
    private String password;

    public LoginBean() {
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            request.login(this.username, this.password);
            return "/user/user_home3?faces-redirect=true";
        } catch (ServletException e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Login Failed","Login Failed"));
            logger.log(Level.SEVERE,"Login Failed");
            return null;
        }
    }
    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        request.getSession().invalidate();
        logger.log(Level.INFO, "User Logged out");
        return "/home?faces-redirect=true";
    }
    
    //metodo che verifica se un un utente è già loggato o meno, così da mostrare on opportuno messaggio in home.xhtml
    public boolean isLogged() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        return request.getUserPrincipal() == null;
    }
}
