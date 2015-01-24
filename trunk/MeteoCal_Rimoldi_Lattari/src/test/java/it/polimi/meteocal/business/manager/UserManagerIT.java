/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.business.manager;

import it.polimi.meteocal.business.manager.UserManager;
import it.polimi.meteocal.entities.User;
import it.polimi.meteocal.utilities.PasswordEncrypter;
import java.awt.image.BufferedImage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.ejb.EJB;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author miglie
 */
@RunWith(Arquillian.class)
public class UserManagerIT {
    

    @EJB
    UserManager cut;
    
    @PersistenceContext
    EntityManager em;
    
//    @PersistenceContext
//    EntityManager em;

    @Deployment
    public static WebArchive createArchiveAndDeploy() {
        return ShrinkWrap.create(WebArchive.class)
                .addClass(UserManager.class)
                .addPackage(User.class.getPackage())
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }
   
    @Test
    public void UserManagerShouldBeInjected() {
        assertNotNull(cut);
    }
    
    @Test
    public void EntityManagerShouldBeInjected() {
        assertNotNull(em);
    }
    
    @Test
    public void passwordsShouldBeEncryptedOnDB() throws IOException {
        User newUser = new User();
        String email = "marco@polimi.com";
        String password = "password";
        newUser.setEmail(email);
        newUser.setName("Marco");
        newUser.setSurname("Miglierina");
        newUser.setPassword(password);
        BufferedImage img = ImageIO.read(new File("C:/Users/Alessandro/Documents/NetBeansProjects/MeteoCal_Rimoldi_Lattari/src/main/webapp/images/icon-user-default.png"));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "png", baos);
        byte[] bytes = baos.toByteArray();
        InputStream is = new ByteArrayInputStream(bytes);
        cut.save(newUser,is);
        User foundUser = em.find(User.class, email);
        assertNotNull(foundUser);
        assertThat(foundUser.getPassword(),is(PasswordEncrypter.encryptPassword(password)));
    }

}