package model;

import java.io.Serializable;

/**
 * Created by Tiago on 27/10/17.
 */

public class ResponseMood implements Serializable{

    private Mood mood;
    private Event event;
    private int workday;
    private String currentDate;
    private String eventText;

    public ResponseMood(Mood mood, Event event, int workday, String currentDate,String eventText) {
        this.mood = mood;
        this.event = event;
        this.workday = workday;
        this.currentDate = currentDate;
        this.eventText = eventText;
    }

    public Mood getMood() {
        return mood;
    }

    public void setMood(Mood mood) {
        this.mood = mood;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public int getWorkday() {
        return workday;
    }

    public void setWorkday(int workday) {
        this.workday = workday;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getEventText() {
        return eventText;
    }

    public void setEventText(String eventText) {
        this.eventText = eventText;
    }
}
