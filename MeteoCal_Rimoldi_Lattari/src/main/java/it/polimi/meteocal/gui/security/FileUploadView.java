/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.gui.security;

import it.polimi.meteocal.business.security.boundary.UserManager;
import java.io.IOException;
import java.io.InputStream;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Francesco
 */
@Named(value = "fileUploadView")
@RequestScoped
public class FileUploadView {

    @EJB
    private UserManager um;
    
    private UploadedFile file;
    
    private StreamedContent fileToDisplay;
    
    private InputStream input;

    /**
     * Get the value of fileToDisplay
     *
     * @return the value of fileToDisplay
     */
    public StreamedContent getFileToDisplay() throws IOException {
        if(file != null) {
           fileToDisplay = new DefaultStreamedContent(file.getInputstream(), "image/jpg");
           return fileToDisplay;
        }
        else {
            return null;
        }
    }

    /**
     * Set the value of fileToDisplay
     *
     * @param fileToDisplay new value of fileToDisplay
     */
    public void setFileToDisplay(StreamedContent fileToDisplay) {
        this.fileToDisplay = fileToDisplay;
    }


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

     
    public void upload() throws IOException {
        if(file != null) {
            FacesMessage message = new FacesMessage("Succesful", file.getFileName().toString() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            input = file.getInputstream();
            fileToDisplay = new DefaultStreamedContent(input , "image/png");
       //     um.generateByteArray(file.getFileName().toString());
        }
    }
    
}
