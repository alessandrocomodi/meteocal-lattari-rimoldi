/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.gui.security;

import it.polimi.meteocal.business.security.boundary.EventManager;
import it.polimi.meteocal.business.security.boundary.NotificationManager;
import it.polimi.meteocal.business.security.boundary.UserManager;
import it.polimi.meteocal.business.security.entity.User;
import it.polimi.meteocal.entities.Event;
import it.polimi.meteocal.entities.Notification;
import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.inject.Named;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.CroppedImage;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Alessandro
 */
@ManagedBean
@SessionScoped
@Named
public class ModificationBean implements Serializable{

    @EJB
    private UserManager um;

    @EJB
    private EventManager em;
    
    @EJB 
    private NotificationManager nm;
    
    private User user;
    
    private Event event;

    private String parameter;
    
    private StreamedContent userAvatar;
    
    private CroppedImage croppedImage;

    public CroppedImage getCroppedImage() {
        return croppedImage;
    }

    public void setCroppedImage(CroppedImage croppedImage) {
        this.croppedImage = croppedImage;
    }


    public StreamedContent getUserAvatar() {
        return userAvatar;
    }
    
    public void crop(){
        InputStream is = new ByteArrayInputStream(croppedImage.getBytes());
        userAvatar = new DefaultStreamedContent(is, "image/png");
    }

    public void setUserAvatar(StreamedContent userAvatar) {
        this.userAvatar = userAvatar;
    }
    
    public void initialize(){
        InputStream is = new ByteArrayInputStream(getCurrentUser().getAvatar());
        userAvatar = new DefaultStreamedContent(is, "image/png");
    }

    public void eventSet(){
        this.event = em.find(Integer.parseInt(parameter));
    }
    
    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    
    private UploadedFile file;
    
    @PostConstruct
    public void init() {
        this.user = this.getCurrentUser();
    }
    
    public ModificationBean() {
    }
 
    public UploadedFile getFile() {
        return file;
    }
 
    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public User getUser() {
        if (user == null) {
            user = new User();
        }
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public String update() throws IOException {
        um.update(this.getCurrentUser(),user,file.getInputstream());
        return "user_home3?faces-redirect=true";
    }
    
    public String updateEvent(List<User> invitedUsers) throws ParserConfigurationException{
        event.setWeatherinfo(this.getWeatherConditionsForEventDay());
        em.update(em.find(Integer.parseInt(parameter)),this.event);
        if(invitedUsers != null){
            boolean duplicate = false;
            for(User u : invitedUsers){
                for(Notification n : u.getNotificationCollection()){
                    if(n.getEvent().getIdevent().equals(this.event.getIdevent())){
                        FacesContext context = FacesContext.getCurrentInstance();
                        FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "User: " + u.getEmail() + " already invited!");
                        context.addMessage(":eventForm:boh", fm);
                        duplicate = true;
                    }
                }
            }
            if(duplicate == true) {
                return "" ;
            }
            nm.createInvitation(invitedUsers, this.event.getIdevent());
            Integer id2 = 0;
            List<Notification> result2 = nm.getLastInvitation(this.event.getIdevent());
            for (Notification n : result2) {
                if (n.getIdnotification() > id2 && n.getType().equals("INVITATION")) {
                id2 = n.getIdnotification();
                }
            }
            for (User u : invitedUsers) {
                u.getNotificationCollection().add(nm.find(id2));
                um.updateUserNotificationList(u);
            }
        }
        //da qui inizio la gestione delle notifiche per la modifica evento
        List<User> participants = event.getUserCollection();
        if (participants != null && !participants.isEmpty()) {
            nm.createEventModifiedNotification(participants, this.event.getIdevent());
            Integer id3 = 0;
            List<Notification> result3 = nm.getLastInvitation(this.event.getIdevent());
            for (Notification n : result3) {
                if (n.getIdnotification() > id3 && n.getType().equals("MODIFICATION")) {
                    id3 = n.getIdnotification();
                }
            }
            for (User u : participants) {
                u.getNotificationCollection().add(nm.find(id3));
                um.updateUserNotificationList(u);
            }
            for (User u : participants) {
                em.removeParticipant(this.event, u);
            }
        }
        
        
        return "calendar_page?faces-redirect=true";
    }
    
    public void upload() {
        if(file != null) {
            FacesMessage message = new FacesMessage("Succesful", file.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
    
    public void uploadListener(FileUploadEvent event) throws IOException{
        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        System.out.println(event.getFile().getInputstream().toString());
        file = event.getFile();
        InputStream is = event.getFile().getInputstream();
        userAvatar = new DefaultStreamedContent(is, "image/png");  
        
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public Image getImageFromBytes() throws IOException{
        Image image = ImageIO.read(new ByteArrayInputStream(getCurrentUser().getAvatar()));
        return image;
    }
    
    public User getCurrentUser(){
        return um.getLoggedUser();
    }

    private String getWeatherConditionsForEventDay() throws ParserConfigurationException {
        String eventDate = this.getFormattedTime();
        String latitudine = event.getLat();
        String longitudine = event.getLon();
        List<String> iterateMe = this.retriveWeatherForecast(latitudine, longitudine);
        for (String s : iterateMe) {
            if (s.contains(eventDate)) {
                return s;
            }
        }
        return "No weather conditions available";
    }

    private String getFormattedTime() {
        //istruzioni per settare il meteo
        String year = "20" + (event.getStarttime().getYear() - 100);
        String month;
        int mon = event.getStarttime().getMonth() + 1;
        if (mon < 10) {
            month = "0" + mon;
        } else {
            month = "" + mon;
        }
        String day;
        if (event.getStarttime().getDate() < 10) {
            day = "0" + event.getStarttime().getDate();
        } else {
            day = "" + event.getStarttime().getDate();
        }
        String toBeCompared = year + "-" + month + "-" + day;
        return toBeCompared;
    }

    private List<String> retriveWeatherForecast(String latitudine, String longitudine) throws ParserConfigurationException {
         DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();

        URL oracle2;
        Document doc;
        
        try {
            oracle2 = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?lat=" + latitudine + "&lon=" + longitudine + "&mode=xml&units=metric&cnt=16&APPID=717e3c2ca6c7dfd9a18b4b25d27555af");
            doc = db.parse(oracle2.openStream());
            doc.getDocumentElement().normalize();
        } catch (Exception ex) {
            List<String> failReturn = new ArrayList();
            failReturn.add("No weather conditions available");
            return failReturn;
        }

        //liste di nodi contenenti gli elementi di interesse
        NodeList dataNodeList = doc.getElementsByTagName("time");
        NodeList weatherNodeList = doc.getElementsByTagName("symbol");
        NodeList tempNodeList = doc.getElementsByTagName("temperature");
        
        //Lista che conterrà tutti i dati, per ogni cella ci sarà una sequenza day code name min max separati da $
        List<String> weatherForecast2 = new ArrayList();
        String day = "";
        String code = "";
        String name = "";
        String min = "";
        String max = "";
        
        //tanto la lunghezza delle liste di nodi è sempre la stessa: 16
        for (int i = 0; i < dataNodeList.getLength(); i++) {
            
            
            //prima la data
            Node nNode1 = dataNodeList.item(i);
            if (nNode1.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nNode1;
                //salvo il dato
                day = element.getAttribute("day");
            }
            
            //poi il meteo
            Node nNode2 = weatherNodeList.item(i);
            if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nNode2;
                //salvo i dati
                code = element.getAttribute("number");
                name = element.getAttribute("name");
            }
            
            //infine la temperatura
            Node nNode3 = tempNodeList.item(i);
            
            if (nNode3.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nNode3;
                //salvo i dati
                min = element.getAttribute("min");
                max = element.getAttribute("max");
            }
            
            //lista dei dati
            weatherForecast2.add(day + ":" + code + ":" + name + ":" + min + ":" + max);
        }
        return weatherForecast2;
    }
}
