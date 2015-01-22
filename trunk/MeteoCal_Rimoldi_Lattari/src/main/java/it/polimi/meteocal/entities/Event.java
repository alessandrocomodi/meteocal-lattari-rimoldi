/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Alessandro
 */
@Entity
@Table(name = "event")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Event.findAll", query = "SELECT e FROM Event e"),
    @NamedQuery(name = "Event.findByIdevent", query = "SELECT e FROM Event e WHERE e.idevent = :idevent"),
    @NamedQuery(name = "Event.findByIndoor", query = "SELECT e FROM Event e WHERE e.indoor = :indoor"),
    @NamedQuery(name = "Event.findByPrivate1", query = "SELECT e FROM Event e WHERE e.private1 = :private1"),
    @NamedQuery(name = "Event.findByStarttime", query = "SELECT e FROM Event e WHERE e.starttime = :starttime"),
    @NamedQuery(name = "Event.findByEndtime", query = "SELECT e FROM Event e WHERE e.endtime = :endtime"),
    @NamedQuery(name = "Event.findByName", query = "SELECT e FROM Event e WHERE e.name = :name"),
    @NamedQuery(name = "Event.findByWeatherinfo", query = "SELECT e FROM Event e WHERE e.weatherinfo = :weatherinfo"),
    @NamedQuery(name = "Event.findByPlace", query = "SELECT e FROM Event e WHERE e.place = :place"),
    @NamedQuery(name = "Event.findByOrganizer", query = "SELECT e FROM Event e WHERE e.owner.email = :owner")})
public class Event implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idevent")
    private Integer idevent;
    @Lob
    @Size(max = 16777215)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "indoor")
    private boolean indoor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "private")
    private boolean private1;
    @Basic(optional = false)
    @NotNull
    @Column(name = "starttime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date starttime;
    @Basic(optional = false)
    @NotNull
    @Column(name = "endtime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endtime;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "name")
    private String name;
    @Size(min = 1, max = 200)
    @Column(name = "weatherinfo")
    private String weatherinfo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "place")
    private String place;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "lat")
    private String lat;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "lon")
    private String lon;
    
    @JoinTable(name = "participants", joinColumns = {
        @JoinColumn(name = "event", referencedColumnName = "idevent")}, inverseJoinColumns = {
        @JoinColumn(name = "user", referencedColumnName = "email")})
    @ManyToMany
    private List<User> userCollection;
    @JoinColumn(name = "owner", referencedColumnName = "email")
    @ManyToOne(optional = false)
    private User owner;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "event")
    private Collection<Notification> notificationCollection;

    public Event() {
    }

    public Event(Integer idevent) {
        this.idevent = idevent;
    }

    public Event(Integer idevent, boolean indoor, boolean private1, Date starttime, Date endtime, String name, String weatherinfo, String place) {
        this.idevent = idevent;
        this.indoor = indoor;
        this.private1 = private1;
        this.starttime = starttime;
        this.endtime = endtime;
        this.name = name;
        this.weatherinfo = weatherinfo;
        this.place = place;
    }

    public Integer getIdevent() {
        return idevent;
    }

    public void setIdevent(Integer idevent) {
        this.idevent = idevent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getIndoor() {
        return indoor;
    }
    
    public String getIndoorString() {
        if (indoor) {
            return "Yes";
        }
        return "No";
    }
    
    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public void setIndoor(boolean indoor) {
        this.indoor = indoor;
    }

    public boolean getPrivate1() {
        return private1;
    }
    
    public String getPrivateString() {
        if (private1) {
            return "Private";
        }
        return "Public";
    } 

    public void setPrivate1(boolean private1) {
        this.private1 = private1;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeatherinfo() {
        if (weatherinfo == null) {
            return "Not yet available";
        }
        return weatherinfo;
    }

    public void setWeatherinfo(String weatherinfo) {
        this.weatherinfo = weatherinfo;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    @XmlTransient
    public List<User> getUserCollection() {
        return userCollection;
    }

    public void setUserCollection(List<User> userCollection) {
        this.userCollection = userCollection;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @XmlTransient
    public Collection<Notification> getNotificationCollection() {
        return notificationCollection;
    }

    public void setNotificationCollection(Collection<Notification> notificationCollection) {
        this.notificationCollection = notificationCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idevent != null ? idevent.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Event)) {
            return false;
        }
        Event other = (Event) object;
        if ((this.idevent == null && other.idevent != null) || (this.idevent != null && !this.idevent.equals(other.idevent))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.polimi.meteocal.entities.Event[ idevent=" + idevent + " ]";
    }
    
    public String retrieveMinTemp() {
        if(weatherinfo.equals("No weather conditions available") || weatherinfo == null){
            return "---";
        }
        String [] parts = weatherinfo.split(":");
        return parts[3];
    }
    
    public String retrieveMaxTemp() {
        if(weatherinfo.equals("No weather conditions available") || weatherinfo == null){
            return "---";
        }
        String [] parts = weatherinfo.split(":");
        return parts[4];
    }
    
    public String retrieveNameCondition() {
        if(weatherinfo.equals("No weather conditions available") || weatherinfo == null){
            return "---";
        }
        String [] parts = weatherinfo.split(":");
        return parts[2];
    }
    
    public String retriveWeatherIconNumber() {
        String [] parts = weatherinfo.split(":");
        int n;
        n = Integer.parseInt(parts[1]);
        boolean day;
        String icon = "";

        //controllo se l'evento inizia di giorno o di notte
        if (starttime.getHours() > 17 || starttime.getHours() < 5) {
        day = false;
        } else {
        day = true;
        }

        //identifico la giusta icona
        if (n == 800) {
        if (day) {
        icon = "01d.png";
        } else {
        icon = "01n.png"; 
        }
        }
        if (n == 801) {
        if (day) {
        icon = "02d.png";
        } else {
        icon = "02n.png"; 
        }
        }
        if (n == 802) {
        if (day) {
        icon = "03d.png";
        } else {
        icon = "03n.png"; 
        }
        }
        if (n == 803 || n == 804) {
        if (day) {
        icon = "04d.png";
        } else {
        icon = "04n.png"; 
        }
        }
        if (n >= 300 && n <= 321) {
        if (day) {
        icon = "09d.png";
        } else {
        icon = "09n.png"; 
        }
        }
        if (n >= 500 && n <= 504) {
        if (day) {
        icon = "10d.png";
        } else {
        icon = "10n.png"; 
        }
        }
        if (n >= 200 && n <= 232) {
        if (day) {
        icon = "11d.png";
        } else {
        icon = "11n.png"; 
        }
        }
        if (n == 511 || n >= 600 && n <= 622) {
        if (day) {
        icon = "13d.png";
        } else {
        icon = "13n.png"; 
        }
        }
        if (n >= 701 && n <= 781) {
        if (day) {
        icon = "50d.png";
        } else {
        icon = "50n.png"; 
        }
        }
        if (n >= 900 && n <= 962) {
        if (day) {
        icon = "11d.png";
        } else {
        icon = "11n.png"; 
        }
        }
        return icon;
    }
    
}
