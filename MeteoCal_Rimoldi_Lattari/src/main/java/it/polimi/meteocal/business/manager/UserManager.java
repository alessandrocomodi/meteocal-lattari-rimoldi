/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.business.manager;

import it.polimi.meteocal.entities.Group;
import it.polimi.meteocal.entities.User;
import it.polimi.meteocal.entities.Event;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Francesco
 */
@Stateless
public class UserManager {
    
    @PersistenceContext
    EntityManager em;
    
    @Inject
    Principal principal;
    
    byte[] bytes;
    
    public void loadDefaultProfileImage(InputStream is) throws IOException {
        BufferedImage img = null;
        try {
            img = ImageIO.read(is);
        } catch (IOException e) {
            System.out.println("Non l'ho letto!");
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "png", baos);
        bytes = baos.toByteArray();
    }

    public void save(User user, InputStream is) throws IOException {
        List<Event> eventOrganized = new ArrayList<Event>();
        user.setEventOrganized(eventOrganized);
        this.loadDefaultProfileImage(is);
        user.setAvatar(bytes);
        user.setGroupName(Group.USERS);
        em.persist(user);
    }
    
    
    public void update(User user, User us,InputStream file) throws IOException {
        BufferedImage img = null;
        img = ImageIO.read(file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "png", baos);
        bytes = baos.toByteArray();
        user.setAvatar(bytes);
        user.setName(us.getName());
        user.setSurname(us.getSurname());
        user.setPhone(us.getPhone());
        user.setPrivacy(us.getPrivacy());
        user.setModifiedPassword(us.getPassword());
        em.merge(user);
    }
    
    public void updateUserNotificationList(User u) {
        em.merge(u);
    }

    public void unregister() {
        em.remove(getLoggedUser());
    }
    
    public User getUserFromEmail(String email) {
        return em.find(User.class, email);
    }
    
    public User getLoggedUser() {
        return em.find(User.class, principal.getName());
    }

    public void generateByteArray(String fileName) throws IOException {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(fileName));
        } catch (IOException e) {
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "png", baos);
        bytes = baos.toByteArray();
    }

    public List<User> findAllUsers(){
        List<User> results = em.createNamedQuery("User.findAll").getResultList();
        return results;
    }
    
}