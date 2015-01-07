/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.gui.security;

import it.polimi.meteocal.business.security.boundary.UserManager;
import it.polimi.meteocal.business.security.entity.User;
import it.polimi.meteocal.entities.Event;
import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Alessandro
 */
@RequestScoped
@Named("ub")
public class UserBean{

    @EJB
    private UserManager um;
    
    private User newUser;
    
    private byte[] img;
    
    public UserBean() {
    }
    
    public StreamedContent getUserAvatar() {
/*
        byte[] buffer;
        FacesContext fc = FacesContext.getCurrentInstance();
        if (fc.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            return new DefaultStreamedContent();
        } else {
            buffer = getCurrentUser().getAvatar();
            InputStream input = new ByteArrayInputStream(buffer);
            StreamedContent image = new DefaultStreamedContent(input, "image/png");
            return image;
        } */
          InputStream is = new ByteArrayInputStream(getCurrentUser().getAvatar());
          StreamedContent image = new DefaultStreamedContent(is, "image/png");
          return image;       
         
    }
    
    public String getPhoneNumber() {
        if (getCurrentUser().getPhone() == null) {
            return "not available";
        }
        return getCurrentUser().getPhone();
    }
    
    public String getCalendarPrivacy() {
        if (getCurrentUser().getPrivacy()) {
            return "private";
        }
        return "public";
    }
    
    public String getName() {
        return um.getLoggedUser().getName();
    }
    
    public String getNewUserName() {
        return newUser.getName();
    }
    
    public String getNewUserSurname() {
        return newUser.getSurname();
    }
    
    public String getNewUserEmail() {
        return newUser.getEmail();
    }
    
    public String getNewUserPhone() {
        return newUser.getPhone();
    }
    
    
 
    public void setNewUser(User newUser) {
        this.newUser = newUser;
    }

    public User getNewUser() {
        if (newUser==null) {
            newUser = new User();
        }
        return newUser;
    }
     
    public void create() throws IOException {
         um.save(newUser);
    }
    
    public User getCurrentUser(){
        return um.getLoggedUser();
    }
    
    //al momento ritorna solo gli eventi organizzati, poi basterà aggiungere anche gli eventi a cui si partecipa
    public List<Event> getOwnEvents() {
        return getCurrentUser().getEventOrganized();
    }
    
}
