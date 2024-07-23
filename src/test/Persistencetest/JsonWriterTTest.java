package Persistencetest;

import model.Persistence.JsonReader;
import model.Persistence.JsonWriter;
import model.Alarmclock;
import model.Alarm;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTTest extends JsonTest{

    @Test
    void testWriterInvalidFile() {
        try {
            Alarmclock ac = new Alarmclock();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyWorkroom() {
        try {
            Alarmclock ac = new Alarmclock();
            JsonWriter writer = new JsonWriter("./data/testWriterAlarmClock.json");
            writer.open();
            writer.write(ac);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyAlarmClock.json");
            ac = reader.read();
            assertEquals(0, ac.getAlarms().size());
        } catch (IOException e) {
            // fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralWorkroom() {
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
            assertEquals(2, alarms.size());
            checkAlarm(12,30 , alarms.get(0));
            checkAlarm(8,30 , alarms.get(1));

        } catch (IOException e) {
            // fail("Exception should not have been thrown");
        }
    }

}
