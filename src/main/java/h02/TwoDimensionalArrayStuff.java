package h02;

import org.tudalgo.algoutils.student.annotation.DoNotTouch;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.util.Arrays;

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
    public static float mean(final int[] input) {
        int sum = 0;
        for (final int j : input) {
            sum += j;
        }
        return (float) sum / input.length;
    }

    /**
     * Returns the mean number of occurrences of the query {@link String} in each line of the input array.
     *
     * @param input the input array
     * @param query the query {@link String}
     * @return the mean number of occurrences of the query {@link String} in each line of the input array
     */
    @DoNotTouch
    public static float meanOccurrencesPerLine(final String[][] input, final String query) {
        return mean(occurrences(input, query));
    }

    /**
     * Overload that splits the input string by lines and spaces, then calls regular meanOccurrencesPerLine.
     *
     * @param input the input string to split by lines and spaces
     * @param query the query {@link String}
     * @return the mean number of occurrences of the query {@link String} in each line of the input array
     */
    @DoNotTouch
    public static float meanOccurrencesPerLine(final String input, final String query) {
        // filter out unwanted symbols
        final String filteredInput = input.replaceAll("[^\\w\\s]", "");
        // split by lines
        final var processedInput = Arrays.stream(filteredInput.split("(\\r\\n|\\r|\\n)"))
            // split by spaces
            .map(line -> line.split("\\s"))
            // collect to 2D array
            .toArray(String[][]::new);
        /// uncomment the following line to log processed input
        // System.out.printf("Processed input: %s%n", Arrays.deepToString(processedInput));
        // call regular meanOccurrencesPerLine
        return meanOccurrencesPerLine(processedInput, query);
    }
}
