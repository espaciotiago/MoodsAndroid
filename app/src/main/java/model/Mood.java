package model;

import java.io.Serializable;

/**
 * Created by Tiago on 25/10/17.
 */

public class Mood implements Serializable {
    private String idInServer;
    private String mood;
    private int value;
    private int resource;

    public Mood(String idInServer,String mood, int value, int resource) {
        this.mood = mood;
        this.value = value;
        this.resource = resource;
        this.idInServer = idInServer;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    public String getIdInServer() {
        return idInServer;
    }

    public void setIdInServer(String idInServer) {
        this.idInServer = idInServer;
    }
}
