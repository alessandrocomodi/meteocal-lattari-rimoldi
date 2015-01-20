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
    
    
    
}
