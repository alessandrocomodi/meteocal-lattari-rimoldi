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
import javax.inject.Named;

/**
 *
 * @author Alessandro
 */
@RequestScoped
@Named("sb")
public class SearchBean {
    
    @EJB
    private SearchManager sm;
    
    private String parameter;

    public SearchBean() {
    }

    public String getParamter() {
        return parameter;
    }

    public void setParamter(String paramter) {
        this.parameter = paramter;
    }

    public ArrayList<User> searchUsers() {
        return sm.searchUsers(parameter);
    }
}
