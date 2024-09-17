package h02;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.annotation.SkipAfterFirstFailedTest;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions4;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;
import org.tudalgo.algoutils.tutor.general.reflections.BasicMethodLink;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLocalVariable;

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
        final List<Integer> expectedArray = params.get("expected result");
        final var ParamsContext = params.toContext();
        final var cb = contextBuilder()
            .add(ParamsContext)
            .add("Method", "push");
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

    private static void testCalculateNextFibonacci(final JsonParameterSet params, final boolean vanforderung) {
        try (final var odasMock = Mockito.mockStatic(OneDimensionalArrayStuff.class, Answers.CALLS_REAL_METHODS)) {
            final List<Integer> input = params.get("array");
            final List<Integer> expectedArray = params.get("expected result");
            final var ParamsContext = params.toContext();
            final var cb = contextBuilder()
                .add(ParamsContext)
                .add("Method", "calculateNextFibonacci");

            odasMock.when(() -> OneDimensionalArrayStuff.push(Mockito.any(), Mockito.anyInt())).thenAnswer(invocation -> {
                final int[] array = invocation.getArgument(0);
                final int value = invocation.getArgument(1);
                final int[] newArray = Arrays.copyOf(array, array.length + 1);
                newArray[array.length] = value;
                return newArray;
            });

            final int[] inputArray = input.stream().mapToInt(i -> i).toArray();
            final int[] result = Assertions2.callObject(
                () -> OneDimensionalArrayStuff.calculateNextFibonacci(
                    inputArray
                ),
                cb.build(),
                r -> "An error occurred during execution."
            );
            final var actualArray = Arrays.stream(result).boxed().toList();
            cb.add("actual result", actualArray);
            if (vanforderung) {
                Assertions2.assertIterableEquals(
                    input,
                    Arrays.stream(inputArray).boxed().toList(),
                    cb.build(),
                    r -> "The input array was changed."
                );
                odasMock.verify(
                    () -> OneDimensionalArrayStuff.push(
                        inputArray,
                        expectedArray.get(expectedArray.size() - 1)
                    ),
                    Mockito.atLeastOnce()
                );
                return;
            }
            Assertions2.assertIterableEquals(
                expectedArray,
                actualArray,
                cb.build(),
                r -> "Invalid result."
            );

        }
    }

    private static void testFibonacci(final JsonParameterSet params, final boolean vanforderung) {
        try (final var odasMock = Mockito.mockStatic(OneDimensionalArrayStuff.class, Answers.CALLS_REAL_METHODS)) {
            final Integer input = params.get("n");
            final int expectedresult = params.get("expected result");
            final List<Integer> referenceArray = params.get("reference array");
            final var ParamsContext = params.toContext("reference array");
            final var cb = contextBuilder()
                .add(ParamsContext)
                .add("Method", "fibonacci");

            odasMock.when(() -> OneDimensionalArrayStuff.push(Mockito.any(), Mockito.anyInt())).thenAnswer(invocation -> {
                final int[] array = invocation.getArgument(0);
                final int value = invocation.getArgument(1);
                final int[] newArray = Arrays.copyOf(array, array.length + 1);
                newArray[array.length] = value;
                return newArray;
            });

            odasMock.when(() -> OneDimensionalArrayStuff.calculateNextFibonacci(Mockito.any())).thenAnswer(invocation -> {
                final int[] array = invocation.getArgument(0);
                return OneDimensionalArrayStuff.push(array, array[array.length - 1] + array[array.length - 2]);
            });

            final int result = Assertions2.callObject(
                () -> OneDimensionalArrayStuff.fibonacci(
                    input
                ),
                cb.build(),
                r -> "An error occurred during execution."
            );

            cb.add("actual result", result);
            if (vanforderung) {
                // test calculateFib was used
                for (int i = 2; i < referenceArray.size(); i++) {
                    final int finalI = i;
                    try {
                        // test for entire array
                        odasMock.verify(
                            () -> OneDimensionalArrayStuff.calculateNextFibonacci(
                                referenceArray.subList(0, finalI).stream().mapToInt(j -> j).toArray()
                            ),
                            Mockito.atLeastOnce()
                        );
                    } catch (final MockitoException e) {
                        // test for last two elements
                        odasMock.verify(
                            () -> OneDimensionalArrayStuff.calculateNextFibonacci(
                                referenceArray.subList(finalI - 2, finalI).stream().mapToInt(j -> j).toArray()
                            ),
                            Mockito.atLeastOnce()
                        );
                    }
                }
                return;
            }
            Assertions2.assertEquals(
                expectedresult,
                result,
                cb.build(),
                r -> "Invalid result."
            );

        }
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "OneDimensionalArrayStuffTestPushRandomNumbers.generated.json")
    public void testPushLastElementCorrect(final JsonParameterSet params) {
        testPush(params, true, false);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "OneDimensionalArrayStuffTestPushRandomNumbers.generated.json")
    public void testPushAllElementsCorrect(final JsonParameterSet params) {
        testPush(params, false, false);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "OneDimensionalArrayStuffTestPushRandomNumbers.generated.json")
    public void testPushOriginalArrayUnchanged(final JsonParameterSet params) {
        testPush(params, false, true);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "OneDimensionalArrayStuffTestCalculateNextFibonacciRandomNumbersTwoPositiveNumbersOnly.generated.json")
    public void testCalculateNextFibonacciPositiveOnly(final JsonParameterSet params) {
        testCalculateNextFibonacci(params, false);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "OneDimensionalArrayStuffTestCalculateNextFibonacciRandomNumbers.generated.json")
    public void testCalculateNextFibonacciAllNumbers(final JsonParameterSet params) {
        testCalculateNextFibonacci(params, false);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "OneDimensionalArrayStuffTestCalculateNextFibonacciRandomNumbers.generated.json")
    public void testCalculateNextFibonacciVanforderungen(final JsonParameterSet params) {
        testCalculateNextFibonacci(params, true);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "OneDimensionalArrayStuffTestFibonacciRandomNumbers.generated.json")
    public void testFibonacciVanforderungen(final JsonParameterSet params) {
        testFibonacci(params, true);
    }

    @Test
    public void testFibonacciNonIterativeVanforderungen() throws NoSuchMethodException {
        // test exactly one for loop
        final var ml = BasicMethodLink.of(OneDimensionalArrayStuff.class.getDeclaredMethod("fibonacci", int.class));
        final var ctElement = ml.getCtElement();
        final var cb = contextBuilder()
            .add("Method", "fibonacci");
        Assertions4.assertIsNotRecursively(
            ctElement,
            cb.build(),
            r -> "The method should not have any recursive calls."
        );
        final var forLoops = ctElement.filterChildren(
            c -> c instanceof spoon.reflect.code.CtFor
                || c instanceof spoon.reflect.code.CtForEach
                || c instanceof spoon.reflect.code.CtWhile
                || c instanceof spoon.reflect.code.CtDo
        ).list();
        Assertions2.assertEquals(
            1,
            forLoops.size(),
            cb.build(),
            r -> "The method should contain exactly one for loop."
        );
        // test exactly one variable declaration
        final var variableDeclarations = ctElement.filterChildren(
            c -> c instanceof CtLocalVariable<?>
        ).list(CtLocalVariable.class);
        cb.add("variable declarations", variableDeclarations);
        Assertions2.assertEquals(
            2,
            variableDeclarations.size(),
            cb.build(),
            r -> "The method should contain exactly two variable declarations."
        );
        // one of type int[] and one of type int
        Assertions2.assertTrue(
            variableDeclarations.stream().anyMatch(
                c -> c.getType().toString().equals("int[]")
            ),
            cb.build(),
            r -> "The method should declare exactly one variable of type int[]."
        );
        Assertions2.assertTrue(
            variableDeclarations.stream().anyMatch(
                c -> c.getType().toString().equals("int")
            ),
            cb.build(),
            r -> "The method should contain exactly one variable of type int."
        );
        // test no Method calls except for calculateNextFibonacci
        final var methodCalls = ctElement.filterChildren(
            c -> c instanceof spoon.reflect.code.CtInvocation<?>
        ).list(CtInvocation.class);
               cb.add("method calls", methodCalls);
        Assertions2.assertEquals(
            1,
            methodCalls.size(),
            cb.build(),
            r -> "The method should contain exactly one method call."
        );
        final var methodCall = methodCalls.get(0);
        Assertions2.assertEquals(
            "calculateNextFibonacci",
            methodCall.getExecutable().getSimpleName(),
            cb.build(),
            r -> "The method should only call calculateNextFibonacci."
        );
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "OneDimensionalArrayStuffTestFibonacciRandomNumbersSmallerThanTwo.generated.json")
    public void testFibonacciSmallerThanTwo(final JsonParameterSet params) {
        testFibonacci(params, false);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "OneDimensionalArrayStuffTestFibonacciRandomNumbers.generated.json")
    public void testFibonacciBigNumbers(final JsonParameterSet params) {
        testFibonacci(params, false);
    }
}
