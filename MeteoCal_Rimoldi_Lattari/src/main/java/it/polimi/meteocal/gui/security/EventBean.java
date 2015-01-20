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
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Alessandro
 */
@ManagedBean
@RequestScoped
@Named("eb")
public class EventBean {
    

    @EJB
    EventManager em;
    
    @EJB
    private NotificationManager nm;
    
    @EJB
    private UserManager um;
    
    private Event event;

    private List<User> guests;
    
    private String lat2;
    
    private List<String> weatherForecast;

    public String getLat2() {
        return lat2;
    }

    public void setLat2(String lat2) {
        this.lat2 = lat2;
    }

    private String lon2;

    public String getLon2() {
        return lon2;
    }

    public void setLon2(String lon2) {
        this.lon2 = lon2;
    }
    
    private List<String> weatherConditions;

    public List<String> getWeatherConditions() {
        return weatherConditions;
    }

    public void setWeatherConditions(List<String> weatherConditions) {
        this.weatherConditions = weatherConditions;
    }



    public List<User> getGuests() {
        return guests;
    }

    public void setGuests(List<User> guests) {
        this.guests = guests;
    }
    
    public String acceptInvitation(Event e, User u) {
        em.addParticipant(e, u);
        return "notification_page?faces-redirect=true";
    }
    
    public EventBean() {
    }

    public Event getEvent() {
        if (event == null) {
            event = new Event();
        }
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
    
    public String createEvent(User user, List<User> invitedUsers) throws ParserConfigurationException, SAXException, IOException {
        Integer id = 0;
        event.setWeatherinfo(this.getWeatherConditionsForEventDay());
        em.createEvent(event, user);
        List<Event> result = em.getEventOrganized(user.getEmail());
        for (Event e : result) {
            if (e.getIdevent() > id) {
                id = e.getIdevent();
            }
        }
        if(invitedUsers != null){
            nm.createInvitation(invitedUsers, id);
            Integer id2 = 0;
            List<Notification> result2 = nm.getLastInvitation(id);
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
        return "calendar_page?feces-redirect=true";
    }
    
    
    public String deleteEvent(Event eventToBeDeleted) {
        nm.removeEveryReletedNotification(eventToBeDeleted.getIdevent());
        nm.createEventDeletedNotification(eventToBeDeleted.getUserCollection(), eventToBeDeleted.getIdevent());
        em.deleteEvent(eventToBeDeleted);
        return "user_home3?feces-redirect=true";
    }
    
    public void addGuest(User user) {
        this.guests.add(user);
    }
    
    private String getWeatherConditionsForEventDay() throws ParserConfigurationException, SAXException, IOException {
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
    
    
    public void retrieveConditions() throws ParserConfigurationException, MalformedURLException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();

        URL oracle2 = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?lat=" + lat2 + "&lon=" + lon2 + "&mode=xml&units=metric&cnt=16&APPID=717e3c2ca6c7dfd9a18b4b25d27555af");

        Document doc = db.parse(oracle2.openStream());

        doc.getDocumentElement().normalize();

        //liste di nodi contenenti gli elementi di interesse
        NodeList dataNodeList = doc.getElementsByTagName("time");
        NodeList weatherNodeList = doc.getElementsByTagName("symbol");
        NodeList tempNodeList = doc.getElementsByTagName("temperature");
        
        //Lista che conterrà tutti i dati, per ogni cella ci sarà una sequenza day code name min max separati da $
        weatherForecast = new ArrayList();
        weatherConditions = new ArrayList();
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
            weatherForecast.add(day + "$" + code + "$" + name + "$" + min + "$" + max);
            weatherConditions.add("Day: " + day + " Cond: " + name + " Min: "+  min + " Max: " + max);
        }
        
        //prova di output
        for (String s : weatherForecast) {
            System.out.println(s);
        }
    }
    
    private List<String> retriveWeatherForecast(String lat, String lon) throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();

        URL oracle2;
        Document doc;
        
        try {
            oracle2 = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?lat=" + lat + "&lon=" + lon + "&mode=xml&units=metric&cnt=16&APPID=717e3c2ca6c7dfd9a18b4b25d27555af");
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
