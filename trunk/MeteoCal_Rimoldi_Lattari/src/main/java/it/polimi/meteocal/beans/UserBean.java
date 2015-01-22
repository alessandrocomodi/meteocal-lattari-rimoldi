/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.beans;

import it.polimi.meteocal.business.manager.UserManager;
import it.polimi.meteocal.entities.User;
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
    
    private String userEmail;

    private StreamedContent genericUserAvatar;

    public StreamedContent getGenericUserAvatar() {
        return this.genericUserAvatar;
    }
    
    //eventualmente da rivedere
    public StreamedContent retriveGenericUserAvatar(String email) {
        if (email == null) {
            return genericUserAvatar = new DefaultStreamedContent();
        }
        do {
            User u = getUserFromEmail(email);
            InputStream is = new ByteArrayInputStream(u.getAvatar());
            genericUserAvatar = new DefaultStreamedContent(is, "image/png");
        } while ( genericUserAvatar == null );
        return genericUserAvatar; 
    }

    public void setGenericUserAvatar(StreamedContent genericUserAvatar) {
        this.genericUserAvatar = genericUserAvatar;
    }

    
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
    
    //metodo ad hoc per la pagina profilo di un utente generico...in realtà si potrebbe unire a quello già esistente
    //per il momento lasciare così
    public String getPhoneFromUser(String email) {
        if (getUserFromEmail(email).getPhone() == null) {
            return "not available";
        }
        return getUserFromEmail(email).getPhone();
    }
    
    //metodo ad hoc per la pagina profilo di un utente generico...in realtà si potrebbe unire a quello già esistente
    //per il momento lasciare così
    public String getCalendarPrivacyFromUser(String email) {
        if (getUserFromEmail(email).getPrivacy()) {
            return "private";
        }
        return "public";
    }
    
    //metodo per valutare se renderizzare o meno il bottone che linka al calendario dell'utente, a seconda della sua privacy
    public boolean visualizeCalendarButton(String email) {
        boolean b = getUserFromEmail(email).getPrivacy();
        //not b perchè renderizzo (quindi TRUE) il bottone se la privacy è pubblica, ovvero privacy = FALSE
        return !b;
    }
    
    
    public String getCalendarPrivacy() {
        if (getCurrentUser().getPrivacy()) {
            return "private";
        }
        return "public";
    }
    
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
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
        InputStream iStream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/images/icon-user-default.png");
        um.save(newUser, iStream);
    }
    
    public User getCurrentUser(){
        return um.getLoggedUser();
    }
    
    public User getUserFromEmail(String email) {
        return um.getUserFromEmail(email);
    }
    
    //al momento ritorna solo gli eventi organizzati, poi basterà aggiungere anche gli eventi a cui si partecipa
    public List<Event> getOwnEvents() {
        return getCurrentUser().getEventOrganized();
    }
    
}
