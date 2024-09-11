package h02;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fopbot.RobotFamily;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
            "OneDimensionalArrayStuffTestPushRandomNumbers.generated.json"
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
            "OneDimensionalArrayStuffTestCalculateNextFibonacciRandomNumbers" + (twoPositivesOnly ? "TwoPositiveNumbersOnly" : "") + ".generated.json"
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
            "OneDimensionalArrayStuffTestFibonacciRandomNumbers" + (smallerThanTwo ? "SmallerThanTwo" : "") + ".generated.json"
        );
    }


    @Test
    public void generateValidateInputRandomCases() throws IOException {
        TestUtils.generateJsonTestData(
            (mapper, index, rnd) -> {
                final ObjectNode objectNode = mapper.createObjectNode();

                // Spielfeldgröße zufällig wählen
                final int width = rnd.nextInt(3, 10); // Breite zwischen 3 und 10
                final int height = rnd.nextInt(3, 10); // Höhe zwischen 3 und 10
                objectNode.put("width", width);
                objectNode.put("height", height);

                // Zufälliges Spielfeld generieren
                RobotFamily[][] stones = new RobotFamily[height][width];
                for (int row = 0; row < height; row++) {
                    for (int col = 0; col < width; col++) {
                        if (rnd.nextBoolean()) {
                            stones[row][col] = rnd.nextBoolean() ? RobotFamily.SQUARE_RED : RobotFamily.SQUARE_BLUE;
                        }
                    }
                }

                // Spaltenindex zufällig wählen (auch ungültige Indizes)
                final int column = rnd.nextInt(-1, width + 1);
                objectNode.put("column", column);

                // Erwartetes Ergebnis berechnen
                boolean expectedResult = column >= 0 && column < width && stones[height - 1][column] == null;
                objectNode.put("expected result", expectedResult);

                // Spielfeld in JSON-Format umwandeln
                ArrayNode stonesArray = mapper.createArrayNode();
                for (int row = 0; row < height; row++) {
                    ArrayNode rowArray = mapper.createArrayNode();
                    for (int col = 0; col < width; col++) {
                        if (stones[row][col] == null) {
                            rowArray.add("EMPTY");
                        } else if (stones[row][col] == RobotFamily.SQUARE_RED) {
                            rowArray.add("SQUARE_RED");
                        } else {
                            rowArray.add("SQUARE_BLUE");
                        }
                    }
                    stonesArray.add(rowArray);
                }
                objectNode.set("stones", stonesArray);

                return objectNode;
            },
            TEST_ITERATIONS,
            "FourWinsTestValidateInputRandomCases.generated.json"
        );
    }

    @Test
    public void generateFourWinsTestGameBoard() throws IOException {
        TestUtils.generateJsonTestData(
            (mapper, index, rnd) -> {
                int worldHeight = rnd.nextInt(5, 10);
                int worldWidth = rnd.nextInt(5, 10);
                // SQUARE_RED <-> true, SQUARE_BLUE <-> false, null <-> null
                RobotFamily[][] gameBoard = new RobotFamily[worldHeight][worldWidth];
                List<Integer> firstFreeIndex = new ArrayList<>(worldWidth);  // values may exceed array index range
                for (int col = 0; col < worldWidth; col++) {
                    int rowsToFill = rnd.nextInt(worldHeight);
                    for (int row = 0; row <= rowsToFill; row++) {
                        gameBoard[row][col] = RobotFamily.SQUARE_RED;
                    }
                    firstFreeIndex.add(rowsToFill + 1);
                }

                ArrayNode firstFreeIndexNode = mapper.createArrayNode();
                firstFreeIndex.stream()
                    .map(mapper.getNodeFactory()::numberNode)
                    .forEach(firstFreeIndexNode::add);

                ArrayNode gameBoardNode = mapper.createArrayNode();
                Arrays.stream(gameBoard)
                    .map(gameBoardRow -> Arrays.stream(gameBoardRow)
                        .map(rf -> mapper.getNodeFactory().textNode(rf != null ? rf.getName() : null))
                        .toList())
                    .forEach(gameBoardRow -> gameBoardNode.add(mapper.createArrayNode().addAll(gameBoardRow)));

                ObjectNode objectNode = mapper.createObjectNode()
                    .put("worldHeight", worldHeight)
                    .put("worldWidth", worldWidth);
                objectNode.set("firstFreeIndex", firstFreeIndexNode);
                objectNode.set("gameBoard", gameBoardNode);

                return objectNode;
            },
            TEST_ITERATIONS,
            "FourWinsTestGameBoard.generated.json"
        );
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void generateFourWinsTestGameBoardWin(boolean horizontal) throws IOException {
        TestUtils.generateJsonTestData(
            (mapper, index, rnd) -> {
                int worldHeight = rnd.nextInt(5, 10);
                int worldWidth = rnd.nextInt(5, 10);
                RobotFamily currentPlayer = rnd.nextBoolean() ? RobotFamily.SQUARE_RED : RobotFamily.SQUARE_BLUE;
                ObjectNode objectNode = mapper.createObjectNode()
                    .put("worldHeight", worldHeight)
                    .put("worldWidth", worldWidth)
                    .put("currentPlayer", currentPlayer.getName());

                ArrayNode winningCoordinates = mapper.createArrayNode();
                rnd.ints(rnd.nextInt(3), 0, horizontal ? worldHeight : worldWidth)
                    .distinct()
                    .forEach(i -> {
                        if (horizontal) {
                            winningCoordinates.add(mapper.createObjectNode()
                                .put("x", rnd.nextInt(worldWidth - 4 + 1))
                                .put("y", i));
                        } else {
                            winningCoordinates.add(mapper.createObjectNode()
                                .put("x", i)
                                .put("y", rnd.nextInt(worldHeight - 4 + 1)));
                        }
                    });
                objectNode.set(horizontal ? "winningRowCoordinates" : "winningColCoordinates", winningCoordinates);

                RobotFamily[][] gameBoard = new RobotFamily[worldHeight][worldWidth];
                for (JsonNode node : winningCoordinates) {
                    for (int offset = 0; offset < 4; offset++) {
                        if (horizontal) {
                            gameBoard[node.get("y").intValue()][node.get("x").intValue() + offset] = currentPlayer;
                        } else {
                            gameBoard[node.get("y").intValue() + offset][node.get("x").intValue()] = currentPlayer;
                        }
                    }
                }

                ArrayNode gameBoardNode = mapper.createArrayNode();
                Arrays.stream(gameBoard)
                    .map(gameBoardRow -> Arrays.stream(gameBoardRow)
                        .map(rf -> mapper.getNodeFactory().textNode(rf != null ? rf.getName() : null))
                        .toList())
                    .forEach(gameBoardRow -> gameBoardNode.add(mapper.createArrayNode().addAll(gameBoardRow)));
                objectNode.set("gameBoard", gameBoardNode);

                return objectNode;
            },
            TEST_ITERATIONS,
            "FourWinsTestGameBoard" + (horizontal ? "Horizontal" : "Vertical") + "Win.generated.json"
        );
    }
}
