/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.gui.security;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;
import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.glassfish.grizzly.InputSource;

/**
 *
 * @author Francesco
 */
@ManagedBean
@Named(value = "wb")
@Dependent
public class WeatherBean {

    private String conditions;

    /**
     * Get the value of conditions
     *
     * @return the value of conditions
     */
    public String getConditions() {
        return conditions;
    }

    /**
     * Set the value of conditions
     *
     * @param conditions new value of conditions
     */
    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    
    private double lon;

    /**
     * Get the value of lon
     *
     * @return the value of lon
     */
    public double getLon() {
        return lon;
    }

    /**
     * Set the value of lon
     *
     * @param lon new value of lon
     */
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
    
    public void retrieveConditions() throws MalformedURLException, IOException, ParserConfigurationException {
        
        URL oracle = new URL("http://api.openweathermap.org/data/2.5/forecast?q=London&mode=xml&units=metric&cnt=7&APPID=717e3c2ca6c7dfd9a18b4b25d27555af");
        BufferedReader in = new BufferedReader(
        new InputStreamReader(oracle.openStream()));
        
        StringBuilder sb = new StringBuilder();
        String inputLine;
        
        while ((inputLine = in.readLine()) != null)
                System.err.println(inputLine);
        in.close();
    }
    
}
