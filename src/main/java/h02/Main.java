package h02;

import fopbot.RobotFamily;
import fopbot.World;
import org.tudalgo.algoutils.student.annotation.SolutionOnly;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import static org.tudalgo.algoutils.student.io.PropertyUtils.getIntProperty;
import static org.tudalgo.algoutils.student.test.StudentTestUtils.printTestResults;
import static org.tudalgo.algoutils.student.test.StudentTestUtils.testEquals;

/**
 * Main entry point in executing the program.
 */
public class Main {
    /**
     * Main entry point in executing the program.
     *
     * @param args program arguments, currently ignored
     */
    public static void main(final String[] args) {
        // H1
        sanityChecksH211();
        sanityChecksH212();
        printTestResults();

        // H2
        sanityChecksH22();
        printTestResults();

        // starting game (comment out if you just want to run the tests)
        final var propFile = "h02.properties";
        new FourWins(
            getIntProperty(propFile, "FW_WORLD_WIDTH"),
            getIntProperty(propFile, "FW_WORLD_HEIGHT")
        ).startGame();
    }

    /**
     * Perform sanity checks for exercise H2.1.1.
     */
    @StudentImplementationRequired("H2.3")
    public static void sanityChecksH211() {
        // push test
        final int[] newArray = OneDimensionalArrayStuff.push(new int[]{0, 1}, 2);
        final int[] expectedArray = {0, 1, 2};
        testEquals(expectedArray.length, newArray.length);
        for (int i = 0; i < newArray.length; i++) {
            testEquals(expectedArray[i], newArray[i]);
        }

        // calculateNextFibonacci test
        int[] fibonacciArray = {0, 1};
        for (int i = 0; i < 20; i++) {
            fibonacciArray = OneDimensionalArrayStuff.calculateNextFibonacci(fibonacciArray);
        }
        testEquals(22, fibonacciArray.length);
        testEquals(0, fibonacciArray[0]);
        testEquals(1, fibonacciArray[1]);
        for (int i = 2; i < fibonacciArray.length; i++) {
            testEquals(fibonacciArray[i - 1] + fibonacciArray[i - 2], fibonacciArray[i]);
        }

        // fibonacci test
        final int[] reference = {0, 1, 1, 2, 3, 5, 8, 13, 21, 34};
        for (int i = 0; i < 10; i++) {
            testEquals(reference[i], OneDimensionalArrayStuff.fibonacci(i));
        }
    }

    /**
     * Perform sanity checks for exercise H2.1.2.
     */
    @StudentImplementationRequired("H2.3")
    public static void sanityChecksH212() {
        // predefined simple test
        final String[][] simpleTest = new String[][]{
            "a b c d e f".split(" "),
            "a b c d e f".split(" "),
            "a b c d e f".split(" "),
        };
        // predefined complex test
        final String[][] complexTest = new String[][]{
            "a a b b c c".split(" "),
            "a b c d e f".split(" "),
            "a a a b b b c c c".split(" "),
        };


        // student implementation here:

        sanityChecksH212Helper(
            simpleTest,
            "b",
            new int[]{1, 1, 1},
            1
        );

        sanityChecksH212Helper(
            complexTest,
            "b",
            new int[]{2, 1, 3},
            2
        );
    }

    /**
     * Helper method for sanity checks for exercise H2.1.2.
     *
     * @param input   the input array
     * @param query   the query string
     * @param refOcc  the reference occurrences
     * @param refMean the reference mean
     */
    @SolutionOnly
    public static void sanityChecksH212Helper(
        final String[][] input,
        final String query,
        final int[] refOcc,
        final float refMean
    ) {
        final int[] occ = TwoDimensionalArrayStuff.occurrences(input, query);
        testEquals(refOcc.length, occ.length);
        for (int i = 0; i < occ.length; i++) {
            testEquals(refOcc[i], occ[i]);
        }
        testEquals(refMean, TwoDimensionalArrayStuff.meanOccurrencesPerLine(input, query));
    }

    /**
     * Perform sanity checks for exercise H2.2
     */
    @StudentImplementationRequired("H2.4")
    public static void sanityChecksH22() {
        // setting world size
        World.setSize(4, 5);

        // predefined stones1 array
        final RobotFamily[][] stones1 = {
            {null, RobotFamily.SQUARE_BLUE, null, RobotFamily.SQUARE_RED},
            {null, null, null, RobotFamily.SQUARE_BLUE},
            {null, null, null, RobotFamily.SQUARE_RED},
            {null, null, null, RobotFamily.SQUARE_BLUE},
            {null, null, null, RobotFamily.SQUARE_RED},
        };

        // predefined stones2 array
        final RobotFamily[][] stones2 = {
            {RobotFamily.SQUARE_BLUE, RobotFamily.SQUARE_BLUE, RobotFamily.SQUARE_BLUE, RobotFamily.SQUARE_BLUE},
            {RobotFamily.SQUARE_RED, RobotFamily.SQUARE_RED, RobotFamily.SQUARE_BLUE, RobotFamily.SQUARE_RED},
            {RobotFamily.SQUARE_BLUE, RobotFamily.SQUARE_RED, RobotFamily.SQUARE_BLUE, RobotFamily.SQUARE_BLUE},
            {RobotFamily.SQUARE_BLUE, RobotFamily.SQUARE_RED, RobotFamily.SQUARE_BLUE, RobotFamily.SQUARE_RED},
            {RobotFamily.SQUARE_BLUE, RobotFamily.SQUARE_BLUE, RobotFamily.SQUARE_BLUE, RobotFamily.SQUARE_RED},
        };


        // student implementation here:

        // H2.2.1 validateInput
        final boolean isInCol1 = FourWins.validateInput(1, stones1);
        final boolean isInCol3 = FourWins.validateInput(3, stones1);

        testEquals(true, isInCol1);
        testEquals(false, isInCol3);


        // H2.2.2 getDestinationRow
        final int rowCol1 = FourWins.getDestinationRow(1, stones1);
        final int rowCol3 = FourWins.getDestinationRow(3, stones1);

        testEquals(1, rowCol1);
        testEquals(-1, rowCol3);


        // H2.2.2 dropStone
        FourWins.dropStone(1, stones1, RobotFamily.SQUARE_RED);
        // System.out.println(Arrays.deepToString(stones1));
        // System.out.println(stones1);
        testEquals(RobotFamily.SQUARE_RED, stones1[1][1]);


        // H2.2.3 testWinHorizontal
        final boolean winRowBlue = FourWins.testWinHorizontal(stones2, RobotFamily.SQUARE_BLUE);
        final boolean winRowRed = FourWins.testWinHorizontal(stones2, RobotFamily.SQUARE_RED);

        testEquals(true, winRowBlue);
        testEquals(false, winRowRed);


        // H2.2.3 testWinVertical
        final boolean winColStones2 = FourWins.testWinVertical(stones2, RobotFamily.SQUARE_BLUE);
        final boolean winColStones1 = FourWins.testWinVertical(stones1, RobotFamily.SQUARE_BLUE);

        testEquals(true, winColStones2);
        testEquals(false, winColStones1);


        // H2.2.3 testWinConditions
        final boolean winStones2 = FourWins.testWinConditions(stones2, RobotFamily.SQUARE_BLUE);
        final boolean winStones1 = FourWins.testWinConditions(stones1, RobotFamily.SQUARE_BLUE);

        testEquals(true, winStones2);
        testEquals(false, winStones1);


        // H2.2.4 switchPlayer
        final RobotFamily nextPlayer1 = FourWins.nextPlayer(RobotFamily.SQUARE_BLUE);
        final RobotFamily nextPlayer2 = FourWins.nextPlayer(RobotFamily.SQUARE_RED);

        testEquals(RobotFamily.SQUARE_RED, nextPlayer1);
        testEquals(RobotFamily.SQUARE_BLUE, nextPlayer2);


        // H2.2.4 colorFieldBackground, writeDrawMessage, writeWinnerMessage, gameLoop
        // Test by playing
    }

}
