/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.entities;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Francesco
 */
@Entity
@Table(name = "notification_addressees")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NotificationAddressees.findAll", query = "SELECT n FROM NotificationAddressees n"),
    @NamedQuery(name = "NotificationAddressees.findByNotification", query = "SELECT n FROM NotificationAddressees n WHERE n.notificationAddresseesPK.notification = :notification"),
    @NamedQuery(name = "NotificationAddressees.findByAddreesee", query = "SELECT n FROM NotificationAddressees n WHERE n.notificationAddresseesPK.addreesee = :addreesee")})
public class NotificationAddressees implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected NotificationAddresseesPK notificationAddresseesPK;

    public NotificationAddressees() {
    }

    public NotificationAddressees(NotificationAddresseesPK notificationAddresseesPK) {
        this.notificationAddresseesPK = notificationAddresseesPK;
    }

    public NotificationAddressees(String notification, String addreesee) {
        this.notificationAddresseesPK = new NotificationAddresseesPK(notification, addreesee);
    }

    public NotificationAddresseesPK getNotificationAddresseesPK() {
        return notificationAddresseesPK;
    }

    public void setNotificationAddresseesPK(NotificationAddresseesPK notificationAddresseesPK) {
        this.notificationAddresseesPK = notificationAddresseesPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (notificationAddresseesPK != null ? notificationAddresseesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NotificationAddressees)) {
            return false;
        }
        NotificationAddressees other = (NotificationAddressees) object;
        if ((this.notificationAddresseesPK == null && other.notificationAddresseesPK != null) || (this.notificationAddresseesPK != null && !this.notificationAddresseesPK.equals(other.notificationAddresseesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.polimi.meteocal.entities.NotificationAddressees[ notificationAddresseesPK=" + notificationAddresseesPK + " ]";
    }
    
}
