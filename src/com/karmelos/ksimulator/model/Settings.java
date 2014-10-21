/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.karmelos.ksimulator.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

/**
 *
 * @author Dare fatimehin
 */
@Entity
public class Settings implements Serializable {
    private static final long serialVersionUID = 1L;    
    private String keyword;
    private String settingvalues;
    private boolean themeUsable;
    @Id
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    @Lob                              
    public String getSettingvalues() {
        return settingvalues;
    }

    public void setSettingvalues(String value) {
        this.settingvalues = value;
    }

    public boolean isThemeUsable() {
        return themeUsable;
    }

    public void setThemeUsable(boolean themeUsable) {
        this.themeUsable = themeUsable;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (keyword != null ? keyword.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the keyword fields are not set
        if (!(object instanceof Settings)) {
            return false;
        }
        Settings other = (Settings) object;
        if ((this.keyword == null && other.keyword != null) || (this.keyword != null && !this.keyword.equals(other.keyword))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.karmelos.ksimulator.model.Settings[ id=" + keyword + " ]";
    }
    
}
