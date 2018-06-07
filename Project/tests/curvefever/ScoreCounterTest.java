package curvefever;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class ScoreCounterTest {
    static ScoreCounter scoreCounterTestObject;

    @BeforeAll
    static void setUp() {
        scoreCounterTestObject = new ScoreCounter(1);
    }

    @Test
    void testIncreaseScore() {
        scoreCounterTestObject.increseScore(0);
        scoreCounterTestObject.increseScore(0);
        assertEquals(2, (int) scoreCounterTestObject.getScores().get(0));
    }
}
