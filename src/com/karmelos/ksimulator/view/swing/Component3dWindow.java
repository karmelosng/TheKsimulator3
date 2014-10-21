/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.karmelos.ksimulator.view.swing;

import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.karmelos.ksimulator.model.SimComponent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.HeadlessException;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 *
 * @author jumoke
 */
public class Component3dWindow extends JFrame implements  WindowListener{
  private LwjglAWTCanvas lac;
  private List<SimComponent> listing;
    public Component3dWindow(List<SimComponent> listed) throws HeadlessException, IOException {
        this.setIconImage(ImageIO.read(this.getClass().getResource("/com/karmelos/ksimulator/2ndbaricon/kicon.png")));
        this.setTitle("3D-View");
        
           listing= listed;
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.setBackground(Color.BLACK);
        lac= new LwjglAWTCanvas(new Component3dApplication(listing), true);
        contentPane.add(lac.getCanvas(),BorderLayout.CENTER);
        
        pack();
        setVisible(true);       
        setSize(600, 500); 
        setLocationRelativeTo(null);
    }

    @Override
    public void windowOpened(WindowEvent we) {
       
    }

    @Override
    public void windowClosing(WindowEvent we) {
        
       //this.setVisible(false);
    }

    @Override
    public void windowClosed(WindowEvent we) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowIconified(WindowEvent we) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowDeiconified(WindowEvent we) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowActivated(WindowEvent we) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowDeactivated(WindowEvent we) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
 
}
