/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.karmelos.ksimulator.view.swing;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jumoke
 */
public class ConfigWork {
  public static void main(String [] args){
  
  System.out.print(""+ retrieveServerAddress());
  
  }
  public static String ping(String url) {
     String availability="";
      HttpURLConnection connection = null;
    try {
        URL u = new URL(url);
        connection = (HttpURLConnection) u.openConnection();
        connection.setRequestMethod("HEAD");
        int code = connection.getResponseCode();
        if(code == 200){
        System.out.print(code);
        availability = "true";
        }
        else{
            availability = "false";
        }
        //  200 is success.
    } catch (Exception e) {
        // TODO Auto-generated catch block
         availability = "false";
    } finally {
        if (connection != null) {
            connection.disconnect();
        }
    }
    return availability;
}
   public static String retrieveServerAddress() {
       String serveradd="ff"; 
       try {
            Class.forName("com.mysql.jdbc.Driver");       
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ksimulator","root","");       
            Statement st = con.createStatement();
            String query = "SELECT * from settings";
            //PreparedStatement prest = con.prepareStatement(query);
            //prest.setString(1, "server_address");
            
            ResultSet rs = st.executeQuery(query);
            rs.last(); System.out.print(rs.getRow());
            while(rs.next()){
          serveradd= rs.getString("value");
            }
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SimView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SimView.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return serveradd;
    }
}
