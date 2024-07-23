package model;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import model.Persistence.Writable;

import java.time.LocalTime;



    // A class representing an alarm clock that shows the list of confirmed alarms, and compares alarm with local time  
public class Alarmclock implements Writable{
    private Alarm alarm;
    private List<Alarm> alarms;
    private LocalTime currentLocalhour;
    private LocalTime currentLocalmin;
    
    
    // EFFECTS: Sets the list of alarms, starts with an empty list of alarm
    // MODIFIES: this
    public Alarmclock() {
        this.alarms = new ArrayList<>();
        this.alarm = null;
        this.currentLocalhour = LocalTime.now();
        this.currentLocalmin = LocalTime.now();
       
    }

    
    // EFFECTS: set the current alarm to the provided alarm
    // MODIFIES: this
    public void setCurrentAlarm(Alarm alarm) {
        this.alarm = alarm;
    }

    
    // EFFECTS: add a new alarm to the list of alarms
    // MODIFIES: this
    public void addAlarm(Alarm alarm) {
        alarms.add(alarm);
        }



    // EFFECTS: if there is an alarm set is the same time as the local time, then alarm should ring
    // MODIFIES: this
    public Boolean alarmIsPlaying(Alarm alarm) {
    if (alarm.getHours() == currentLocalhour.getHour() && alarm.getMinutes() ==  currentLocalmin.getMinute()); {
    return true;
} 
}

    // EFFECTS: returns the current alarm that is set 
    public Alarm getRecentAlarm() {
        return alarm;
    }

    // EFFECTS: returns the current alarm list that is set 
     public List<Alarm> getAlarms() {
         return alarms;
     }


    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("alarms", alarmsToJson());
        return json;
    }

    private JSONArray alarmsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Alarm a: alarms) {
            jsonArray.put(a.toJson());
        }
        return jsonArray;
    }

    }




