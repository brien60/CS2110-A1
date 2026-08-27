package cs2110;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class HardModeTest {

    /* *******************************************************************************************
     * The code at the top of this file is used to capture the console output, so we can check
     * that it is correct.
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
     * Returns a String array whose entries are lines of the console from `play()` or
     * `playHardMode()`.
     */
    String[] reconstructConsole(String inputs) {
        out.flush();
        String[] outLines = outBytes.toString().split(System.lineSeparator());
        String[] inLines = inputs.split("\n");
        assert outLines.length == inLines.length + 1;
        String[] console = new String[2 * outLines.length - 1];
        for (int i = 0; i < outLines.length - 1; i++) {
            int splitIndex = outLines[i].indexOf(":") + 2; // separate prompt from message
            console[2 * i] = outLines[i].substring(0, splitIndex) + inLines[i];
            console[2 * i + 1] = outLines[i].substring(splitIndex);
        }
        console[console.length - 1] = outLines[outLines.length - 1];
        return console;
    }

    /* *******************************************************************************************
     * Here is where the tests begin.
     ******************************************************************************************* */

    @DisplayName("A full winning game of Mastermind with no hard mode violations has the correct "
            + "console outputs in hard mode.")
    @Test
    @SuppressWarnings("UnnecessaryUnicodeEscape")
    void testFullGame() {
        String inputs = "1122\n2334\n2556\n6351\n5316";
        Mastermind.play(new Scanner(inputs), "5316", 6, 8, true);
        String[] expected = new String[]{
                "1. Enter a guess: 1122",
                "\u26AA \u26AB \u26AB \u26AB ",
                "2. Enter a guess: 2334",
                "\uD83D\uDD34 \u26AB \u26AB \u26AB ",
                "3. Enter a guess: 2556",
                "\uD83D\uDD34 \u26AA \u26AB \u26AB ",
                "4. Enter a guess: 6351",
                "\uD83D\uDD34 \u26AA \u26AA \u26AA ",
                "5. Enter a guess: 5316",
                "\uD83D\uDD34 \uD83D\uDD34 \uD83D\uDD34 \uD83D\uDD34 ",
                "Congratulations! You won in 5 guesses."
        };
        assertArrayEquals(expected, reconstructConsole(inputs));
    }

    @DisplayName("WHEN playing in normal mode, THEN conflicts with pegs from previous guesses should "
            + "not result in error messages AND the game should reveal the pegs and advance to the "
            + "next guess number.")
    @Test
    @SuppressWarnings("UnnecessaryUnicodeEscape")
    void testNormalModeAllowsContradictions() {
        String inputs = "1222\n1333\n3333";
        Mastermind.play(new Scanner(inputs), "3333", 6, 8, false);
        String[] expected = new String[]{
                "1. Enter a guess: 1222",
                "\u26AB \u26AB \u26AB \u26AB ",
                "2. Enter a guess: 1333",
                "\uD83D\uDD34 \uD83D\uDD34 \uD83D\uDD34 \u26AB ",
                "3. Enter a guess: 3333",
                "\uD83D\uDD34 \uD83D\uDD34 \uD83D\uDD34 \uD83D\uDD34 ",
                "Congratulations! You won in 3 guesses."
        };
        assertArrayEquals(expected, reconstructConsole(inputs));
        clearOutputStream();
    }

    @DisplayName("WHEN playing in hard mode AND a previous guess included no red or white pegs AND"
            + "a future guess includes one of the same digits, THEN the game displays the correct "
            + "error message and prompts the user for a new guess without increasing the "
            + "guess number.")
    @Test
    @SuppressWarnings("UnnecessaryUnicodeEscape")
    void testCantRepeatNoPegs() {
        // same position
        String inputs = "1222\n1333\n3333";
        Mastermind.play(new Scanner(inputs), "3333", 6, 8, true);
        String[] expected = new String[]{
                "1. Enter a guess: 1222",
                "\u26AB \u26AB \u26AB \u26AB ",
                "2. Enter a guess: 1333",
                "Your guess conflicts with information from the guess 1222. Try again.",
                "2. Enter a guess: 3333",
                "\uD83D\uDD34 \uD83D\uDD34 \uD83D\uDD34 \uD83D\uDD34 ",
                "Congratulations! You won in 2 guesses."
        };
        assertArrayEquals(expected, reconstructConsole(inputs));
        clearOutputStream();

        // different position
        inputs = "1222\n3133\n3333";
        Mastermind.play(new Scanner(inputs), "3333", 6, 8, true);
        expected = new String[]{
                "1. Enter a guess: 1222",
                "\u26AB \u26AB \u26AB \u26AB ",
                "2. Enter a guess: 3133",
                "Your guess conflicts with information from the guess 1222. Try again.",
                "2. Enter a guess: 3333",
                "\uD83D\uDD34 \uD83D\uDD34 \uD83D\uDD34 \uD83D\uDD34 ",
                "Congratulations! You won in 2 guesses."
        };
        assertArrayEquals(expected, reconstructConsole(inputs));
        clearOutputStream();
    }

    @DisplayName("WHEN playing in hard mode AND a previous guess included one red peg AND a future "
            + "carries down two digits, THEN the game displays the correct error message and "
            + "prompts the user for a new guess without increasing the guess number.")
    @Test
    @SuppressWarnings("UnnecessaryUnicodeEscape")
    void testCantCarryExtra() {
        String inputs = "1234\n1255\n2222";
        Mastermind.play(new Scanner(inputs), "2222", 6, 8, true);
        String[] expected = new String[]{
                "1. Enter a guess: 1234",
                "\uD83D\uDD34 \u26AB \u26AB \u26AB ",
                "2. Enter a guess: 1255",
                "Your guess conflicts with information from the guess 1234. Try again.",
                "2. Enter a guess: 2222",
                "\uD83D\uDD34 \uD83D\uDD34 \uD83D\uDD34 \uD83D\uDD34 ",
                "Congratulations! You won in 2 guesses."
        };
        assertArrayEquals(expected, reconstructConsole(inputs));
        clearOutputStream();
    }

    /*
     * FIXME: The tests that we provided in this file do not come close to covering the specs for
     *  hard mode. We encourage you to add additional test cases, using the ones from above as a
     *  guide, to improve the coverage and gain confidence that your code is correct. You will not
     *  submit these test cases.
     */
}
