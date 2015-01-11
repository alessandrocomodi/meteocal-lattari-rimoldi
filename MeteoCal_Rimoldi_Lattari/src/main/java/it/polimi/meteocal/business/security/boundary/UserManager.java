/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.business.security.boundary;

import it.polimi.meteocal.business.security.entity.Group;
import it.polimi.meteocal.business.security.entity.User;
import it.polimi.meteocal.entities.Calendar;
import it.polimi.meteocal.entities.Event;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.primefaces.model.UploadedFile;

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
    
    public void loadDefaultProfileImage() throws IOException {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("C:/Users/Francesco/Documents/NetBeansProjects/se2project/trunk/MeteoCal_Rimoldi_Lattari/src/main/webapp/images/icon-user-default.png"));
        } catch (IOException e) {
            System.out.println("Non l'ho letto!");
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "png", baos);
        bytes = baos.toByteArray();
    }

    public void save(User user) throws IOException {
        user.setCalendar(new Calendar(user.getPrivacy()));
        List<Event> eventOrganized = new ArrayList<Event>();
        //al momento generano eccezioni, ma andranno messi prima o poi questi comandi
//        List<Event> eventCollection= new ArrayList<Event>();
//        List<Notification> notificationCollection1 = new ArrayList<Notification>();
//        List<Notification> notificationCollection = new ArrayList<Notification>();
//        user.setNotificationCollection(notificationCollection);
//        user.setNotificationCollection1(notificationCollection1);
//        user.setEventCollection(eventCollection);
        user.setEventOrganized(eventOrganized);
        this.loadDefaultProfileImage();
        user.setAvatar(bytes);
        user.setGroupName(Group.USERS);
        user.getCalendar().setUser(user);
        em.persist(user);
        em.persist(user.getCalendar());
    }
    
    
    public void update(User user, User us,UploadedFile file) {
        user.setName(us.getName());
        user.setSurname(us.getSurname());
        user.setPhone(us.getPhone());
        user.setPrivacy(us.getPrivacy());
        user.setModifiedPassword(us.getPassword());
        user.getCalendar().setPrivate1(us.getPrivacy());
        em.merge(user);
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

    
}