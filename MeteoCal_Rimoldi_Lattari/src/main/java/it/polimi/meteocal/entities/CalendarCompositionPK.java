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
public class CalendarCompositionPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "calendar")
    private String calendar;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "event")
    private String event;

    public CalendarCompositionPK() {
    }

    public CalendarCompositionPK(String calendar, String event) {
        this.calendar = calendar;
        this.event = event;
    }

    public String getCalendar() {
        return calendar;
    }

    public void setCalendar(String calendar) {
        this.calendar = calendar;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (calendar != null ? calendar.hashCode() : 0);
        hash += (event != null ? event.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CalendarCompositionPK)) {
            return false;
        }
        CalendarCompositionPK other = (CalendarCompositionPK) object;
        if ((this.calendar == null && other.calendar != null) || (this.calendar != null && !this.calendar.equals(other.calendar))) {
            return false;
        }
        if ((this.event == null && other.event != null) || (this.event != null && !this.event.equals(other.event))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.polimi.meteocal.entities.CalendarCompositionPK[ calendar=" + calendar + ", event=" + event + " ]";
    }
    
}
