package h02;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static h02.TestConstants.TEST_ITERATIONS;

@DisabledIf("org.tudalgo.algoutils.tutor.general.Utils#isJagrRun()")
public class TestJsonGenerators {
    @Test
    public void generateOneDimensionalArrayStuffTestPushRandomNumbers() throws IOException {
        TestUtils.generateJsonTestData(
            (mapper, index, rnd) -> {
                final ObjectNode objectNode = mapper.createObjectNode();
                objectNode.put("value", rnd.nextInt((int) -2e5, (int) 2e5));
                final List<Integer> input = new ArrayList<>();
                if (index < 99) {
                    for (int i = 0; i < rnd.nextInt(5, 10); i++) {
                        input.add(rnd.nextInt((int) -2e5, (int) 2e5));
                    }
                }
                final ArrayNode inputArrayNode = mapper.createArrayNode();
                input.forEach(inputArrayNode::add);
                objectNode.set("array", inputArrayNode);
                input.add(objectNode.get("value").asInt());
                final ArrayNode expectedArrayNode = mapper.createArrayNode();
                input.forEach(expectedArrayNode::add);
                objectNode.set("expected result", expectedArrayNode);
                return objectNode;
            },
            TEST_ITERATIONS,
            "OneDimensionalArrayStuffTestPushRandomNumbers.json"
        );
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void generateOneDimensionalArrayStuffTestCalculateNextFibonacciRandomNumbers(
        final boolean twoPositivesOnly
    ) throws IOException {
        TestUtils.generateJsonTestData(
            (mapper, index, rnd) -> {
                final ObjectNode objectNode = mapper.createObjectNode();
                final List<Integer> input = new ArrayList<>();
                for (int i = 0; i < (twoPositivesOnly ? 2 : rnd.nextInt(5, 10)); i++) {
                    input.add(rnd.nextInt(
                        twoPositivesOnly ? 0 : (int) -2e5,
                        (int) 2e5
                    ));
                }
                final ArrayNode inputArrayNode = mapper.createArrayNode();
                input.forEach(inputArrayNode::add);
                objectNode.set("array", inputArrayNode);
                System.out.println(input.size());
                final int nextFibonacci = input.get(input.size() - 1) + input.get(input.size() - 2);
                input.add(nextFibonacci);
                final ArrayNode expectedArrayNode = mapper.createArrayNode();
                input.forEach(expectedArrayNode::add);
                objectNode.set("expected result", expectedArrayNode);
                return objectNode;
            },
            TEST_ITERATIONS,
            "OneDimensionalArrayStuffTestCalculateNextFibonacciRandomNumbers" + (twoPositivesOnly ? "TwoPositiveNumbersOnly" : "") + ".json"
        );
    }

    /**
     * Reference Fibonacci implementation using the closed-form formula.
     *
     * @param n The number to calculate the Fibonacci number for.
     * @return The Fibonacci number.
     * @see <a href="https://en.wikipedia.org/wiki/Fibonacci_sequence#Closed-form_expression">Fibonacci Closed-form expression on Wikipedia</a>
     */
    public static long fib(final int n) {
        final double sqrt5 = Math.sqrt(5);
        final double phi = (1 + sqrt5) / 2;
        final double psi = (1 - sqrt5) / 2;

        return Math.round((Math.pow(phi, n) - Math.pow(psi, n)) / sqrt5);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void generateOneDimensionalArrayStuffTestFibonacciRandomNumbers(
        final boolean smallerThanTwo
    ) throws IOException {
        TestUtils.generateJsonTestData(
            (mapper, index, rnd) -> {
                final int startIdx = smallerThanTwo ? index : index + 2;
                final ObjectNode objectNode = mapper.createObjectNode();
                objectNode.put("n", startIdx);
                objectNode.put("expected result", fib(startIdx));
                final ArrayNode refArrayNode = mapper.createArrayNode();
                for (int i = 0; i <= startIdx; i++) {
                    refArrayNode.add(fib(i));
                }
                objectNode.set("reference array", refArrayNode);
                return objectNode;
            },
            smallerThanTwo ? 2 : TEST_ITERATIONS,
            "OneDimensionalArrayStuffTestFibonacciRandomNumbers" + (smallerThanTwo ? "SmallerThanTwo" : "") + ".json"
        );
    }
}
