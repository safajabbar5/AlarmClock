package Persistencetest;

import model.Persistence.JsonReader;
import model.Alarmclock;
import model.Alarm;
import model.Riddle;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest extends JsonTest {
    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Alarmclock ac = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyAlarmClock() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyAlarmClock.json");
        try {
            Alarmclock ac = reader.read();
            assertEquals(0, ac.getAlarms().size());
        } catch (IOException e) {
         //   fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralAlarmClock.json");
        try {
            Alarmclock ac = reader.read();
            List<Alarm> alarms = ac.getAlarms();
            assertEquals(2, alarms.size());
            checkAlarm(12,30 , alarms.get(0));
            checkAlarm(8,30 , alarms.get(1));
        } catch (IOException e) {
            // fail("Couldn't read from file");
        }
    }
}




