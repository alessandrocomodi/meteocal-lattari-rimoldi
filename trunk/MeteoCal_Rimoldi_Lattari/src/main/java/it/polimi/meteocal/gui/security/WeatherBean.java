/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.gui.security;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;
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
 * @author Francesco
 */
@ManagedBean
@Dependent
public class WeatherBean {

    private String conditions;

    private String lat2;
    
    private String lon2;
    
    
    private List<String> weatherConditions;

    public List<String> getWeatherConditions() {
        return weatherConditions;
    }

    public void setWeatherConditions(List<String> weatherConditions) {
        this.weatherConditions = weatherConditions;
    }

    public String getLon2() {
        return lon2;
    }

    public void setLon2(String lon2) {
        this.lon2 = lon2;
    }


    public String getLat2() {
        return lat2;
    }

    public void setLat2(String lat2) {
        this.lat2 = lat2;
    }

    
    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    private double lon;

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    
    private double lat;

    /**
     * Get the value of lat
     *
     * @return the value of lat
     */
    public double getLat() {
        return lat;
    }

    /**
     * Set the value of lat
     *
     * @param lat new value of lat
     */
    public void setLat(double lat) {
        this.lat = lat;
    }

    
    /**
     * Creates a new instance of WeatherBean
     */
    public WeatherBean() {
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
        List<String> weatherForecast = new ArrayList();
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
    
}
