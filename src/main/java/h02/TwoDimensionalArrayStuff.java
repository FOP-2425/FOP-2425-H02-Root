package h02;

import org.tudalgo.algoutils.student.annotation.DoNotTouch;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

/**
 * This class serves as a container for the methods that are to be implemented by the students for exercise H2.1.2.
 */
public class TwoDimensionalArrayStuff {

    /**
     * Prevent instantiation of this utility class.
     */
    private TwoDimensionalArrayStuff() {
        throw new IllegalStateException("This class is not meant to be instantiated.");
    }

    /**
     * Returns an array containing the number of occurrences of the query {@link String} in each line of the input array.
     *
     * @param input the input array
     * @param query the query {@link String}
     * @return an array containing the number of occurrences of the query {@link String} in each line of the input array
     */
    @StudentImplementationRequired("H2.1.2")
    public static int[] occurrences(final String[][] input, final String query) {
        final int[] result = new int[input.length];
        for (int row = 0; row < input.length; row++) {
            for (int col = 0; col < input[row].length; col++) {
                if (input[row][col].equals(query)) {
                    result[row]++;
                }
            }
        }
        return result;
    }

    /**
     * Returns the mean of the input array.
     *
     * @param input the input array
     * @return the mean of the input array
     */
    @StudentImplementationRequired("H2.1.2")
    public static int mean(final int[] input) {
        int sum = 0;
        for (final int j : input) {
            sum += j;
        }
        return sum / input.length;
    }

    /**
     * Returns the mean number of occurrences of the query {@link String} in each line of the input array.
     *
     * @param input the input array
     * @param query the query {@link String}
     * @return the mean number of occurrences of the query {@link String} in each line of the input array
     */
    @DoNotTouch
    public static int meanOccurrencesPerLine(final String[][] input, final String query) {
        return mean(occurrences(input, query));
    }
}
