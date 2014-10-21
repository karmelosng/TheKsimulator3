                                                                    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.karmelos.ksimulator.model;
import java.io.Serializable;
import javax.persistence.Embeddable;

/**
 *
 * @author MorpheuS
 */

@Embeddable
public class SimLocationPointId implements Serializable {
  private String userId;
  private Long componentId;
   private Long stateId;

    
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getComponentId() {
        return componentId;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }
     
    public void setComponentId(Long componentId) {
        this.componentId = componentId;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.userId != null ? this.userId.hashCode() : 0);
        hash = 59 * hash + (this.componentId != null ? this.componentId.hashCode() : 0);
        hash = 59 * hash + (this.stateId != null ? this.stateId.hashCode() : 0);
        return hash;
    }

     @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SimLocationPointId other = (SimLocationPointId) obj;
        if ((this.userId == null) ? (other.userId != null) : !this.userId.equals(other.userId)) {
            return false;
        }
        if (this.componentId != other.componentId && (this.componentId == null || !this.componentId.equals(other.componentId))) {
            return false;
        }
        if (this.stateId != other.stateId && (this.stateId == null || !this.stateId.equals(other.stateId))) {
            return false;
        }
        return true;
    }

    
}
