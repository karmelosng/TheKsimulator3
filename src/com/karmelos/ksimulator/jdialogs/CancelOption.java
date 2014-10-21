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

/**
 *
 * @author Ables
 */
public class CancelOption extends JDialog{
    
JPanel panel1,panel2;
JButton cancelButton;
private String label1;

JLabel label;
ImageIcon icon = new ImageIcon(getClass().getResource("/com/karmelos/ksimulator/2ndbaricon/kicon.png"));
    public CancelOption(Frame owner, String title) {
        super(owner, title);
    
    }
    
    public void showDialog(){
    
         panel1 = new JPanel();
        panel2 = new JPanel();
        label = new JLabel(getLabel1());
        label.setFont(new Font("trebuchet ms", Font.PLAIN, 12));
        panel1.add(label);
       cancelButton = new JButton("Cancel");
       cancelButton.setFont(new Font("trebuchet ms", Font.PLAIN, 12));
       cancelButton.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent event){
       dispose();
       }
       });
       panel2.add(cancelButton);
       add(panel1);
       add(panel2,BorderLayout.SOUTH);
       
//        setSize(350, 130);
        setModal(true);
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setModalExclusionType(Dialog.ModalExclusionType.NO_EXCLUDE);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(icon.getImage());
        setVisible(true);
    }
//    public static void main(String[] args) {
//        new Ok_Option(null, "Drag Out Of Worspace Alert");
//    }

    public String getLabel1() {
        return label1;
    }

    public void setLabel1(String label1) {
        this.label1 = label1;
    }
}
