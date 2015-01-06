/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.gui.security;

import com.fasterxml.jackson.databind.util.ISO8601Utils;
import it.polimi.meteocal.business.security.boundary.UserManager;
import it.polimi.meteocal.business.security.entity.User;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import sun.misc.IOUtils;

/**
 *
 * @author Alessandro
 */
@ManagedBean
@RequestScoped
@Named
public class ModificationBean {

    @EJB
    private UserManager um;
    
    private User user;
    
    private UploadedFile file;

    /**
     * Get the value of file
     *
     * @return the value of file
     */
    public UploadedFile getFile() {
        return file;
    }

    /**
     * Set the value of file
     *
     * @param file new value of file
     */
    public void setFile(UploadedFile file) {
        this.file = file;
    }


    public ModificationBean() {
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
    
    public String update() throws IOException {
        um.update(this.getCurrentUser(),user,file);
        return "user_home3?faces-redirect=true";
    }
    
    public User getCurrentUser(){
        return um.getLoggedUser();
    }
}
