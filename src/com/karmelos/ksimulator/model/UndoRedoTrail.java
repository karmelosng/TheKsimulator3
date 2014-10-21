/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.karmelos.ksimulator.model;

import java.util.Date;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Dare
 */

public class UndoRedoTrail {
    // This class takes care of the undoredotrail, deleted after End of Session

    private String userName;
    private Date timingOfMovement;
    private String simComponent;
    private SimPoint lastSimPoint;
    private String operation; // either dropped, moved or deleted

    public String getOperation() {
        return operation;
    }

   
    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getUserName() {
        return userName;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getTimingOfMovement() {
        return timingOfMovement;
    }

   
    public void setTimingOfMovement(Date timingOfMovement) {
        this.timingOfMovement = timingOfMovement;
    }

    public String getSimComponent() {
        return simComponent;
    }

    
    public void setSimComponent(String simComponent) {
        this.simComponent = simComponent;
    }

    public SimPoint getLastSimPoint() {
        return lastSimPoint;
    }

    
    public void setLastSimPoint(SimPoint lastSimPoint) {
        this.lastSimPoint = lastSimPoint;
    }
}
