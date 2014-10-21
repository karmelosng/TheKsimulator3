/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.karmelos.ksimulator.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;



/**
 *
 * @author MorpheuS
 */


@Entity
public class SimUser implements Serializable {
   
    private String username;
    
    private String password;
    
    private String firstName;
    
    private String middleName;
    
    private String lastName;

    @Id
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }    

    @Override
    public String toString() {
        return  username + "                            ";
    }

   
}
