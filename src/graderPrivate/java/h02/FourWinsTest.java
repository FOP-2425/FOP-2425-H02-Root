package h02;

import fopbot.RobotFamily;
import fopbot.World;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.SpoonUtils;
import org.tudalgo.algoutils.tutor.general.annotation.SkipAfterFirstFailedTest;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;
import spoon.Launcher;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.CtType;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
        CtType<?> ctType = SpoonUtils.getType(FourWins.class.getName());
        CtMethod<?> getDestinationRowCtMethod = ctType.getMethodsByName("getDestinationRow")
            .stream()
            .filter(ctMethod -> {
                List<CtParameter<?>> parameters = ctMethod.getParameters();
                return parameters.size() == 2 &&
                    parameters.get(0).getType().getQualifiedName().equals("int") &&
                    parameters.get(1).getType().getQualifiedName().equals("fopbot.RobotFamily[][]");
            })
            .findAny()
            .orElseThrow();
        int loopStatements = 0;
        Iterator<CtElement> statementIterator = getDestinationRowCtMethod.getBody().descendantIterator();

        while (statementIterator.hasNext()) {
            CtElement ctElement = statementIterator.next();
            if (ctElement instanceof CtFor || ctElement instanceof CtWhile || ctElement instanceof CtDo) {
                loopStatements++;
            }
        }
        assertEquals(1, loopStatements, emptyContext(), result -> "Method does not use exactly one loop");
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
}
