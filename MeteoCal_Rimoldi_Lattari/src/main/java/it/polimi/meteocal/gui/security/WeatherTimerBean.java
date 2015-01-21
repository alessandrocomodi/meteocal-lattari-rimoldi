/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.gui.security;

import it.polimi.meteocal.business.security.boundary.EventManager;
import it.polimi.meteocal.entities.Event;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TimerService;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Alessandro
 */
@Startup
@Singleton
public class WeatherTimerBean {
    private static final Logger logger = Logger.getLogger(WeatherTimerBean.class.getName());
    
    @EJB
    private EventManager em;
    
    @Resource
    private TimerService timerService;
    
    @PostConstruct
    private void init() {
        logger.log(Level.INFO, "WeatherChecker created");
    }

    @Schedule(minute = "0,3,5,10,20", hour = "16,17", persistent = false)
    public void checkWeatherAutoTimer() throws ParserConfigurationException {
        List<Event> eventsToCheck = em.findAllEvents();
        if (eventsToCheck != null && !eventsToCheck.isEmpty()) {
            for (Event e : eventsToCheck) {
                String eventDate = this.getFormattedTime(e);
                List<String> weatherForecast16Days;
                weatherForecast16Days = this.retriveWeatherForecast(e.getLat(), e.getLon());
                boolean flag = false;
                for (String s : weatherForecast16Days) {
                    if (s.contains(eventDate)) {
                        e.setWeatherinfo(s);
                        em.updateWeatherInfo(e);
                        flag = true;
                        logger.log(Level.INFO, e.getName() + e.getWeatherinfo(), new Date());
                    }
                }
                if (flag == false) {
                    e.setWeatherinfo("No weather conditions available");
                    em.updateWeatherInfo(e);
                    logger.log(Level.INFO, e.getName() + e.getWeatherinfo(), new Date());
                }
            }
            
        }
        
        
        System.out.println("prova");
    }
    
    private String getFormattedTime(Event event) {
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
