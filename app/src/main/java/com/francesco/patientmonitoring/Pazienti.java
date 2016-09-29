package com.francesco.patientmonitoring;

/**
 * Created by Fra on 20/09/2016.
 */
public class Pazienti {

    private String username, name, birthdate;

    public Pazienti(String username, String name, String birthdate) {
        this.username = username;
        this.name = name;
        this.birthdate = birthdate;
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

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {

        this.birthdate = birthdate;
    }
}
