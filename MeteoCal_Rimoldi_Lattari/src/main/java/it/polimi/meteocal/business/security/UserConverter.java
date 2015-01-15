/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.business.security;

import it.polimi.meteocal.business.security.boundary.UserManager;
import it.polimi.meteocal.business.security.entity.User;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Francesco
 */
@FacesConverter(value="userConverter")
public class UserConverter implements Converter{

    @EJB
    private UserManager um;
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if(value.trim().equals("")){
            return null;
        } else {
            try {
                List<User> allUsers = um.findAllUsers();
                for(User u : allUsers){
                    if(u.getEmail().equals(value)){
                        return u;
                    }
                }
            } catch(NumberFormatException exception) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid user"));
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if(value == null || value.equals("")){
            return "";
        } else {
            return String.valueOf(((User)value).getEmail());
        }
    }
    
}
