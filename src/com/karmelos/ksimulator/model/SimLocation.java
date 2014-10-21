/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.karmelos.ksimulator.model;

import java.io.Serializable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 *
 * @author MorpheuS
 */

public class SimLocation implements  Serializable{
    
     private SimPoint topLeft;
     private double componentWidth;
     private double componentHeight;
   
   
    
       // private SimLocationPointId simPointLocId;

    public SimLocation() {
    }

//     @EmbeddedId
//    public SimLocationPointId getSimPointLocId() {
//        return simPointLocId;
//    }
//
//    public void setSimPointLocId(SimLocationPointId simPointLocId) {
//        this.simPointLocId = simPointLocId;
//    }
//    
    
    public SimLocation(double componentWidth, double componentHeight, SimPoint minimumAxis) {
        this.componentWidth = componentWidth;
        this.componentHeight = componentHeight;
        this.topLeft = minimumAxis;
    }
    
    

    public double getComponentWidth() {
        return componentWidth;
    }

    public void setComponentWidth(double componentWidth) {
        this.componentWidth = componentWidth;
    }

    public double getComponentHeight() {
        return componentHeight;
    }

    public void setComponentHeight(double componentHeight) {
        this.componentHeight = componentHeight;
    }
    
    @OneToOne
    @JoinColumn(insertable = false, updatable = false, name = "componentId", referencedColumnName = "componentId")
    public SimPoint getTopLeft() {
        return topLeft;
    }

    public void setTopLeft(SimPoint topLeft) {
        this.topLeft = topLeft;
    }
    

}
