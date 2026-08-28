package cs2110;

import java.util.Random;
import java.util.Scanner;

/**
 * A console-based implementation of the popular Mastermind code-breaking game.
 */
public class Mastermind {

    /**
     * Returns `false` and prints an explanatory message if the given `guess` is not valid,
     * otherwise returns `true`. A guess is not valid if (1) it contains a number of symbols
     * different from the `codeLength`, in which case "Your guess must have # symbols. Try again."
     * is printed, with # replaced by the `codeLength`, or (2) it contains the correct number of
     * symbols, but one of them is not an allowed digit for the given `alphabetSize`, in which case
     * "Your guess cannot include the symbol '*'. Try again." is printed, with * replaced by the
     * first illegal symbol. Both messages should end with a newline.
     */
    static boolean isValidGuess(String guess, int codeLength, int alphabetSize) {
        assert guess != null; // defensive programming for implicit non-null pre-condition

        // TODO 1: Implement this method according to its specifications.
        if (guess.length() != codeLength) {
            System.out.println("Your guess must have " + codeLength + " symbols. Try again.");
            return false;
        }

        if (alphabetSize == 10) {
            for (int i = 0; i < guess.length(); i++) {
                if (guess.charAt(i) < 48 || guess.charAt(i) > 57) {
                    System.out.println("Your guess cannot include the symbol '" + guess.charAt(i) + "'. Try again.");
                    return false;
                }
            }
        }
        else {
            for (int i = 0; i < guess.length(); i++) {
                if (guess.charAt(i) < 49 || guess.charAt(i) > 48+alphabetSize) {
                    System.out.println("Your guess cannot include the symbol '" + guess.charAt(i) + "'. Try again.");
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Returns an `int[2]` array where the value at index 0 is the number of red pegs assigned to
     * the guess and the value at index 1 is the number of white pegs assigned to the guess. Each
     * red peg corresponds to symbol in the guess that matches the symbol in the same position of
     * the code. Each white peg corresponds to a symbol in the guess that appears in a different
     * position in the code (which has not been paired with a guess symbol corresponding to a
     * different peg). Requires that `guess` and `code` are valid.
     */
    public static int[] pegCounts(String guess, String code) {
        assert guess != null;
        assert code != null;

        // TODO 3: Implement this method according to its specifications.
        int[] pegCounts = new int[2];

        boolean[] codeSymbolIsPaired = new boolean[code.length()];
        boolean[] guessSymbolIsPaired = new boolean[guess.length()];

        // First pass: Assign red pegs and prevent paired symbols in the code and guess from
        // being used again.
        for (int i = 0; i < code.length(); i++) {
            if (code.charAt(i) == guess.charAt(i)) {
                pegCounts[0]++;
                codeSymbolIsPaired[i] = true;
                guessSymbolIsPaired[i] = true;
            }
        }

        // Second Pass: Assign white pegs
        for (int i = 0; i < code.length(); i++) {
            if (codeSymbolIsPaired[i]) continue;

            for (int j = 0; j < guess.length(); j++) {
                if (i == j) continue;

                if (code.charAt(i) == guess.charAt(j)) {
                    if (!guessSymbolIsPaired[j]) {
                        pegCounts[1]++;
                        codeSymbolIsPaired[i] = true;
                        guessSymbolIsPaired[j] = true;

                        break;
                    }
                }
            }
        }

        return pegCounts;

    }

    /**
     * Prints the key peg output to the console corresponding to the given `pegCounts` array,
     * followed by a newline. The output consists of `codeLength` colored circles, with any red
     * circles followed by any white circles followed by any black circles (indicating the absense
     * of a key peg). Requires that `pegCounts.length == 2` with `pegCounts[0] >= 0`, `pegCounts[1]
     * >= 0`, and `pegCounts[0] + pegCounts[1] <= codeLength`.
     */
    @SuppressWarnings("UnnecessaryUnicodeEscape")
    static void printKeyPegs(int[] pegCounts, int codeLength) {
        System.out.println("\uD83D\uDD34 ".repeat(pegCounts[0]) // red circles
                + "\u26AA ".repeat(pegCounts[1]) // white circles
                + "\u26AB ".repeat(codeLength - pegCounts[0] - pegCounts[1]) // black circles
        );
    }

    /**
     * Simulates a game of Mastermind with the given target `code`, using the given Scanner `sc` to
     * receive guesses from the user. Over the course of the given number of `turns`, the game
     * should prompt the user for a guess with the console output "#. Enter a guess: " where # is
     * the current valid guess number (starting from 1) and accept the user's console input by
     * calling `sc.nextLine()`. If the user gives a valid guess, the corresponding key peg output is
     * printed. If the guess matches the code, the game should then print "Congratulations! You won
     * in # guesses." with # replaced by the number of valid guesses, and the method should return.
     * Otherwise, the prompt is made for the next guess. If the user gives an invalid guess (which
     * includes contradictory guesses if `hardMode == true`), then the method should prompt the
     * user for another guess with the same guess number. If the user runs out of guesses, the game
     * should print "Better luck next time. The code was ****.", with **** replaced by the `code`.
     * Requires that `code` is a valid for the given `alphabetSize`.
     */
    static void play(Scanner sc, String code, int alphabetSize, int turns, boolean hardMode) {
        int guess_num = 1;
        System.out.println(guess_num + ". Enter a guess: ");

        // TODO 5: Implement this method according to its specs in the case that `hardMode` is `false`.
        // TODO 6: Modify the definition of this method to account for the case that `hardMode` is
        //  `true`. Your definition should promote maintainability by avoiding duplicate code and
        //  delegating involved computations to at least one helper method.
        throw new UnsupportedOperationException();
    }

    /**
     * Generates a random code with the given `codeLength` with symbols sampled randomly from the
     * first `alphabetSize` digits.
     */
    static String generateRandomCode(int codeLength, int alphabetSize) {
        StringBuilder code = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < codeLength; i++) {
            code.append((char) ('0' + (rand.nextInt(1, alphabetSize + 1) % 10)));
        }
        return code.toString();
    }

    /**
     * Returns the int value of the given `arg` with the given `name` if it falls within the range
     * `[min,max]`, otherwise prints an error message and terminates the program.
     */
    static int parseArgInBounds(String arg, String name, int min, int max) {
        int val = 0;
        try {
            val = Integer.parseInt(arg);
            if (val < min || val > max) {
                throw new IllegalArgumentException();
            }
        } catch (RuntimeException e) {
            System.err.println("Illegal " + name + " argument: " + arg);
            System.exit(1);
        }
        return val;
    }

    /**
     * Creates a new Mastermind game, generating the code and printing the welcome message before
     * calling the `play()` method to handle the rest of the game logic. Checks for command-line
     * arguments to set the three game parameters: (1) `hardMode` ("hard"): true if game requires
     * subsequent guesses to be consistent with previous pegs (default=`false`), (2) `codeLength`
     * ("l=#"): the number of symbols in the code (default=4, min=3, max=6), (3) `alphabetSize`
     * ("a=#"): the number of possible symbols in each position of the code (default=6, min=4,
     * max=10). Illegal arguments cause an error message to be printed and the program to be
     * terminated.
     */
    @SuppressWarnings("ForLoopReplaceableByForEach")
    public static void main(String[] args) {
        // initialize settings to default values
        boolean hardMode = false;
        int codeLength = 4;
        int alphabetSize = 6;

        // loop over program arguments to update game settings
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("hard")) {
                hardMode = true;
            } else if (args[i].startsWith("l=")) {
                codeLength = parseArgInBounds(args[i].substring(2), "code length", 3, 6);
            } else if (args[i].startsWith("a=")) {
                alphabetSize = parseArgInBounds(args[i].substring(2), "alphabet size", 4, 10);
            } else {
                System.err.println("Illegal program argument: " + args[i]);
                System.exit(1);
            }
        }

        String code = generateRandomCode(codeLength, alphabetSize);
        int numGuesses = codeLength + alphabetSize - 2;

        System.out.printf("Welcome to Mastermind!%nWe've generated a secret code containing %d"
                + " digits from %d to %d.%nYou have %d attempts to guess this code.%n", codeLength,
                alphabetSize < 10 ? 1 : 0, alphabetSize < 10 ? alphabetSize : 9, numGuesses);

        if (hardMode) {
            System.out.println("You're playing in HARD mode. Guesses cannot contradict information "
                    + "from previous pegs.");
        }
        System.out.println(); // one extra line break

        try (Scanner sc = new Scanner(System.in)) {
            play(sc, code, alphabetSize, numGuesses, hardMode);
        }
    }
}
