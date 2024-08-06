package persistencetest;

import model.Alarmclock;
import model.Persistence.JsonReader;
import model.Persistence.JsonWriter;
import model.Alarm;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTTest extends JsonTest {
    

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open(); // open a writer with an illegal file name
            fail("IOException expected");
        } catch (IOException e) {
            // test passes if the Exception is caught
        }
    }

    @Test
    void testWriterEmptyAlarmClock() {
        Alarmclock ac = new Alarmclock();
        try {
           
            JsonWriter writer = new JsonWriter("./data/testWriterAlarmClock.json");
            writer.open(); // open to write data
            writer.write(ac); // empty alarm clock
            writer.close(); // close
           
        } catch (IOException e) {
            fail("IOException expected");
        }   
        assertEquals(0, ac.getAlarms().size()); 
        

    }

    @Test
    void testWriterGeneralAlarmClock() {
        try {
            Alarmclock ac = new Alarmclock();
            ac.addAlarm(new Alarm(12, 30));
            ac.addAlarm(new Alarm(8, 45));
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralAlarmClock.json");
            writer.open();
            writer.write(ac);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralAlarmClock.json");
            ac = reader.read();
            List<Alarm> alarms = ac.getAlarms();
            assertEquals(2, alarms.size()); // since there are two alarms
            checkAlarm(12,30, alarms.get(0));
            checkAlarm(8,45, alarms.get(1));

        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

}


