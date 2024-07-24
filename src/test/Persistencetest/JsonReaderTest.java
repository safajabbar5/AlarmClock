package persistencetest;

import model.Alarmclock;
import model.persistence.JsonReader;
import model.Alarm;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class JsonReaderTest extends JsonTest {
    
    @Test
    void testReaderNonExistentFile() {
        try {
            // reads a file that does nto exist
            JsonReader reader = new JsonReader("./data/noSuchFile.json");
            Alarmclock ac = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // test passes if the Exception is caught
        }
    }

   

    @Test
    void testReaderEmptyAlarmClock() { 
        Alarmclock ac = new Alarmclock(); // empty
        JsonReader reader = new JsonReader("./data/testReaderEmptyAlarmClock.json");
        try {
            //  reads data from file into alarm clock object
            ac = reader.read();
        } catch (IOException e) {
            e.getMessage();
        }
        assertEquals(0, ac.getAlarms().size()); // since alarm clock was empty
    }


    @Test
    void testReaderGeneralAlarmClock() {
        Alarmclock ac = new Alarmclock();
        List<Alarm> alarms = ac.getAlarms();
        try {
            ac.addAlarm(new Alarm(12, 30));
            ac.addAlarm(new Alarm(8, 45));
            JsonReader reader = new JsonReader("./data/testReaderGeneralAlarmClock.json");
            reader.read();
        } catch (IOException e) {
            e.getMessage();
        }
        assertEquals(2, alarms.size()); // since two alarms were added
    }
}






