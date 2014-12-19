/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
 * @author Francesco
 */
@Entity
@Table(name = "event")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Event.findAll", query = "SELECT e FROM Event e"),
    @NamedQuery(name = "Event.findById", query = "SELECT e FROM Event e WHERE e.id = :id"),
    @NamedQuery(name = "Event.findByName", query = "SELECT e FROM Event e WHERE e.name = :name"),
    @NamedQuery(name = "Event.findByStartTime", query = "SELECT e FROM Event e WHERE e.startTime = :startTime"),
    @NamedQuery(name = "Event.findByEndTime", query = "SELECT e FROM Event e WHERE e.endTime = :endTime"),
    @NamedQuery(name = "Event.findByDescription", query = "SELECT e FROM Event e WHERE e.description = :description"),
    @NamedQuery(name = "Event.findByPrivate1", query = "SELECT e FROM Event e WHERE e.private1 = :private1"),
    @NamedQuery(name = "Event.findByIndoor", query = "SELECT e FROM Event e WHERE e.indoor = :indoor"),
    @NamedQuery(name = "Event.findByCreator", query = "SELECT e FROM Event e WHERE e.creator = :creator"),
    @NamedQuery(name = "Event.findByPlace", query = "SELECT e FROM Event e WHERE e.place = :place")})
public class Event implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "event")
    private Collection<Notification> notificationCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
    @Basic(optional = false)
    @NotNull
    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;
    @Size(max = 45)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "private")
    private boolean private1;
    @Basic(optional = false)
    @NotNull
    @Column(name = "indoor")
    private boolean indoor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "creator")
    private String creator;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "place")
    private String place;

    public Event() {
    }

    public Event(Integer id) {
        this.id = id;
    }

    public Event(Integer id, String name, Date startTime, Date endTime, boolean private1, boolean indoor, String creator, String place) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.private1 = private1;
        this.indoor = indoor;
        this.creator = creator;
        this.place = place;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getPrivate1() {
        return private1;
    }

    public void setPrivate1(boolean private1) {
        this.private1 = private1;
    }

    public boolean getIndoor() {
        return indoor;
    }

    public void setIndoor(boolean indoor) {
        this.indoor = indoor;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Event)) {
            return false;
        }
        Event other = (Event) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.polimi.meteocal.entities.Event[ id=" + id + " ]";
    }

    @XmlTransient
    public Collection<Notification> getNotificationCollection() {
        return notificationCollection;
    }

    public void setNotificationCollection(Collection<Notification> notificationCollection) {
        this.notificationCollection = notificationCollection;
    }
    
}
