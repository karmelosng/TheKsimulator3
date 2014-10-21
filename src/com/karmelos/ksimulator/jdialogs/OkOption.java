/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.karmelos.ksimulator.jdialogs;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class OkOption extends JDialog{
JPanel panel1,panel2;
JButton okButton;
private String label1;
JLabel label;
ImageIcon icon = new ImageIcon(getClass().getResource("/com/karmelos/ksimulator/2ndbaricon/kicon.png"));
    public OkOption(Frame owner, String title) {
        super(owner, title);
       
    }
     public void setMessage(String msg){
    
    label1=msg;
    
    }
     public String getMessage(){
    
    return label1;
    
    }
    public void showDialog(){
    
         panel1 = new JPanel();
        panel2 = new JPanel();
        label = new JLabel(getLabel1());
        label.setFont(new Font("trebuchet ms", Font.PLAIN, 12));
        panel1.add(label);
       okButton = new JButton("Ok");
       okButton.setFont(new Font("trebuchet ms", Font.PLAIN, 12));
       okButton.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent event){
       dispose();
       }
       });
       panel2.add(okButton);
       add(panel1);
       add(panel2,BorderLayout.SOUTH);
       
//        setSize(450, 130);
        setModal(true);
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setModalExclusionType(Dialog.ModalExclusionType.NO_EXCLUDE);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(icon.getImage());
        setVisible(true);
    }
//    public static void main(String[] args) {
//        new OkOption(null, "Drag Out Of Worspace Alert");
//    }

    public String getLabel1() {
        return label1;
    }

    public void setLabel1(String label1) {
        this.label1 = label1;
    }
}
