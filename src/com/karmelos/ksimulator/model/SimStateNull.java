/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.karmelos.ksimulator.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

/**
 *
 * @author Dare Fatimehin
 */
// Dummy Class to handle unstarted State issues, i.e  Changing SimUser when state isnt started
public class SimStateNull extends Observable{
    private Settings settingModel;

    public SimStateNull() {
        this.settingModel = new Settings();
    }
    
    
    @Override
    public void setChanged() {
        super.setChanged();
    }
}
