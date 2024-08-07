package h02;

import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.annotation.SkipAfterFirstFailedTest;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

@TestForSubmission
@Timeout(
    value = TestConstants.TEST_TIMEOUT_IN_SECONDS,
    unit = TimeUnit.SECONDS,
    threadMode = Timeout.ThreadMode.SEPARATE_THREAD
)
@SkipAfterFirstFailedTest(TestConstants.SKIP_AFTER_FIRST_FAILED_TEST)
public class OneDimensionalArrayStuffTest {
    /**
     * Tests the {@link OneDimensionalArrayStuff#push(int[], int)} method.
     *
     * @param params        The {@link JsonParameterSet} to use for the test.
     * @param lastOnly      Whether to only test the last element.
     * @param unchangedOnly Whether to only test that the input array is unchanged. Will not test the result.
     */
    private static void testPush(final JsonParameterSet params, final boolean lastOnly, final boolean unchangedOnly) {
        final List<Integer> input = params.get("array");
        final int value = params.getInt("value");
        final List<Integer> expectedArray = new ArrayList<>(input);
        expectedArray.add(value);
        final var ParamsContext = params.toContext();
        final var cb = contextBuilder()
            .add(ParamsContext)
            .add("Method", "push")
            .add("expected result", expectedArray);
        final int[] inputArray = input.stream().mapToInt(i -> i).toArray();
        final int[] result = Assertions2.callObject(
            () -> OneDimensionalArrayStuff.push(
                inputArray,
                value
            ),
            cb.build(),
            r -> "An error occurred during execution."
        );
        final var actualArray = Arrays.stream(result).boxed().toList();
        cb.add("actual result", actualArray);
        if (unchangedOnly) {
            Assertions2.assertIterableEquals(
                input,
                Arrays.stream(inputArray).boxed().toList(),
                cb.build(),
                r -> "The input array was changed."
            );
            return;
        }
        if (lastOnly) {
            Assertions2.assertEquals(
                expectedArray.get(expectedArray.size() - 1),
                actualArray.get(actualArray.size() - 1),
                cb.build(),
                r -> "Invalid result."
            );
        } else {
            Assertions2.assertIterableEquals(
                expectedArray,
                actualArray,
                cb.build(),
                r -> "Invalid result."
            );
        }
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "OneDimensionalArrayStuffTestRandomNumbers.json")
    public void testPushLastElementCorrect(final JsonParameterSet params) {
        testPush(params, true, false);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "OneDimensionalArrayStuffTestRandomNumbers.json")
    public void testPushAllElementsCorrect(final JsonParameterSet params) {
        testPush(params, false, false);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "OneDimensionalArrayStuffTestRandomNumbers.json")
    public void testPushOriginalArrayUnchanged(final JsonParameterSet params) {
        testPush(params, false, true);
    }
}
