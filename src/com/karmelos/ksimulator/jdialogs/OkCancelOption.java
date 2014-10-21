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
public class OkCancelOption extends JDialog{
    JPanel  panel1;
JPanel panel2;
JButton okButton;
JButton cancelButton;
JLabel label;
private String label1;
boolean close;
int one;
ImageIcon icon = new ImageIcon(getClass().getResource("/com/karmelos/ksimulator/2ndbaricon/kicon.png"));
    public OkCancelOption(Frame owner, String title) {
        super(owner, title);
        //initialize components
   
    }

    public String getLabel1() {
        return label1;
    }

    public void setLabel1(String label1) {
        this.label1 = label1;
    }
    
    class ButtonHandler implements ActionListener{
        public void actionPerformed(ActionEvent event){
        if(event.getActionCommand().equals(" Ok  ")){
        one = 1;
            OkCancelOption.this.dispose();
        }
        else{
        one = 0;
        OkCancelOption.this.dispose();
        }
        }
    
    }
//    public static void main(String[] args) {
//        new CloseWindowDialog(null, null);
//    }
    public boolean showDialog(){
    
             panel1 = new JPanel();
        panel2 = new JPanel();
        
        label = new JLabel(getLabel1());
        label.setFont(new Font("trebuchet ms", Font.PLAIN, 12));
        panel1.add(label);
        
        //instantiates handler
        ButtonHandler handler = new ButtonHandler();
        //creating yesButton
        okButton = new JButton(" Ok  ");
        okButton.setFont(new Font("trebuchet ms", Font.PLAIN, 12));
        okButton.addActionListener(handler);
        
        //creating noButton;
        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("trebuchet ms", Font.PLAIN, 12));
        
        cancelButton.addActionListener(handler);
        
        panel2.add(okButton);
        panel2.add(cancelButton);
        //add panels to jdialog
        add(panel1);
        add(panel2,BorderLayout.SOUTH);
                
//        setSize(350, 130);
        setModal(true);
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setModalExclusionType(Dialog.ModalExclusionType.NO_EXCLUDE);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(icon.getImage());
        setVisible(true);
        
        if(one == 1){
        close = true;
        }
        else{
        close = false;
        }
        return close;
    }

}
