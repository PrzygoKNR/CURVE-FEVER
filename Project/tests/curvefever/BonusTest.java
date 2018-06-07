package curvefever;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BonusTest {
    static Bonus bonusTestObject;

    private final double doubleDelta = 0.000001;

    @BeforeAll
    static void setUp() {
       bonusTestObject = new Bonus(1,2,BonusType.changeSize);
    }

    @Test
    void testPosition() {
        assertEquals(1, bonusTestObject.position.x);
        assertEquals(2, bonusTestObject.position.y);
    }
}
