package h02;

import org.tudalgo.algoutils.student.annotation.SolutionOnly;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

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
        new FourWins().startGame();
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
        // simple test
        sanityChecksH212Helper(
            new String[][]{
                "a b c d e f".split(" "),
                "a b c d e f".split(" "),
                "a b c d e f".split(" "),
                },
            "b",
            new int[]{1, 1, 1},
            1
        );
        // more complex test
        sanityChecksH212Helper(
            new String[][]{
                "a a b b c c".split(" "),
                "a b c d e f".split(" "),
                "a a a b b b c c c".split(" "),
                },
            "b",
            new int[]{2, 1, 3},
            2
        );
    }

    @SolutionOnly
    public static void sanityChecksH212Helper(final String[][] input, final String query, final int[] refOcc, final int refMean) {
        final int[] occ = TwoDimensionalArrayStuff.occurrences(input, query);
        testEquals(refOcc.length, occ.length);
        for (int i = 0; i < occ.length; i++) {
            testEquals(refOcc[i], occ[i]);
        }
        testEquals(refMean, TwoDimensionalArrayStuff.meanOccurrencesPerLine(input, query));
    }
}
