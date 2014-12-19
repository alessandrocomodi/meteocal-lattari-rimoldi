/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Alessandro
 */
@Entity
@Table(name = "calendar")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Calendar.findAll", query = "SELECT c FROM Calendar c"),
    @NamedQuery(name = "Calendar.findByIdcalendar", query = "SELECT c FROM Calendar c WHERE c.idcalendar = :idcalendar"),
    @NamedQuery(name = "Calendar.findByPrivate1", query = "SELECT c FROM Calendar c WHERE c.private1 = :private1")})
public class Calendar implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idcalendar")
    private Integer idcalendar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "private")
    private boolean private1;
    @JoinTable(name = "calendarcomp", joinColumns = {
        @JoinColumn(name = "calendar", referencedColumnName = "idcalendar")}, inverseJoinColumns = {
        @JoinColumn(name = "event", referencedColumnName = "idevent")})
    @ManyToMany
    private Collection<Event> eventCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "calendar")
    private Collection<User> userCollection;
    @JoinColumn(name = "user", referencedColumnName = "email")
    @ManyToOne(optional = false)
    private User user;

    public Calendar() {
    }

    public Calendar(Integer idcalendar) {
        this.idcalendar = idcalendar;
    }

    public Calendar(Integer idcalendar, boolean private1) {
        this.idcalendar = idcalendar;
        this.private1 = private1;
    }

    public Integer getIdcalendar() {
        return idcalendar;
    }

    public void setIdcalendar(Integer idcalendar) {
        this.idcalendar = idcalendar;
    }

    public boolean getPrivate1() {
        return private1;
    }

    public void setPrivate1(boolean private1) {
        this.private1 = private1;
    }

    @XmlTransient
    public Collection<Event> getEventCollection() {
        return eventCollection;
    }

    public void setEventCollection(Collection<Event> eventCollection) {
        this.eventCollection = eventCollection;
    }

    @XmlTransient
    public Collection<User> getUserCollection() {
        return userCollection;
    }

    public void setUserCollection(Collection<User> userCollection) {
        this.userCollection = userCollection;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idcalendar != null ? idcalendar.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Calendar)) {
            return false;
        }
        Calendar other = (Calendar) object;
        if ((this.idcalendar == null && other.idcalendar != null) || (this.idcalendar != null && !this.idcalendar.equals(other.idcalendar))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.polimi.meteocal.entities.Calendar[ idcalendar=" + idcalendar + " ]";
    }
    
}
