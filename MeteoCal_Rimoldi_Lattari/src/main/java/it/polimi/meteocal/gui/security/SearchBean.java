/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.gui.security;

import it.polimi.meteocal.business.security.boundary.SearchManager;
import it.polimi.meteocal.business.security.entity.User;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Francesco
 */
@ManagedBean
@Named(value = "searchBean")
@RequestScoped
public class SearchBean {

    @EJB
    private SearchManager sm;
    
        private String parameter;

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    private User selectedUser;

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    private String par;

    public String getPar() {
        return par;
    }

    public void setPar(String par) {
        this.par = par;
    }

    
    /**
     * Creates a new instance of SearchBean
     */
    public SearchBean() {
    }
    
    public String resultUrl() {
        return "search_results2?faces-redirect=true&par=" + parameter;
    }

    public List<User> searchUsers(String parameter2) {
        return sm.searchUsers(parameter2);
    }
    
    public boolean noResult(String parameter2) {
        return sm.searchUsers(parameter2).isEmpty();
    }
    
}
