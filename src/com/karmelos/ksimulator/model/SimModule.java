package com.karmelos.ksimulator.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class SimModule implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 4237619316615369473L;
    private Long id;
    private String modelName;
    private String description;
    private String versionName;
    private SimModuleType moduleType;
    private List<SimComponent> components;
    private double scaleRatio=0.00;
    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getScaleRatio() {
        return scaleRatio;
    }

    public void setScaleRatio(double scaleRatio) {
        this.scaleRatio = scaleRatio;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    @Lob
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    @ManyToOne
    public SimModuleType getModuleType() {
        return moduleType;
    }

    public void setModuleType(SimModuleType moduleType) {
        this.moduleType = moduleType;
    }

    @OneToMany(fetch = FetchType.EAGER)
    public List<SimComponent> getComponents() {
        return components;
    }

    public void setComponents(List<SimComponent> components) {
        this.components = components;
    }

    @Override
    public String toString() {
        return modelName; //To change body of generated methods, choose Tools | Templates.
    }
}
