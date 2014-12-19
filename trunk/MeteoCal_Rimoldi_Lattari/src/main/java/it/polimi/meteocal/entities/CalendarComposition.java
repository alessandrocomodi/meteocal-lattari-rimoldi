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
@Table(name = "calendar_composition")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CalendarComposition.findAll", query = "SELECT c FROM CalendarComposition c"),
    @NamedQuery(name = "CalendarComposition.findByCalendar", query = "SELECT c FROM CalendarComposition c WHERE c.calendarCompositionPK.calendar = :calendar"),
    @NamedQuery(name = "CalendarComposition.findByEvent", query = "SELECT c FROM CalendarComposition c WHERE c.calendarCompositionPK.event = :event")})
public class CalendarComposition implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CalendarCompositionPK calendarCompositionPK;

    public CalendarComposition() {
    }

    public CalendarComposition(CalendarCompositionPK calendarCompositionPK) {
        this.calendarCompositionPK = calendarCompositionPK;
    }

    public CalendarComposition(String calendar, String event) {
        this.calendarCompositionPK = new CalendarCompositionPK(calendar, event);
    }

    public CalendarCompositionPK getCalendarCompositionPK() {
        return calendarCompositionPK;
    }

    public void setCalendarCompositionPK(CalendarCompositionPK calendarCompositionPK) {
        this.calendarCompositionPK = calendarCompositionPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (calendarCompositionPK != null ? calendarCompositionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CalendarComposition)) {
            return false;
        }
        CalendarComposition other = (CalendarComposition) object;
        if ((this.calendarCompositionPK == null && other.calendarCompositionPK != null) || (this.calendarCompositionPK != null && !this.calendarCompositionPK.equals(other.calendarCompositionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.polimi.meteocal.entities.CalendarComposition[ calendarCompositionPK=" + calendarCompositionPK + " ]";
    }
    
}
