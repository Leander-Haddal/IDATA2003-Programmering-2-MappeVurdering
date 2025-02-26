package no.ntnu;
import java.util.Random;

/**
 * Represents a single die with a configurable number of faces (default 6).
 */
public class Die {
    private final int faces;
    private final Random random;

    public Die() {
        this(6); // Default to 6 faces
    }

    public Die(int faces) {
        this.faces = faces;
        this.random = new Random();
    }

    /**
     * Rolls the die and returns a random number between 1 and faces.
     */
    public int roll() {
        return 1 + random.nextInt(faces);
    }

    public int getFaces() {
        return faces;
    }
}