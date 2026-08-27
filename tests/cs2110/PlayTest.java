package cs2110;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PlayTest {

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

    /* *******************************************************************************************
     * Here is where the tests begin.
     ******************************************************************************************* */

    @DisplayName("WHEN the `play()` method is called, THEN the user receives the correct input "
            + "prompt.")
    @Test
    void testCorrectPrompt() {
        Mastermind.play(new Scanner("1234\n"), "1234", 6, 8, false);
        out.flush();
        String prompt = outBytes.toString().split("\uD83D\uDD34")[0];
        assertEquals("1. Enter a guess: ", prompt);
    }

    @DisplayName("WHEN the `play()` method is called AND the user guesses the correct code, "
            + "THEN the correct win message is printed.")
    @Test
    void testWinMessage() {
        // 1 guess
        Mastermind.play(new Scanner("1234\n"), "1234", 6, 8, false);
        out.flush();
        String message = outBytes.toString().split(System.lineSeparator())[1];
        assertEquals("Congratulations! You won in 1 guesses.", message);
        outBytes.reset();

        // 2 guesses
        Mastermind.play(new Scanner("1234\n1235\n"), "1235", 6, 8, false);
        out.flush();
        message = outBytes.toString().split(System.lineSeparator())[2];
        assertEquals("Congratulations! You won in 2 guesses.", message);
    }

    @DisplayName("WHEN the `play()` method is called AND the user runs out of guesses before they "
            + "guess the code, THEN the correct loss message is printed.")
    @Test
    void testLossMessage() {
        Mastermind.play(new Scanner("1111\n2222\n3333\n"), "4444", 6, 3, false);
        out.flush();
        String message = outBytes.toString().split(System.lineSeparator())[3];
        assertEquals("Better luck next time. The code was 4444.", message);
    }

    /**
     * Returns a String array whose entries are lines of the console from `play()`.
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

    @DisplayName("WHEN the `play()` method is called AND the user makes an invalid guess, THEN "
            + "the correct message is printed AND they are prompted to enter a new guess without "
            + "the guess number increasing.")
    @Test
    void testInvalidGuess() {
        // 1 guess, 2 attempts
        String inputs = "12345\n1234";
        Mastermind.play(new Scanner(inputs), "1234", 6, 8, false);
        String[] expected = {
                "1. Enter a guess: 12345",
                "Your guess must have 4 symbols. Try again.",
                "1. Enter a guess: 1234",
                "\uD83D\uDD34 \uD83D\uDD34 \uD83D\uDD34 \uD83D\uDD34 ",
                "Congratulations! You won in 1 guesses."
        };
        assertArrayEquals(expected, reconstructConsole(inputs));
        outBytes.reset();

        // 1 guess, 3 attempts
        inputs = "12345\n1237\n1234";
        Mastermind.play(new Scanner(inputs), "1234", 6, 8, false);
        expected = new String[]{
            "1. Enter a guess: 12345",
            "Your guess must have 4 symbols. Try again.",
            "1. Enter a guess: 1237",
            "Your guess cannot include the symbol '7'. Try again.",
            "1. Enter a guess: 1234",
            "\uD83D\uDD34 \uD83D\uDD34 \uD83D\uDD34 \uD83D\uDD34 ",
            "Congratulations! You won in 1 guesses."
        };
        assertArrayEquals(expected, reconstructConsole(inputs));
        outBytes.reset();
    }

    @DisplayName("A full winning game of Mastermind has the correct console outputs.")
    @Test
    @SuppressWarnings("UnnecessaryUnicodeEscape")
    void testFullGameWin() {
        String inputs = "1122\n2334\n2556\n6351\n5316";
        Mastermind.play(new Scanner(inputs), "5316", 6, 8, false);

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

    @DisplayName("A full losing game of Mastermind has the correct console outputs.")
    @Test
    @SuppressWarnings("UnnecessaryUnicodeEscape")
    void testFullGameLoss() {
        String inputs = "1234\n1523\n1362\n2546\n2356\n1122\n3344\n5136";
        Mastermind.play(new Scanner(inputs), "4532", 6, 8, false);

        String[] expected = new String[]{
                "1. Enter a guess: 1234",
                "\uD83D\uDD34 \u26AA \u26AA \u26AB ",
                "2. Enter a guess: 1523",
                "\uD83D\uDD34 \u26AA \u26AA \u26AB ",
                "3. Enter a guess: 1362",
                "\uD83D\uDD34 \u26AA \u26AB \u26AB ",
                "4. Enter a guess: 2546",
                "\uD83D\uDD34 \u26AA \u26AA \u26AB ",
                "5. Enter a guess: 2356",
                "\u26AA \u26AA \u26AA \u26AB ",
                "6. Enter a guess: 1122",
                "\uD83D\uDD34 \u26AB \u26AB \u26AB ",
                "7. Enter a guess: 3344",
                "\u26AA \u26AA \u26AB \u26AB ",
                "8. Enter a guess: 5136",
                "\uD83D\uDD34 \u26AA \u26AB \u26AB ",
                "Better luck next time. The code was 4532."
        };

        assertArrayEquals(expected, reconstructConsole(inputs));
    }
}
