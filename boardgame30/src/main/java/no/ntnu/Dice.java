package no.ntnu;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a collection of dice (two 6-sided dice).
 */
public class Dice {
    private final List<Die> dice;

    public Dice(int numberOfDice) {
        dice = new ArrayList<>();
        for (int i = 0; i < numberOfDice; i++) {
            dice.add(new Die());
        }
    }

    /**
     * Rolls all dice and returns the total sum of faces.
     */
    public int rollAll() {
        int sum = 0;
        for (Die die : dice) {
            sum += die.roll();
        }
        return sum;
    }

    public List<Die> getDice() {
        return dice;
    }
}