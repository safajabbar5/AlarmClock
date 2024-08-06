import model.Alarm;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkAlarm(int hours, int minutes, Alarm alarm) {
        assertEquals(hours, alarm.getHours());
        assertEquals(minutes, alarm.getMinutes());
    }

}
