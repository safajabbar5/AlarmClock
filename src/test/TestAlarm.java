import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Alarm;

import static org.junit.jupiter.api.Assertions.*;

public class TestAlarm {

    private Alarm testAlarm1;
    private Alarm testAlarm2;
    
    @BeforeEach
    void runBefore() {
        testAlarm1 = new Alarm(5, 30);
        testAlarm2 = new Alarm(1,25);

    }

    @Test
    public void testConstructor() {
        assertEquals(5,testAlarm1.getHours());
        assertEquals(30,testAlarm1.getMinutes());
        assertEquals(1,testAlarm2.getHours());
        assertEquals(25, testAlarm2.getMinutes());
}

@Test
public void testsetHour() {
    testAlarm1.setHour(12);
    assertEquals(12,testAlarm1.getHours());
    testAlarm1.setHour(60);
    assertEquals(12, testAlarm1.getHours());
    testAlarm1.setHour(23);
    assertEquals(23,testAlarm1.getHours());
    testAlarm1.setHour(0);
    assertEquals(0,testAlarm1.getHours());
    testAlarm2.setHour(-10);
    testAlarm2.setHour(10);
    assertEquals(10,testAlarm2.getHours());
    
}


@Test
public void testsetMinutes() {
    testAlarm1.setMinutes(59);
    assertEquals(59,testAlarm1.getMinutes());
    testAlarm1.setMinutes(40);
    assertEquals(40,testAlarm1.getMinutes());
    testAlarm2.setMinutes(0);
    assertEquals(0,testAlarm2.getMinutes());
    testAlarm2.setMinutes(1);
    assertEquals(1,testAlarm2.getMinutes());
    testAlarm2.setMinutes(70);
    assertEquals(1,testAlarm2.getMinutes());
    testAlarm2.setMinutes(-10);
    assertEquals(1,testAlarm2.getMinutes());
}

}
