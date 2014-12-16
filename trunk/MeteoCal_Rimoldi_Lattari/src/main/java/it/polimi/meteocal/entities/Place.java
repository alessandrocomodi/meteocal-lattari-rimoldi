/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Alessandro
 */
@Entity
@Table(name = "place")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Place.findAll", query = "SELECT p FROM Place p"),
    @NamedQuery(name = "Place.findByStreet", query = "SELECT p FROM Place p WHERE p.street = :street"),
    @NamedQuery(name = "Place.findByNumber", query = "SELECT p FROM Place p WHERE p.number = :number"),
    @NamedQuery(name = "Place.findByCity", query = "SELECT p FROM Place p WHERE p.city = :city"),
    @NamedQuery(name = "Place.findByCountry", query = "SELECT p FROM Place p WHERE p.country = :country"),
    @NamedQuery(name = "Place.findByName", query = "SELECT p FROM Place p WHERE p.name = :name"),
    @NamedQuery(name = "Place.findByWeatherUpdateTime", query = "SELECT p FROM Place p WHERE p.weatherUpdateTime = :weatherUpdateTime"),
    @NamedQuery(name = "Place.findByWeatherInfo", query = "SELECT p FROM Place p WHERE p.weatherInfo = :weatherInfo"),
    @NamedQuery(name = "Place.findByPlaceId", query = "SELECT p FROM Place p WHERE p.placeId = :placeId")})
public class Place implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "street")
    private String street;
    @Basic(optional = false)
    @NotNull
    @Column(name = "number")
    private int number;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "city")
    private String city;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "country")
    private String country;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Column(name = "weather_update_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date weatherUpdateTime;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "weather_info")
    private String weatherInfo;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "place_id")
    private String placeId;

    public Place() {
    }

    public Place(String placeId) {
        this.placeId = placeId;
    }

    public Place(String placeId, String street, int number, String city, String country, String name, Date weatherUpdateTime, String weatherInfo) {
        this.placeId = placeId;
        this.street = street;
        this.number = number;
        this.city = city;
        this.country = country;
        this.name = name;
        this.weatherUpdateTime = weatherUpdateTime;
        this.weatherInfo = weatherInfo;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getWeatherUpdateTime() {
        return weatherUpdateTime;
    }

    public void setWeatherUpdateTime(Date weatherUpdateTime) {
        this.weatherUpdateTime = weatherUpdateTime;
    }

    public String getWeatherInfo() {
        return weatherInfo;
    }

    public void setWeatherInfo(String weatherInfo) {
        this.weatherInfo = weatherInfo;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (placeId != null ? placeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Place)) {
            return false;
        }
        Place other = (Place) object;
        if ((this.placeId == null && other.placeId != null) || (this.placeId != null && !this.placeId.equals(other.placeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.polimi.meteocal.entities.Place[ placeId=" + placeId + " ]";
    }
    
}
