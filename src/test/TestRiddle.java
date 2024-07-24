import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Riddle;

public class TestRiddle {

    private Riddle testRiddle;

    @BeforeEach
    void runBefore() {
        testRiddle = new Riddle("this is hard", "true");

    }

    @Test
    void testConstructor() {
        assertEquals("this is hard", testRiddle.getQuestion());
        assertEquals("true", testRiddle.getAnswer());
    }

    @Test
    void testcheckRiddleAnswer() {
        assertTrue(testRiddle.checkRiddleAnswer("true"));
        assertFalse(testRiddle.checkRiddleAnswer("TRUE"));
        assertFalse(testRiddle.checkRiddleAnswer("10"));
    }

}
