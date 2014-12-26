/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.business.security.boundary;

import it.polimi.meteocal.business.security.entity.Group;
import it.polimi.meteocal.business.security.entity.User;
import static it.polimi.meteocal.business.security.entity.User_.privacy;
import it.polimi.meteocal.entities.Calendar;
import it.polimi.meteocal.entities.Event;
import it.polimi.meteocal.entities.Notification;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.print.DocFlavor.URL;
import static javax.servlet.SessionTrackingMode.URL;

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
            img = ImageIO.read(new File("C:/Users/Alessandro/Documents/NetBeansProjects/MeteoCal_Rimoldi_Lattari/src/main/webapp/images/icon-user-default.png"));
        } catch (IOException e) {
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "png", baos);
        bytes = baos.toByteArray();
    }

        
    

    public void save(User user) throws IOException {
        user.setCalendar(new Calendar(user.getPrivacy()));
        //al momento generano eccezioni, ma andranno messi prima o poi questi comandi
//        List<Event> eventCollection1 = new ArrayList<Event>();
//        List<Event> eventCollection= new ArrayList<Event>();
//        List<Notification> notificationCollection1 = new ArrayList<Notification>();
//        List<Notification> notificationCollection = new ArrayList<Notification>();
//        user.setNotificationCollection(notificationCollection);
//        user.setNotificationCollection1(notificationCollection1);
//        user.setEventCollection(eventCollection);
//        user.setEventCollection1(eventCollection1);
        this.loadDefaultProfileImage();
        user.setAvatar(bytes);
        user.setGroupName(Group.USERS);
        user.getCalendar().setUser(user);
        em.persist(user);
        em.persist(user.getCalendar());
    }
    
    
    public void update(User user) {
        user.setName(user.getName());
        user.setSurname(user.getSurname());
        user.setPassword(user.getPassword());
        user.getCalendar().setPrivate1(user.getPrivacy());
        em.merge(user);
    }


    public void unregister() {
        em.remove(getLoggedUser());
    }

    public User getLoggedUser() {
        return em.find(User.class, principal.getName());
    }

    
}
