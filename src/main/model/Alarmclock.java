package model;


import java.util.ArrayList;
import java.util.List;


// A class representing an alarm clock that shows the lsit of alarms  
public class Alarmclock {

    private List<Alarmclock> alarms;

// Sets the list of alarms
    public Alarmclock(){
        alarms = new ArrayList<>();

    }

    public void addAlarm(Alarmclock alarm) {
        alarms.add(alarm);
    }

    public List<Alarmclock> getAlarams(){
        return alarms;
    }

}
