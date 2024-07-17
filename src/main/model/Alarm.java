package model;

// A class representing a Alarm with a set time in hours and mintues
public class Alarm {
   
    private int alarm;
    private int min;   
    private int hrs;

    // EFFECTS: constructs a non-set alarm object
    public Alarm(Integer hrs, Integer min)   {  
        this.hrs = hrs;
        this.min = min;
    }

    // EFFECTS: sets the alarm to the provided hour
    // MODIFIES: this  
    public void setHour(Integer hour) {
        if (23 >= hour && 0 <= hour) {
        this.hrs = hour;
        } 
    }

    // EFFECTS: sets the alarm to the provided mintues
    // MODIFIES: this  
    public void setMinutes(Integer minutes) {
        if (59 >= minutes && 0 <= minutes) {
            this.min = minutes;
            } 
        }

    // EFFECTS: return the set alarm
    public int getAlarm() {
    return alarm;
    }

    // EFFECTS: return the set hour of the alarm
    public int getHours() {
        return hrs;
        }
    
    // EFFECTS: return the mintues of the alarm
    public int getMinutes() {
        return min;
        }

    





}
