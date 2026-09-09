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

        b = Mastermind.isValidGuess("", 4, 6);
        assertFalse(b);
        assertOutput("Your guess must have 4 symbols. Try again.");

        b = Mastermind.isValidGuess("1", 10, 6);
        assertFalse(b);
        assertOutput("Your guess must have 10 symbols. Try again.");
    }

    @DisplayName("WHEN a guess is made with a non-digit symbol, THEN `isValidGuess()` returns "
            + "`false` and prints the correct error message.")
    @Test
    void testGuessNonDigit() {
        boolean b = Mastermind.isValidGuess("12E4", 4, 6);
        assertFalse(b);
        assertOutput("Your guess cannot include the symbol 'E'. Try again.");

        b = Mastermind.isValidGuess("12a4", 4, 9);
        assertFalse(b);
        assertOutput("Your guess cannot include the symbol 'a'. Try again.");

        b = Mastermind.isValidGuess("12!4", 4, 10);
        assertFalse(b);
        assertOutput("Your guess cannot include the symbol '!'. Try again.");
    }

    @DisplayName("WHEN a guess is made with a digit not in the allowed subset of digit characters "
            + "based on the value of `alphabetLength`, THEN `isValidGuess()` returns "
            + "`false` and prints the correct error message.")
    @Test
    void testGuessInvalidDigit() {
        boolean b = Mastermind.isValidGuess("54321", 5, 4);
        assertFalse(b);
        assertOutput("Your guess cannot include the symbol '5'. Try again.");

        b = Mastermind.isValidGuess("1230", 4, 9);
        assertFalse(b);
        assertOutput("Your guess cannot include the symbol '0'. Try again.");

        b = Mastermind.isValidGuess("6789", 4, 5);
        assertFalse(b);
        assertOutput("Your guess cannot include the symbol '6'. Try again.");

        b = Mastermind.isValidGuess("4785", 4, 5);
        assertFalse(b);
        assertOutput("Your guess cannot include the symbol '7'. Try again.");

        b = Mastermind.isValidGuess("4589", 4, 5);
        assertFalse(b);
        assertOutput("Your guess cannot include the symbol '8'. Try again.");
    }

    @DisplayName("WHEN a guess is made with too many symbols, THEN `isValidGuess()` returns "
            + "`false` and prints the correct error message.")
    @Test
    void testGuessTooLong() {
        boolean b = Mastermind.isValidGuess("123456789", 8, 9);
        assertFalse(b);
        assertOutput("Your guess must have 8 symbols. Try again.");

        b = Mastermind.isValidGuess("1", 0, 9);
        assertFalse(b);
        assertOutput("Your guess must have 0 symbols. Try again.");

        b = Mastermind.isValidGuess("12A", 4, 6);
        assertFalse(b);
        assertOutput("Your guess must have 4 symbols. Try again.");
    }

    @DisplayName("WHEN a valid guess is made with `alphabetSize == 10`, THEN "
            + "`isValidGuess()` returns `true` and nothing is printed.")
    @Test
    void testAlphabetSizeTen() {
        boolean b = Mastermind.isValidGuess("123456789032", 12, 10);
        assertTrue(b);
        assertNoOutput();
    }

    @DisplayName("WHEN a valid guess is made with an `alphabetSize` less than 10 , THEN "
            + "`isValidGuess()` returns `true` and nothing is printed.")
    @Test
    void testAlphabetSizeLessTen() {
        boolean b = Mastermind.isValidGuess("123455", 6, 5);
        assertTrue(b);
        assertNoOutput();

        b = Mastermind.isValidGuess("141414", 6, 4);
        assertTrue(b);
        assertNoOutput();

        b = Mastermind.isValidGuess("123456789", 9, 9);
        assertTrue(b);
        assertNoOutput();
    }

    @DisplayName("WHEN a guess is made with a space character, THEN "
            + "`isValidGuess()` returns `false` and nothing is printed.")
    @Test
    void testSpaceCharacter() {
        boolean b = Mastermind.isValidGuess("4 66464", 6, 4);
        assertFalse(b);

        b = Mastermind.isValidGuess(" 464646", 6, 4);
        assertFalse(b);

        b = Mastermind.isValidGuess("464646 ", 6, 4);
        assertFalse(b);
    }



}
