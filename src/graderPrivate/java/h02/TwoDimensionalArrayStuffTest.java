package h02;

import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.annotation.SkipAfterFirstFailedTest;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertNotNull;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.callObject;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

@TestForSubmission
@Timeout(
    value = TestConstants.TEST_TIMEOUT_IN_SECONDS,
    unit = TimeUnit.SECONDS,
    threadMode = Timeout.ThreadMode.SEPARATE_THREAD
)
@SkipAfterFirstFailedTest(TestConstants.SKIP_AFTER_FIRST_FAILED_TEST)
public class TwoDimensionalArrayStuffTest {

    @ParameterizedTest
    @JsonParameterSetTest("TwoDimensionalArrayStuffTestEmptySentence.json")
    public void testOccurrencesEmptyArray(JsonParameterSet params) {
        testOccurrences(params);
    }

    @ParameterizedTest
    @JsonParameterSetTest("TwoDimensionalArrayStuffTestSingleSentence.json")
    public void testOccurrencesSingleSentence(JsonParameterSet params) {
        testOccurrences(params);
    }

    @ParameterizedTest
    @JsonParameterSetTest("TwoDimensionalArrayStuffTestMultipleSentences.json")
    public void testOccurrencesMultipleSentences(JsonParameterSet params) {
        testOccurrences(params);
    }

    @ParameterizedTest
    @JsonParameterSetTest("TwoDimensionalArrayStuffTestIntegerMean.json")
    public void testMeanInteger(JsonParameterSet params) {
        testMean(params);
    }

    @ParameterizedTest
    @JsonParameterSetTest("TwoDimensionalArrayStuffTestFloatMean.json")
    public void testMeanFloat(JsonParameterSet params) {
        testMean(params);
    }

    private static void testOccurrences(JsonParameterSet params) {
        List<String> sentences = params.get("sentences");
        String[][] input = sentences.stream()
            .map(s -> s.split(" "))
            .toArray(String[][]::new);
        String query = params.getString("query");
        Context context = contextBuilder()
            .add("input", input)
            .add("query", query)
            .build();

        List<Integer> expectedList = params.get("expectedResult");
        AtomicInteger counter = new AtomicInteger(0);
        int[] expected = new int[expectedList.size()];
        expectedList.forEach(i -> expected[counter.getAndIncrement()] = i);
        int[] actual = callObject(() -> TwoDimensionalArrayStuff.occurrences(input, query), context, result ->
            "An exception occurred while invoking method occurrences");

        assertNotNull(actual, context, result ->
            "Array returned by method occurrences is null");
        assertEquals(input.length, actual.length, context, result ->
            "Array returned by method occurrences does not have correct length");
        for (int i = 0; i < sentences.size(); i++) {
            final int finalI = i;
            assertEquals(expected[i], actual[i], context, result ->
                "Array returned by method occurrences does not have correct value at index " + finalI);
        }
    }

    private static void testMean(JsonParameterSet params) {
        List<Integer> inputList = params.get("input");
        AtomicInteger counter = new AtomicInteger(0);
        int[] input = new int[inputList.size()];
        inputList.forEach(i -> input[counter.getAndIncrement()] = i);
        Context context = contextBuilder()
            .add("input", input)
            .build();

        float expected = params.getFloat("expected");
        float actual = callObject(() -> TwoDimensionalArrayStuff.mean(input), context, result ->
            "An exception occurred while invoking method mean");

        // susceptible to rounding errors?
        assertEquals(expected, actual, context, result -> "Value returned by method mean is incorrect");
    }
}
