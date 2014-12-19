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
@Table(name = "participations")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Participations.findAll", query = "SELECT p FROM Participations p"),
    @NamedQuery(name = "Participations.findByEvent", query = "SELECT p FROM Participations p WHERE p.participationsPK.event = :event"),
    @NamedQuery(name = "Participations.findByUser", query = "SELECT p FROM Participations p WHERE p.participationsPK.user = :user")})
public class Participations implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ParticipationsPK participationsPK;

    public Participations() {
    }

    public Participations(ParticipationsPK participationsPK) {
        this.participationsPK = participationsPK;
    }

    public Participations(String event, String user) {
        this.participationsPK = new ParticipationsPK(event, user);
    }

    public ParticipationsPK getParticipationsPK() {
        return participationsPK;
    }

    public void setParticipationsPK(ParticipationsPK participationsPK) {
        this.participationsPK = participationsPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (participationsPK != null ? participationsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Participations)) {
            return false;
        }
        Participations other = (Participations) object;
        if ((this.participationsPK == null && other.participationsPK != null) || (this.participationsPK != null && !this.participationsPK.equals(other.participationsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.polimi.meteocal.entities.Participations[ participationsPK=" + participationsPK + " ]";
    }
    
}
