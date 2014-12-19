/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Francesco
 */
@Embeddable
public class NotificationAddresseesPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "notification")
    private String notification;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "addreesee")
    private String addreesee;

    public NotificationAddresseesPK() {
    }

    public NotificationAddresseesPK(String notification, String addreesee) {
        this.notification = notification;
        this.addreesee = addreesee;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getAddreesee() {
        return addreesee;
    }

    public void setAddreesee(String addreesee) {
        this.addreesee = addreesee;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (notification != null ? notification.hashCode() : 0);
        hash += (addreesee != null ? addreesee.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NotificationAddresseesPK)) {
            return false;
        }
        NotificationAddresseesPK other = (NotificationAddresseesPK) object;
        if ((this.notification == null && other.notification != null) || (this.notification != null && !this.notification.equals(other.notification))) {
            return false;
        }
        if ((this.addreesee == null && other.addreesee != null) || (this.addreesee != null && !this.addreesee.equals(other.addreesee))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.polimi.meteocal.entities.NotificationAddresseesPK[ notification=" + notification + ", addreesee=" + addreesee + " ]";
    }
    
}
