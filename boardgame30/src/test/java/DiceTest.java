import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import no.ntnu.Dice;

/**
 * Tests for the Dice class.
 */
public class DiceTest {

    @Test
    void testDiceConstructorAndRollAll() {
        // Create a Dice object with 2 dice (each 6-sided).
        Dice dice = new Dice(2);

        // Check if the Dice collection is of size 2.
        Assertions.assertEquals(2, dice.getDice().size(),
            "Dice should contain 2 individual dice.");

        // Roll it a few times and check sum range (2D6 => 2..12)
        for (int i = 0; i < 20; i++) {
            int rollSum = dice.rollAll();
            Assertions.assertTrue(rollSum >= 2 && rollSum <= 12,
                "Sum of 2 dice rolls should be between 2 and 12.");
        }
    }

    @Test
    void testMultipleDice() {
        // Create Dice with 3 dice (each 6-sided).
        Dice dice = new Dice(3);
        // 3D6 => sum range is 3..18
        for (int i = 0; i < 20; i++) {
            int rollSum = dice.rollAll();
            Assertions.assertTrue(rollSum >= 3 && rollSum <= 18,
                "Sum of 3 dice rolls should be between 3 and 18.");
        }
    }
}
