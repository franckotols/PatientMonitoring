package com.francesco.patientmonitoring;

/**
 * Created by Fra on 20/09/2016.
 */
public class Pazienti {

    private String username, name;

    public Pazienti(String username, String name) {
        this.username = username;
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
