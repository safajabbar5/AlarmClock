package model;


import java.util.List;
import java.time.LocalDateTime;



// EFFECTS: A class representing an alarm clock that shows the list of confirmed alarms  
public class Alarmclock {
    private Alarm alarm;
    private List<Alarm> alarms;
    private LocalDateTime currentLocalhour;
    private LocalDateTime currentLocalmin;
    
// MODIFIES: this
// EFFECTS: Sets the list of alarms,starts with an empty string
    public Alarmclock() {
        // alarms.isEmpty();
        this.alarm = null;
        this.currentLocalhour = LocalDateTime.now();
        this.currentLocalmin = LocalDateTime.now();
       
    }

// MODIFIES: this
    // EFFECTS: set the current alarm to the provided alarm

    public void setCurrentAlarm(Alarm alarm) {
        this.alarm = alarm;
    }

// MODIFIES: this
// EFFECTS: add a new alarm to the list
    public void addAlarm(Alarm alarm) {
        if (!alarms.contains(alarm)) {
            alarms.add(alarm);
        }
        System.out.println("You have added the same Alarm time again");
    }



// MODIFIES: this
// EFFECTS: if there is an alarm set then alarm should start playing 
public Boolean alarmIsPlaying(Alarm alarm) {
    if (alarm.getHours() == currentLocalhour.getHour() && alarm.getMinutes() ==  currentLocalmin.getMinute()); {
    return true;
} 
}

// MODIFIES: this
    // EFFECTS: returns the current alarm that is set 
    public Alarm getRecentAlarm() {
        System.out.println("Your alarm has been set to");
        return alarm;
    }

    public LocalDateTime getLocalHourTime() {
        return currentLocalhour;
    }

    public LocalDateTime getLocalHourMinTime() {
        return currentLocalmin;
    }

     public List<Alarm> getAlarms() {
        System.out.println("Confirmed alarms for the day are");
         return alarms;
     }

    }




