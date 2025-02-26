import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import no.ntnu.Die;

/**
 * Tests for the Die class.
 */
public class DieTest {

    @Test
    void testConstructorDefaultFaces() {
        Die die = new Die();
        Assertions.assertEquals(6, die.getFaces(), 
            "Default die should have 6 faces.");
    }

    @Test
    void testConstructorCustomFaces() {
        Die die = new Die(10);
        Assertions.assertEquals(10, die.getFaces(), 
            "Die should have 10 faces when created with 10.");
    }

    @Test
    void testRollValueInRange() {
        Die die = new Die(); // 6 faces
        for (int i = 0; i < 50; i++) {
            int roll = die.roll();
            Assertions.assertTrue(roll >= 1 && roll <= 6, 
                "Roll should be in the range 1 to 6");
        }
    }

}
