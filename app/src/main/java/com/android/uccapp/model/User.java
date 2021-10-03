package com.android.uccapp.model;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

public class User implements Serializable {
    private UUID userId;
    private String registrationNumber;
    private String firstName;
    private String lastName;
    private String password;
    private boolean isAdmin;

    public User(){
//        this(this.getRegistrationNumber());
    }
    public User(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}