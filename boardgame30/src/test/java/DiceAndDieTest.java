import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import no.ntnu.Dice;
import no.ntnu.Die;

/**
 * Tests for Die and Dice classes.
 */
public class DiceAndDieTest {
    
    @Test
    void testDieDefaultConstructor() {
        Die die = new Die();
        assertEquals(6, die.getFaces());
    }
    
    @Test
    void testDieCustomFaces() {
        Die die8 = new Die(8);
        Die die20 = new Die(20);
        
        assertEquals(8, die8.getFaces());
        assertEquals(20, die20.getFaces());
    }
    
    @Test
    void testDieRollRange() {
        Die die = new Die();
        
        for (int i = 0; i < 100; i++) {
            int roll = die.roll();
            assertTrue(roll >= 1 && roll <= 6,
                "Roll should be between 1 and 6, but was " + roll);
        }
    }
    
    @Test
    void testDieRollDistribution() {
        Die die = new Die(6);
        int[] counts = new int[7];
        
        for (int i = 0; i < 6000; i++) {
            counts[die.roll()]++;
        }
        
        for (int i = 1; i <= 6; i++) {
            assertTrue(counts[i] > 800 && counts[i] < 1200,
                "Face " + i + " appeared " + counts[i] + " times, expected ~1000");
        }
    }
    
    @Test
    void testDiceConstructor() {
        Dice dice = new Dice(2);
        assertEquals(2, dice.getDice().size());
        
        Dice singleDie = new Dice(1);
        assertEquals(1, singleDie.getDice().size());
        
        Dice manyDice = new Dice(5);
        assertEquals(5, manyDice.getDice().size());
    }
    
    @Test
    void testDiceRollAll() {
        Dice dice = new Dice(2);
        
        for (int i = 0; i < 100; i++) {
            int sum = dice.rollAll();
            assertTrue(sum >= 2 && sum <= 12,
                "Sum of 2 dice should be between 2 and 12, but was " + sum);
        }
    }
    
    @Test
    void testDiceRollAllSingleDie() {
        Dice dice = new Dice(1);
        
        for (int i = 0; i < 100; i++) {
            int sum = dice.rollAll();
            assertTrue(sum >= 1 && sum <= 6,
                "Single die should roll between 1 and 6, but was " + sum);
        }
    }
    
    @Test
    void testDiceRollAllMultipleDice() {
        Dice dice = new Dice(3);
        
        for (int i = 0; i < 100; i++) {
            int sum = dice.rollAll();
            assertTrue(sum >= 3 && sum <= 18,
                "Sum of 3 dice should be between 3 and 18, but was " + sum);
        }
    }
    
    @Test
    void testDiceGetDice() {
        Dice dice = new Dice(2);
        
        assertEquals(2, dice.getDice().size());
        
        Die die1 = dice.getDice().get(0);
        Die die2 = dice.getDice().get(1);
        
        assertNotNull(die1);
        assertNotNull(die2);
        assertNotSame(die1, die2);
    }
    
    @Test
    void testDiceDoubles() {
        Dice dice = new Dice(2);
        boolean foundDoubles = false;
        
        for (int i = 0; i < 100 && !foundDoubles; i++) {
            dice.rollAll();
            int die1 = dice.getDice().get(0).roll();
            int die2 = dice.getDice().get(1).roll();
            
            if (die1 == die2) {
                foundDoubles = true;
            }
        }
        
        assertTrue(foundDoubles, "Should have found at least one double in 100 attempts");
    }
}