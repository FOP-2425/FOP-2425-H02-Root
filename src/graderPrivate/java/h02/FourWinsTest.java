package h02;

import fopbot.RobotFamily;
import fopbot.World;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.annotation.SkipAfterFirstFailedTest;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;

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
public class FourWinsTest {

    @ParameterizedTest
    @JsonParameterSetTest(value = "fourWinsTestValidateInput.json")
    public void testValidateInputEdgeCases(final JsonParameterSet params) {
        testValidateInput(params);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "fourWinsTestValidateInputRandomCases.json")
    public void testValidateInputRandomCases(final JsonParameterSet params) {
        testValidateInput(params);
    }

    public void testValidateInput(final JsonParameterSet params) {
        // get params
        final int paramColumn = params.getInt("column");
        final int paramWidth = params.getInt("width");
        final int paramHeight = params.getInt("height");
        final List<List<String>> paramStones = params.get("stones");
        final boolean expectedResult = params.getBoolean("expected result");

        // write params to context
        final var ParamsContext = params.toContext("expected result");
        final var cb = contextBuilder()
            .add(ParamsContext)
            .add("Method", "validateInput");

        // init the world size
        World.setSize(paramWidth, paramHeight);


        // parse array and calculate result
        RobotFamily[][] paramStonesArray = paramStones.stream()
            .map(innerList -> innerList.stream()
                .map(str -> "EMPTY".equals(str) ? null : "SQUARE_RED".equals(str) ? RobotFamily.SQUARE_RED :
                    RobotFamily.SQUARE_BLUE)
                .toArray(RobotFamily[]::new))
            .toArray(RobotFamily[][]::new);



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

}
