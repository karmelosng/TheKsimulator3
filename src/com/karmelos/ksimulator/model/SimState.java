package com.karmelos.ksimulator.model;

//TODO use proper code format conventions (readable property and variable names etc) and other Java conventions

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Stack;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


@Entity
public class SimState extends Observable implements Serializable {

  
    private static final long serialVersionUID = -505800887487595246L;
    private Long id;
    private String description;
    private double score;
    private boolean globalAccessFlag;
    private boolean readWriteFlag;
    private Date savedAt;
    private SimUser simUser;
    private List<SimComponent> availableComponents;
    private Map<SimComponent,SimPoint> placedComponents = new LinkedHashMap();
    private Map<SimComponent,SimPoint> usedComponents= new LinkedHashMap();
    private List<SimComponent> undo;
    
    private Date timerStart;
    private Long timerDuration;
    private boolean timed;
    private String level;
    // until drag and drop starts 


    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Lob
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getGlobalAccessFlag() {
        return globalAccessFlag;
    }

    public void setGlobalAccessFlag(boolean globalAccessFlag) {
        this.globalAccessFlag = globalAccessFlag;
    }

    public boolean getReadWriteFlag() {
        return readWriteFlag;
    }

    public void setReadWriteFlag(boolean readWriteFlag) {
        this.readWriteFlag = readWriteFlag;
    } 

    @Temporal(TemporalType.TIMESTAMP)
    public Date getSavedAt() {
        return savedAt;
    }

    public void setSavedAt(Date savedAt) {
        this.savedAt = savedAt;
    }

    @ManyToOne
    public SimUser getSimUser() {
        return simUser;
    }

    public void setSimUser(SimUser simUser) {
        this.simUser = simUser;
    }
    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinTable(name = "Placed_Components")
    public Map<SimComponent, SimPoint> getPlacedComponents() {
        return placedComponents;
    }

    public void setPlacedComponents(Map<SimComponent, SimPoint> placedComponents) {
        this.placedComponents = placedComponents;
    }
     
    
    
    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinTable(name = "Available_Components")
    public List<SimComponent> getAvailableComponents() {
        return availableComponents;
    }

    public void setAvailableComponents(List<SimComponent> availableComponents) {
         this.availableComponents = new Stack();
        if(availableComponents != null) {
            this.availableComponents.addAll(availableComponents);
        }
    }
    
    
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "Used_Components")
    public Map<SimComponent, SimPoint> getUsedComponents() {
        return usedComponents;
    }

    
    
    public void setUsedComponents(Map<SimComponent,SimPoint> usedComponents) {
        this.usedComponents = usedComponents;
    }
  
    @Transient
    public List<SimComponent> getUndo() {
        return undo;
    }
   
    public void setUndo(List<SimComponent> undo) {
        this.undo = undo;
    }
   
    
    
    @Override
    public void setChanged() {
        super.setChanged();
    }

    @Override
    public String toString() {
        String paddedString;
        int lengthOfString = description.trim().length();
        if(lengthOfString >=20){ paddedString = description.substring(0,15);}
        else{
           int pad = 20-lengthOfString;
            paddedString =String.format("%-"+pad+"s", description);
        
        }
        return  paddedString+"||"  + savedAt;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getTimerStart() {
        return timerStart;
    }

    public void setTimerStart(Date timerStart) {
        this.timerStart = timerStart;
    }

    public Long getTimerDuration() {
        return timerDuration;
    }

    public void setTimerDuration(Long timerDuration) {
        this.timerDuration = timerDuration;
    }

    public boolean isTimed() {
        return timed;
    }

    public void setTimed(boolean timed) {
        this.timed = timed;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }


    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    
}
