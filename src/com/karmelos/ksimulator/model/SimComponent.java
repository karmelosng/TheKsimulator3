package com.karmelos.ksimulator.model;
import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.swing.ImageIcon;

@Entity
public class SimComponent implements Serializable {

    private static final long serialVersionUID = -3884151126049415788L;
    private Long id;
    private double scorePoint;
    private String componentName;
    private String description;
    private int overlayOrder;
    private boolean starter;
    private byte[] rawIconImage;
    private byte[] rawWireframeImage;
    private byte[] rawSolidImage;
    private byte[] rawDescriptionimage;
    private SimModule module;
    private Set<SimComponent> successors;    
    
    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }    
    @Lob
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOverlayOrder() {
        return overlayOrder;
    }

    public void setOverlayOrder(int overlayOrder) {
        this.overlayOrder = overlayOrder;
    }

    public boolean isStarter() {
        return starter;
    }

    public void setStarter(boolean starter) {
        this.starter = starter;
    }
    
    @Transient
    public ImageIcon getIconImage() throws IOException {
        InputStream in = new ByteArrayInputStream(rawIconImage);
        return new ImageIcon(ImageIO.read(in));
    }

    @Transient
    public Image getWireframeImage() throws IOException {
        InputStream in = new ByteArrayInputStream(rawWireframeImage);
        return ImageIO.read(in);
    }

    @Transient
    public Image getSolidImage() throws IOException {
       InputStream in = new ByteArrayInputStream(rawSolidImage);
        return ImageIO.read(in);
    }
    @Transient
    public ImageIcon getDescriptionIconImage() throws IOException{
      InputStream in = new ByteArrayInputStream(rawDescriptionimage);
        return new ImageIcon(ImageIO.read(in));
    }

    
    @Transient
    public Image getThreedImageBackIO() throws IOException{
    InputStream in= new ByteArrayInputStream(rawIconImage);
    return ImageIO.read(in);
    }
     @Column(length = 15000)
     @Lob
     @Basic(fetch = FetchType.LAZY)
    public byte[] getRawDescriptionimage() {
        return rawDescriptionimage;
    }

    public void setRawDescriptionimage(byte[] rawDescriptionimage) {
        this.rawDescriptionimage = rawDescriptionimage;
    }
  
    @Column(length = 15000)
     @Lob
     @Basic(fetch = FetchType.LAZY)
    public byte[] getRawIconImage() {
        return rawIconImage;
    }

    public void setRawIconImage(byte[] rawIconImage) {
        this.rawIconImage = rawIconImage;
    }
    
    @Column(length = 15000)
     @Lob
     @Basic(fetch = FetchType.LAZY)
    public byte[] getRawWireframeImage() {
        return rawWireframeImage;
    }

    public void setRawWireframeImage(byte[] rawWireframeImage) {
        this.rawWireframeImage = rawWireframeImage;
    }
    
    
    @Column(length = 15000)
     @Lob
     @Basic(fetch = FetchType.LAZY)
    public byte[] getRawSolidImage() {
        return rawSolidImage;
    }

    public void setRawSolidImage(byte[] rawSolidImage) {
        this.rawSolidImage = rawSolidImage;
    }

    @ManyToOne
    public SimModule getModule() {
        return module;
    }

    public void setModule(SimModule model) {
        this.module = model;
    }
     
   
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "Successors")   
    public Set<SimComponent> getSuccessors() {
        return successors;
    }

    public void setSuccessors(Set<SimComponent> nonDependents) {
        this.successors = nonDependents;
    }
    
    public void setPersistentSuccessors(List<SimComponent> successorList){
       if (successorList != null){
        this.successors= new HashSet<SimComponent>(successorList);
       }
   
    
    }
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SimComponent other = (SimComponent) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }
  
    public double getScorePoint() {
        return scorePoint;
    }

    public void setScorePoint(double scorePoint) {
        this.scorePoint = scorePoint;
    }
    
    @Override
    public String toString() {
        return getComponentName();
    }
}
