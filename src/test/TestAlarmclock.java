import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Alarm;
import model.Alarmclock;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TestAlarmclock {

    private Alarmclock testAlarmClock;
    private List<Alarm> alarmList;
    private Alarm testa1;
    private Alarm testa2;
    private LocalDateTime currentLocalhour;
    private LocalDateTime currentLocalmin;

    @BeforeEach
    void runBefore() {

        testAlarmClock = new Alarmclock();
        alarmList = new ArrayList<>();
        testa1 = new Alarm(6, 30);
        testa2 = new Alarm(9, 30);
        this.currentLocalhour = LocalDateTime.now();
        this.currentLocalmin = LocalDateTime.now();

    }

    @Test
    void testConstructor() {
        assertNull(testAlarmClock.getRecentAlarm());

    }

    @Test
    void testsetCurrentAlarm() {
        testAlarmClock.setCurrentAlarm(testa1);
        assertEquals(testa1, testAlarmClock.getRecentAlarm());
        testAlarmClock.setCurrentAlarm(testa2);
        assertEquals(testa2, testAlarmClock.getRecentAlarm());

    }

    @Test
    void testaddAlarm() {
        testAlarmClock.addAlarm(testa1);
        testAlarmClock.addAlarm(testa2);
        alarmList = testAlarmClock.getAlarms();
        assertEquals(2, alarmList.size());
        assertEquals(testa1, alarmList.get(0));
        assertEquals(testa2, alarmList.get(1));

    }

    @Test
    void testalarmIsPlaying() {
        LocalTime now = LocalTime.now();
        Alarm currenttime = new Alarm(currentLocalhour.getHour(), currentLocalmin.getMinute());
        alarmList.add(currenttime);
        assertFalse(currenttime.equals(testa1));
        assertFalse(currenttime.equals(testa1));
       // assertTrue(testAlarmClock.alarmIsPlaying(currenttime));

    }

}
