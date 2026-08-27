package cs2110;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class IsValidGuessTest {

    /* *******************************************************************************************
     * The code at the top of this file is used to capture the console output, so we can check   *
     * that it is correct.                                                                       *
     ******************************************************************************************* */

    /**
     * The original `System.out`.
     */
    PrintStream systemOut;

    /**
     * Replacement for `System.out` during test execution.
     */
    PrintStream out;
    ByteArrayOutputStream outBytes;

    @BeforeEach
    void setUpSimulator() {
        outBytes = new ByteArrayOutputStream();
        out = new PrintStream(outBytes);
        systemOut = System.out;
        System.setOut(out);
        clearOutputStream();
    }

    /**
     * Resets the output stream so we can capture the print output from processing one command
     */
    void clearOutputStream() {
        out.flush();
        outBytes.reset();
    }

    @AfterEach
    void restoreOutput() {
        out.close();
        System.setOut(systemOut);
    }

    /**
     * Asserts that the line captured in the OutputStream is equal to `expected`, including a 
     * trailing newline character.
     */
    void assertOutput(String expected) {
        out.flush();
        assertEquals(expected + System.lineSeparator(), outBytes.toString());
        outBytes.reset();
    }

    /**
     * Asserts that no console output has been captured in the OutputStream.
     */
    void assertNoOutput() {
        out.flush();
        assertEquals("", outBytes.toString());
        outBytes.reset();
    }

    /* *******************************************************************************************
     * Here is where the tests begin.
     ******************************************************************************************* */

    @DisplayName("WHEN a valid guess is made with `codeLength == 4` and `alphabetSize == 6`, THEN "
            + "`isValidGuess()` returns `true` and nothing is printed.")
    @Test
    void testValidGuessDefaultParams() {
        boolean b = Mastermind.isValidGuess("1234", 4, 6);
        assertTrue(b);
        assertNoOutput();
    }

    @DisplayName("WHEN a guess is made with too few symbols, THEN `isValidGuess()` returns "
            + "`false` and prints the correct error message.")
    @Test
    void testGuessTooShort() {
        boolean b = Mastermind.isValidGuess("123", 4, 6);
        assertFalse(b);
        assertOutput("Your guess must have 4 symbols. Try again.");
    }

    @DisplayName("WHEN a guess is made with a non-digit symbol, THEN `isValidGuess()` returns "
            + "`false` and prints the correct error message.")
    @Test
    void testGuessNonDigit() {
        boolean b = Mastermind.isValidGuess("12E4", 4, 6);
        assertFalse(b);
        assertOutput("Your guess cannot include the symbol 'E'. Try again.");
    }

    @DisplayName("WHEN a guess is made with a digit not in the allowed subset of digit characters "
            + "based on the value of `alphabetLength`, THEN `isValidGuess()` returns "
            + "`false` and prints the correct error message.")
    @Test
    void testGuessInvalidDigit() {
        boolean b = Mastermind.isValidGuess("54321", 5, 4);
        assertFalse(b);
        assertOutput("Your guess cannot include the symbol '5'. Try again.");
    }

    @DisplayName("WHEN a guess is made with too many symbols, THEN `isValidGuess()` returns "
            + "`false` and prints the correct error message.")
    @Test
    void testGuessTooLong() {
        boolean b = Mastermind.isValidGuess("123456789", 8, 9);
        assertFalse(b);
        assertOutput("Your guess must have 8 symbols. Try again.");
    }

    @DisplayName("WHEN a valid guess is made with `alphabetSize == 10`, THEN "
            + "`isValidGuess()` returns `true` and nothing is printed.")
    @Test
    void testAlphabetSizeTen() {
        boolean b = Mastermind.isValidGuess("123456789032", 12, 10);
        assertTrue(b);
    }

    @DisplayName("WHEN a valid guess is made with an `alphabetSize` less than 10 , THEN "
            + "`isValidGuess()` returns `true` and nothing is printed.")
    @Test
    void testAlphabetSizeLessTen() {
        boolean b = Mastermind.isValidGuess("111111", 6, 1);
        assertTrue(b);

        b = Mastermind.isValidGuess("121212", 6, 2);
        assertTrue(b);

        b = Mastermind.isValidGuess("123456789", 9, 9);
        assertTrue(b);
    }

//    test zeros

        // TODO 2: Add additional test cases to cover the specifications of the `isValidGuess()`
        //  method. All of your tests should include descriptive @DisplayNames and method names.
}
