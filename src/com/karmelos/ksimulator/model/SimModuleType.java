/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.karmelos.ksimulator.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

/**
 *
 * @author odada
 */
@Entity
public class SimModuleType implements Serializable {
    
    private Long id;
    
    private String typeName;
    
    private String description;
    
    private List<SimModule> modules;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    
    @Lob
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @OneToMany(mappedBy = "moduleType")
    public List<SimModule> getModules() {
        return modules;
    }

    public void setModules(List<SimModule> modules) {
        this.modules = modules;
    }

    @Override
    public int hashCode() {
        int hash = 5;
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
        final SimModuleType other = (SimModuleType) obj;
       if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.typeName == null) ? (other.typeName != null) : !this.typeName.equals(other.typeName)) {
            return false;
        }
        if ((this.description == null) ? (other.description != null) : !this.description.equals(other.description)) {
            return false;
        }
        if (this.modules != other.modules && (this.modules == null || !this.modules.equals(other.modules))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return getTypeName();
    }
    
}
