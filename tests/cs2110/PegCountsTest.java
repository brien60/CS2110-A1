package cs2110;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PegCountsTest {

    @DisplayName("WHEN the correct code is guessed, THEN the returned array should have the "
            + "code length at index 0 (all red pegs) and 0 at index 1 (no white pegs).")
    @Test
    void testCorrectGuess() {
        assertArrayEquals(new int[]{4, 0}, Mastermind.pegCounts("1234", "1234"));
        assertArrayEquals(new int[]{6, 0}, Mastermind.pegCounts("123456", "123456"));
    }

    @DisplayName("WHEN the guess does not share any digits with the code, THEN the returned array "
            + "should contain 0s at both indices.")
    @Test
    void testNoPegs() {
        assertArrayEquals(new int[]{0, 0}, Mastermind.pegCounts("1234", "5566"));
    }

    @DisplayName("WHEN the guess shares one symbol in the same position with the code and disagrees "
            + "on all other symbols, THEN the returned array should have 1 at index 0 (one red peg) "
            + "and 0 at index 1 (no white pegs).")
    @Test
    void testOneRedPeg() {
        assertArrayEquals(new int[]{1, 0}, Mastermind.pegCounts("1234", "1566"));
        assertArrayEquals(new int[]{1, 0}, Mastermind.pegCounts("1234", "5266"));
        assertArrayEquals(new int[]{1, 0}, Mastermind.pegCounts("1234", "5536"));
        assertArrayEquals(new int[]{1, 0}, Mastermind.pegCounts("1234", "5564"));
    }

    @DisplayName("WHEN the guess results in one red peg and one white peg, THEN the correct peg "
            + "counts array is returned.")
    @Test
    void testAllColors() {
        assertArrayEquals(new int[]{1, 1}, Mastermind.pegCounts("1234", "1562"));
    }

    @DisplayName("WHEN the guess results in one red peg, and in a different position of the code or "
            + "guess there is the same symbol as the matching symbols connected by the red peg, "
            + "THEN the correct pegs counts array is returned.")
    @Test
    void testOneRedPegNoWhitePeg() {
        assertArrayEquals(new int[]{1, 0}, Mastermind.pegCounts("1734", "1512"));
        assertArrayEquals(new int[]{1, 0}, Mastermind.pegCounts("1512", "1734"));
    }

    @DisplayName("WHEN the guess has all the same symbols as the code, but disagrees in position for "
            + "all of them, THEN the returned array should have the 0 at index 0 (no red pegs) and 0 "
            + "at index 1 (`codeLength` white pegs).")
    @Test
    void testAllWhitePegs() {
        assertArrayEquals(new int[]{0, 5}, Mastermind.pegCounts("12345", "34251"));
    }

    @Test
    void testRandomStuff() {
        assertArrayEquals(new int[]{2, 2}, Mastermind.pegCounts("1133", "3113"));
    }



        // TODO 4: Add additional test cases to cover the specifications of the `pegCounts()`
        //  method. All of your tests should include descriptive @DisplayNames and method names.
}
