package curvefever;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    static Player playerTestObject;

    private final double doubleDelta = 0.000001;

    @BeforeAll
    static void setUp() {
        playerTestObject = new Player(Color.RED, KeyCode.K, KeyCode.C, new Point(1, 2), 1.0);
    }

    @Test
    void testGetLeftKey() {
        assertEquals(playerTestObject.getLeftKey(), KeyCode.K);
    }

    @Test
    void testGetRightKey() {
        assertEquals(playerTestObject.getRightKey(), KeyCode.C);
    }

    @Test
    void testGetColor() {
        assertEquals(playerTestObject.getColor(), Color.RED);
    }

    @Test
    void testSetSpeed() {
        final double speed = 10;
        playerTestObject.setSpeed(speed);
        assertEquals(playerTestObject.getSpeed(), speed, this.doubleDelta);
    }

    @Test
    void testSetIsDead() {
        playerTestObject.setIsDead(true);
        assertEquals(playerTestObject.getIsDead(), true);
    }

    @Test
    void testGetAngle() {
        assertEquals(playerTestObject.getAngle(), 1.0, this.doubleDelta);
    }

    @Test
    void testMakeStep() {
        Player player = new Player(Color.RED, KeyCode.K, KeyCode.C, new Point(10.0, 10.0), 0.0);
        player.setSpeed(10.0);
        player.makeStep();
        assertEquals(20.0, player.getPositions().y, this.doubleDelta);
    }

    @Test
    void testHandleKey() {
        double startingAngle = playerTestObject.getAngle();
        KeyCode key = playerTestObject.getLeftKey();
        playerTestObject.handleKey(key);
        assertEquals(startingAngle + 2.0, playerTestObject.getAngle(), this.doubleDelta);
    }

    @Test
    void testCatchBonus() {
        playerTestObject.catchBonus(BonusType.changeSize);
        assertEquals(CurveFeverConsts.BONUS_SIZE_OF_PLAYER, playerTestObject.getSize());
    }
}