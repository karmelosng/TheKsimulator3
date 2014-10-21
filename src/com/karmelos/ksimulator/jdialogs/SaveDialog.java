/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
public class SaveDialog extends JDialog{
   
    private JLabel label;
    private JPanel panel1;
    private JPanel panel2;
    private JButton saveButton;
    private JButton dontButton;
    private JButton cancelButton;
    private String label1;
    private int keyOp;
    
    ImageIcon icon = new ImageIcon(getClass().getResource("/com/karmelos/ksimulator/2ndbaricon/kicon.png"));
    public SaveDialog(Frame owner){
    super(owner);
    }
    
    public String getLabel1() {
        return label1;
    }

    public int getKeyOp() {
        return keyOp;
    }

    public void setKeyOp(int keyOp) {
        this.keyOp = keyOp;
    }

    public void setLabel1(String label1) {
        this.label1 = label1;
    }
    
    public int showDialog(){
    label = new JLabel(getLabel1());
    label.setFont(new Font("trebuchet ms", Font.PLAIN, 12));
    panel1 = new JPanel();
    panel1.add(label);
    
    ButtonHandler handler = new ButtonHandler();
    
    saveButton = new JButton("   Save   ");
    saveButton.setFont(new Font("trebuchet ms", Font.PLAIN, 12));
    saveButton.addActionListener(handler);
    dontButton = new JButton("Don't Save");
    dontButton.setFont(new Font("trebuchet ms", Font.PLAIN, 12));
    dontButton.addActionListener(handler);
    cancelButton = new JButton("  Cancel  ");
    cancelButton.setFont(new Font("trebuchet ms", Font.PLAIN, 12));
    cancelButton.addActionListener(handler);
    
    panel2 = new JPanel();
    panel2.add(saveButton);
    panel2.add(dontButton);
    panel2.add(cancelButton);
    
        add(panel1);
        add(panel2,BorderLayout.SOUTH);
        
        setModal(true);
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setModalExclusionType(Dialog.ModalExclusionType.NO_EXCLUDE);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(icon.getImage());
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setTitle("KSimulator");
        setVisible(true);
    return getKeyOp();
    }
    
    class ButtonHandler implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("   Save   ")){
                setKeyOp(1);
                SaveDialog.this.dispose();
            }
            else if(e.getActionCommand().equals("Don't Save")){
                setKeyOp(2);
                SaveDialog.this.dispose();
            }
            else if(e.getActionCommand().equals("  Cancel  ")){
                setKeyOp(3);
                SaveDialog.this.dispose();
            }
        }
    }
}
