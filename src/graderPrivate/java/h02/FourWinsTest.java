package h02;

import fopbot.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.SpoonUtils;
import org.tudalgo.algoutils.tutor.general.annotation.SkipAfterFirstFailedTest;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.assertions.PreCommentSupplier;
import org.tudalgo.algoutils.tutor.general.assertions.ResultOfObject;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.reference.CtExecutableReference;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
@Timeout(
    value = TestConstants.TEST_TIMEOUT_IN_SECONDS,
    unit = TimeUnit.SECONDS,
    threadMode = Timeout.ThreadMode.SEPARATE_THREAD
)
@SkipAfterFirstFailedTest(TestConstants.SKIP_AFTER_FIRST_FAILED_TEST)
public class FourWinsTest {

    @ParameterizedTest
    @JsonParameterSetTest(value = "generateFourWinsTest_validateInput.json")
    public void testValidateInput(final JsonParameterSet params) {
        // get params
        final int paramColumn = params.getInt("column");
        final List<List<String>> paramStones = params.get("stones");
        final boolean expectedResult = params.getBoolean("expected result");

        // write params to context
        final var ParamsContext = params.toContext("expected result");
        final var cb = contextBuilder()
            .add(ParamsContext)
            .add("Method", "validateInput");

        // parse array and calculate result
        RobotFamily[][] paramStonesArray = paramStones.stream()
            .map(
                innerList -> innerList.stream().map(
                    (str) -> str.equals("SQUARE_RED") ? RobotFamily.SQUARE_RED : RobotFamily.SQUARE_BLUE
                ).toArray(RobotFamily[]::new)
            ).toArray(RobotFamily[][]::new);
        final boolean actualResult = Assertions2.callObject(
            () -> FourWins.validateInput(
                paramColumn,
                paramStonesArray
            ),
            cb.build(),
            r -> "An error occurred during execution."
        );

        // validate result
        Assertions2.assertEquals(
            expectedResult,
            actualResult,
            cb.build(),
            r -> "Invalid result."
        );
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "generateNoTest.json")
    public void noTestYet(final JsonParameterSet params) {
        Assertions2.assertEquals(
            true,
            false,
            contextBuilder().build(),
            r -> "Test not implemented yet."
        );
    }

    @ParameterizedTest
    @JsonParameterSetTest("FourWinsTestGameBoard.json")
    public void testGetDestinationRowFreeSlot(JsonParameterSet params) {
        testGetDestinationRow(params, true);
    }

    @ParameterizedTest
    @JsonParameterSetTest("FourWinsTestGameBoard.json")
    public void testGetDestinationRowBlockedSlot(JsonParameterSet params) {
        testGetDestinationRow(params, false);
    }

    @Test
    public void testGetDestinationRowVAnforderung() {
        iterateMethodStatements("getDestinationRow",
            new Class[] {int.class, RobotFamily[][].class},
            iterator -> {
                int loopStatements = 0;

                while (iterator.hasNext()) {
                    CtElement ctElement = iterator.next();
                    if (ctElement instanceof CtFor || ctElement instanceof CtWhile || ctElement instanceof CtDo) {
                        loopStatements++;
                    }
                }
                assertEquals(1, loopStatements, emptyContext(), result -> "Method does not use exactly one loop");
            });
    }

    @ParameterizedTest
    @JsonParameterSetTest("FourWinsTestGameBoard.json")
    public void testDropStoneRobotCorrect(JsonParameterSet params) {
        int worldHeight = params.getInt("worldHeight");
        int worldWidth = params.getInt("worldWidth");
        List<Integer> firstFreeIndex = params.get("firstFreeIndex");
        RobotFamily[][] gameBoard = new RobotFamily[worldHeight][worldWidth];
        List<List<String>> paramStones = params.get("gameBoard");
        for (int row = 0; row < worldHeight; row++) {
            for (int col = 0; col < worldWidth; col++) {
                gameBoard[row][col] = paramStones.get(row).get(col) != null ? RobotFamily.SQUARE_RED : null;
            }
        }

        World.setSize(worldWidth, worldHeight);
        World.setDelay(0);
        for (int col = 0; col < worldWidth; col++) {
            if (firstFreeIndex.get(col) >= worldHeight) {
                continue;
            }

            World.getGlobalWorld().reset();  // clear entities
            RobotFamily currentPlayer = col % 2 == 0 ? RobotFamily.SQUARE_RED : RobotFamily.SQUARE_BLUE;
            Context context = contextBuilder()
                .add("world height", worldHeight)
                .add("world width", worldWidth)
                .add("column", col)
                .add("stones", gameBoard)
                .add("currentPlayer", currentPlayer)
                .build();

            try {
                final int finalCol = col;
                call(() -> FourWins.dropStone(finalCol, gameBoard, currentPlayer), context, result ->
                    "An exception occurred while invoking method dropStone. Result may be salvageable, continuing...");
            } catch (Throwable t) {
                t.printStackTrace(System.err);
            }

            List<Robot> robots = World.getGlobalWorld()
                .getAllFieldEntities()
                .stream()
                .filter(fieldEntity -> fieldEntity instanceof Robot)
                .map(fieldEntity -> (Robot) fieldEntity)
                .toList();
            assertEquals(1, robots.size(), context, result ->
                "Unexpected number of robots in world");

            RobotTrace trace = World.getGlobalWorld().getTrace(robots.get(0));
            Robot robot = trace.getTransitions().get(0).robot;
            assertEquals(col, robot.getX(), context, result ->
                "Robot was initialized with incorrect x coordinate");
            assertEquals(worldHeight - 1, robot.getY(), context, result ->
                "Robot was initialized with incorrect y coordinate");
            assertEquals(Direction.DOWN, robot.getDirection(), context, result ->
                "Robot was initialized with incorrect direction");
            assertEquals(0, robot.getNumberOfCoins(), context, result ->
                "Robot was initialized with incorrect number of coins");
            assertEquals(currentPlayer, robot.getRobotFamily(), context, result ->
                "Robot was initialized with incorrect robot family");
        }
    }

    @Test
    public void testDropStoneCallsGetDestinationRow() {
        CtMethod<?> dropStoneCtMethod = getCtMethod("dropStone", new Class[] {int.class, RobotFamily[][].class, RobotFamily.class});
        CtExecutableReference<?> getDestinationRowCtExecRef = getCtMethod("getDestinationRow", new Class[] {int.class, RobotFamily[][].class})
            .getReference();
        Iterator<CtElement> iterator = dropStoneCtMethod.descendantIterator();

        boolean getDestinationRowCalled = false;
        while (!getDestinationRowCalled && iterator.hasNext()) {
            CtElement ctElement = iterator.next();
            if (ctElement instanceof CtInvocation<?> ctInvocation) {
                getDestinationRowCalled = ctInvocation.getExecutable().equals(getDestinationRowCtExecRef);
            }
        }
        assertTrue(getDestinationRowCalled, emptyContext(), result ->
            "Method dropStone does not call method getDestinationRow");
    }

    @ParameterizedTest
    @JsonParameterSetTest("FourWinsTestGameBoard.json")
    public void testDropStoneMovementCorrect(JsonParameterSet params) {
        int worldHeight = params.getInt("worldHeight");
        int worldWidth = params.getInt("worldWidth");
        List<Integer> firstFreeIndex = params.get("firstFreeIndex");
        RobotFamily[][] gameBoard = new RobotFamily[worldHeight][worldWidth];
        List<List<String>> paramStones = params.get("gameBoard");
        for (int row = 0; row < worldHeight; row++) {
            for (int col = 0; col < worldWidth; col++) {
                gameBoard[row][col] = paramStones.get(row).get(col) != null ? RobotFamily.SQUARE_RED : null;
            }
        }

        World.setSize(worldWidth, worldHeight);
        World.setDelay(0);
        for (int col = 0; col < worldWidth; col++) {
            if (firstFreeIndex.get(col) >= worldHeight) {
                continue;
            }

            World.getGlobalWorld().reset();  // clear entities
            RobotFamily currentPlayer = RobotFamily.SQUARE_RED;
            Context context = contextBuilder()
                .add("world height", worldHeight)
                .add("world width", worldWidth)
                .add("column", col)
                .add("stones", gameBoard)
                .add("currentPlayer", currentPlayer)
                .build();

            try {
                final int finalCol = col;
                call(() -> FourWins.dropStone(finalCol, gameBoard, currentPlayer), context, result ->
                    "An exception occurred while invoking method dropStone. Result may be salvageable, continuing...");
            } catch (Throwable t) {
                t.printStackTrace(System.err);
            }

            List<Robot> robots = World.getGlobalWorld()
                .getAllFieldEntities()
                .stream()
                .filter(fieldEntity -> fieldEntity instanceof Robot)
                .map(fieldEntity -> (Robot) fieldEntity)
                .toList();
            assertEquals(1, robots.size(), context, result ->
                "Unexpected number of robots in world");

            List<Transition> transitions = World.getGlobalWorld().getTrace(robots.get(0)).getTransitions();
            int expectedTransitionsSize = worldHeight - 1 - firstFreeIndex.get(col) + 3;
            for (int i = 0; i < expectedTransitionsSize; i++) {
                Transition transition = transitions.get(i);
                final int finalI = i;
                PreCommentSupplier<ResultOfObject<Transition.RobotAction>> preCommentSupplier = result ->
                    "Robot did not perform the expected action (action number %d)".formatted(finalI);
                if (i < expectedTransitionsSize - 3) {  // moving
                    assertEquals(Transition.RobotAction.MOVE, transition.action, context, preCommentSupplier);
                } else if (i < expectedTransitionsSize - 1) {  // left turns
                    assertEquals(Transition.RobotAction.TURN_LEFT, transition.action, context, preCommentSupplier);
                } else {  // last action (none)
                    assertEquals(Transition.RobotAction.NONE, transition.action, context, preCommentSupplier);
                }
            }
        }
    }

    @Test
    public void testDropStoneVAnforderung() {
        iterateMethodStatements("dropStone",
            new Class[] {int.class, RobotFamily[][].class, RobotFamily.class},
            iterator -> {
                int loopStatements = 0;

                while (iterator.hasNext()) {
                    CtElement ctElement = iterator.next();
                    if (ctElement instanceof CtFor || ctElement instanceof CtWhile || ctElement instanceof CtDo) {
                        loopStatements++;
                    }
                }
                assertEquals(1, loopStatements, emptyContext(), result -> "Method does not use exactly one loop");
            });
    }

    private void testGetDestinationRow(JsonParameterSet params, boolean testFreeSlots) {
        int worldHeight = params.getInt("worldHeight");
        int worldWidth = params.getInt("worldWidth");
        List<Integer> firstFreeIndex = params.get("firstFreeIndex");
        RobotFamily[][] gameBoard = new RobotFamily[worldHeight][worldWidth];
        List<List<String>> paramStones = params.get("gameBoard");
        for (int row = 0; row < worldHeight; row++) {
            for (int col = 0; col < worldWidth; col++) {
                gameBoard[row][col] = paramStones.get(row).get(col) != null ? RobotFamily.SQUARE_RED : null;
            }
        }

        World.setSize(worldWidth, worldHeight);
        for (int i = 0; i < firstFreeIndex.size(); i++) {
            int index = firstFreeIndex.get(i);
            if ((testFreeSlots && index >= worldHeight) || (!testFreeSlots && index < worldHeight)) {
                continue;
            }

            final int column = i;
            int expected = testFreeSlots ? index : -1;
            Context context = contextBuilder()
                .add("column", column)
                .add("stones", gameBoard)
                .build();
            int actual = callObject(() -> FourWins.getDestinationRow(column, gameBoard), context, result ->
                "An exception occurred while invoking method getDestinationRow");
            assertEquals(expected, actual, context, result ->
                "Method getDestinationRow returned an incorrect value");
        }
    }

    private static void iterateMethodStatements(String methodName, Class<?>[] paramTypes, Consumer<Iterator<CtElement>> consumer) {
        Iterator<CtElement> iterator = getCtMethod(methodName, paramTypes)
            .getBody()
            .descendantIterator();
        consumer.accept(iterator);
    }

    private static CtMethod<?> getCtMethod(String methodName, Class<?>[] paramTypes) {
        return SpoonUtils.getType(FourWins.class.getName())
            .getMethodsByName(methodName)
            .stream()
            .filter(ctMethod -> {
                List<CtParameter<?>> parameters = ctMethod.getParameters();
                boolean result = parameters.size() == paramTypes.length;
                for (int i = 0; result && i < parameters.size(); i++) {
                    result = parameters.get(i).getType().getQualifiedName().equals(paramTypes[i].getTypeName());
                }
                return result;
            })
            .findAny()
            .orElseThrow();
    }
}
