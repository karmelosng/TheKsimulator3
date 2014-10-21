package com.karmelos.ksimulator.view.swing;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.karmelos.ksimulator.controller.SimController;
import com.karmelos.ksimulator.model.SimUser;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Fatimehin Dare
 */
public class ChangeUserDialog extends JDialog implements MouseListener{
private JPanel form;
private JLabel logout;
private JLabel login;
private JLabel usernameLabel;
private JLabel passwordLabel;
private JLabel errorlabel;
private JComboBox usernames;
private DefaultComboBoxModel usernameModel;
private JTextField passwordField;
private JButton changeUserBtn;  
private JButton cancelBtn;
private JButton logOutCurrentUser;
private JSeparator logoutSeperator;
private JSeparator loginSeperator;
private SimController simController;
ImageIcon iconLogin = new ImageIcon(getClass().getResource("/com/karmelos/ksimulator/2ndbaricon/login.png"));
ImageIcon iconLogout = new ImageIcon(getClass().getResource("/com/karmelos/ksimulator/2ndbaricon/logout.png"));
ImageIcon iconLoginButt = new ImageIcon(getClass().getResource("/com/karmelos/ksimulator/2ndbaricon/login.png"));
ImageIcon iconLogOutButt = new ImageIcon(getClass().getResource("/com/karmelos/ksimulator/2ndbaricon/logout.png"));
ImageIcon loginbig = new ImageIcon(getClass().getResource("/com/karmelos/ksimulator/2ndbaricon/loginbig.png"));
ImageIcon logoutbig = new ImageIcon(getClass().getResource("/com/karmelos/ksimulator/2ndbaricon/logoutbig.png"));
private SimView simViewer;
    public ChangeUserDialog(Frame frame, boolean bln) {
        super(frame, bln);
        usernameModel= new DefaultComboBoxModel();
    }
    public void setController(SimController sc){
    simController = sc;
    }
    public void setSimView(SimView sc){
    simViewer = sc;
    }
    public boolean showDialog(){
        form= new JPanel();
        form.setLayout(new GridLayout(0,1));
        form.addMouseListener(this);
        
        //labels
        login= new JLabel("Login");        
        logout = new JLabel("LogOut");
        login.setBorder(new EmptyBorder(0, 10, -5, 10));
        logout.setBorder(new EmptyBorder(0, 10, -5, 10));
        logout.setIcon(logoutbig);
        login.setIcon(loginbig);
        usernameLabel = new JLabel("Username::");
        passwordLabel= new JLabel("Password::");
        errorlabel= new JLabel("");
        errorlabel.setForeground(Color.RED);
        usernameLabel.setBorder(new EmptyBorder(0, 10, 0, 0));
        passwordLabel.setBorder(new EmptyBorder(0, 10, 0, 0));
        //btn
        changeUserBtn= new JButton("Change User");
        changeUserBtn.setIcon(iconLoginButt);
        changeUserBtn.addMouseListener(this);
        logOutCurrentUser= new JButton("Logout");
        logOutCurrentUser.setIcon(iconLogout);
        logOutCurrentUser.addMouseListener(this);
        // seperators
        loginSeperator= new JSeparator(JSeparator.HORIZONTAL);
        logoutSeperator = new JSeparator(JSeparator.HORIZONTAL);
        // comboBoxes
        usernames = new JComboBox();
        usernames.setModel(usernameModel);
        SimUser[] fetchSimUser = simController.fetchSimUser();
        loadUsers(fetchSimUser);
        //Textfield for password
       passwordField = new JPasswordField();
       passwordField.setBorder(new EmptyBorder(10, 0, 10, 0));
       passwordField.setFont(new Font("serif", Font.BOLD, 12));

   form.add(new JLabel("<html><u>                           Click Panel to Hide </u></html"));
   form.add(new JSeparator(JSeparator.HORIZONTAL));
   form.add(login);
   form.add(loginSeperator);
   form.add(usernameLabel);
   form.add(usernames);
   form.add(passwordLabel);
   form.add(passwordField);
   form.add(errorlabel);
   form.add(changeUserBtn);
    form.add(logout);
    form.add(logoutSeperator);
    form.add(logOutCurrentUser);
    add(form);
    setSize(350, 350);
        setModal(true);
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setModalExclusionType(Dialog.ModalExclusionType.NO_EXCLUDE);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(simViewer.getLoggedLabel());
        setUndecorated(true);
        setVisible(true);
    
    
     return false;
    }
    public void loadUsers(SimUser[] arrayState){
   
        for(SimUser s:arrayState){
    
         usernameModel.addElement(s);
    
       }
   
    
    }
    @Override
    public void mouseClicked(MouseEvent me) {
    Object source = me.getSource();
    if(!passwordField.getText().equalsIgnoreCase("")){
    // if password isnt entered
         if (source.equals(changeUserBtn)){
        SimUser loginCredentials = simController.login(usernames.getSelectedItem().toString(),passwordField.getText());
            
            if(loginCredentials != null){
            simController.changeUser(loginCredentials);
            dispose();
            }
            else{
             errorlabel.setText("Wrong Username/Password Combination");
            }
        }
         else if (source.equals(passwordField)){
            errorlabel.setText("");
        }
         else if (source.equals(logOutCurrentUser)){
           simController.closeState();
        }
         else if(source.equals(form)){
         dispose();
        }
    }
     else if(source.equals(form)){
         dispose();
        }
     else if(source.equals(logOutCurrentUser)){
       // goes back to system name
         simController.logOut(simViewer);
         dispose();
     }
    else{
         
       errorlabel.setText("Please Type a Password");
         
         }
    }

    @Override
    public void mousePressed(MouseEvent me) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent me) {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent me) {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
