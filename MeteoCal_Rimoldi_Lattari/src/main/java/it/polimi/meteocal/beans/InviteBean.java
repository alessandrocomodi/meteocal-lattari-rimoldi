/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.beans;

import it.polimi.meteocal.business.manager.UserManager;
import it.polimi.meteocal.entities.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Francesco
 */
@Named(value = "inviteBean")
@ViewScoped
public class InviteBean implements Serializable {
    
    @EJB
    private UserManager um;
    
    private List<User> selectedUsers = new ArrayList<>();
        
    private List<User> allUsers;

    public List<User> getAllUsers() {
        return allUsers;
    }

    public void setAllUsers(List<User> allUsers) {
        this.allUsers = allUsers;
    }
  

    public List<User> getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(List<User> selectedUsers) {
        this.selectedUsers = selectedUsers;
    }

    public List<User> suggestUsers(String search){
        List<User> suggestions = new ArrayList<>();
        allUsers = um.findAllUsers();
        String organizer = um.getLoggedUser().getEmail();
        
        for(User u : this.allUsers){
            if(!u.getEmail().equals(organizer) && !selectedUsers.contains(u) && 
                    u.getEmail().startsWith(search)){
                suggestions.add(u);
            }
        }
        return suggestions;
    }

    /**
     * Creates a new instance of InviteBean
     */
    public InviteBean() {
    }
    
}
