package com.android.uccapp;

import java.util.Map;

public class User {
    private String index;
    private String firstName;
    private String lastName;
    private String password;
    private Map<String, String> user;
    private Map<String, String> pscsc170027;

    public User() {
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
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

    public Map<String, String> getUser() {
        return user;
    }

    public void setUser(Map<String, String> user) {
        this.user = user;
    }

    public Map<String, String> getPscsc170027() {
        return pscsc170027;
    }

    public void setPscsc170027(Map<String, String> pscsc170027) {
        this.pscsc170027 = pscsc170027;
    }
}
