/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.karmelos.ksimulator.model;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 *
 * @author MorpheuS
 */
@Entity
public class SimPoint implements Serializable{
    private double topX;
    private double topY;
    
     @EmbeddedId
    private SimLocationPointId simPointLocId;

    public SimPoint() {
    }

    public SimPoint(double topX, double topY) {
        this.topX = topX;
        this.topY = topY;
    }
    
   
   public SimLocationPointId getSimPointLocId() {
        return simPointLocId;
    }

    public void setSimPointLocId(SimLocationPointId simPointLocId) {
        this.simPointLocId = simPointLocId;
    }
    
   
    public double getTopX() {
        return topX;
    }

    public void setTopX(double topX) {
        this.topX = topX;
    }

    public double getTopY() {
        return topY;
    }

    public void setTopY(double topY) {
        this.topY = topY;
    }

    @Override
    public String toString() {
        return getTopX()+":"+getTopY();
    }
    
}
