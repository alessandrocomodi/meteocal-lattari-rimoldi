/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.gui.security;

import it.polimi.meteocal.business.security.boundary.SearchManager;
import it.polimi.meteocal.business.security.entity.User;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Named;

/**
 *
 * @author Alessandro
 */
@ManagedBean
@ApplicationScoped
@Named("sb")
public class SearchBean {
    
    @EJB
    private SearchManager sm;
    
    private String parameter;

    public SearchBean() {
    }
    
    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
    
    public String getParameter() {
        return parameter;
    }

    

    public List<User> searchUsers() {
        return sm.searchUsers(parameter);
    }
}
