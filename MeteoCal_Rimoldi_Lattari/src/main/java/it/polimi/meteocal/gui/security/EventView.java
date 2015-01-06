/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.gui.security;

import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.primefaces.event.map.PointSelectEvent;
import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.LatLngBounds;

/**
 *
 * @author Francesco
 */
@ManagedBean
public class EventView implements Serializable {
    
    private String address;

    /**
     * Get the value of address
     *
     * @return the value of address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Set the value of address
     *
     * @param address new value of address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    
    private double lat;
    
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

    
    public void onStateChange(StateChangeEvent event) {
        LatLngBounds bounds = event.getBounds();
        int zoomLevel = event.getZoomLevel();
          
        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Zoom Level", String.valueOf(zoomLevel)));
        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Center", event.getCenter().toString()));
        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "NorthEast", bounds.getNorthEast().toString()));
        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "SouthWest", bounds.getSouthWest().toString()));
    }
      
    public void onPointSelect(PointSelectEvent event) {
        LatLng latlng = event.getLatLng();
        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Point Selected", "Lat:" + latlng.getLat() + ", Lng:" + latlng.getLng()));
        setLat(latlng.getLat());
        setLon(latlng.getLng());
    }
    
    public void onPointSelected(Double lat, Double lon) {
        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Point Selected", "Lat:" +lat + ", Lng:" + lon));
    }
    
    public void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    

    
    
}
