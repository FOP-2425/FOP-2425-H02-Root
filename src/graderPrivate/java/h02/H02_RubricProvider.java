package h02;

import org.sourcegrade.jagr.api.rubric.Criterion;
import org.sourcegrade.jagr.api.rubric.JUnitTestRef;
import org.sourcegrade.jagr.api.rubric.Rubric;
import org.sourcegrade.jagr.api.rubric.RubricProvider;
import org.sourcegrade.jagr.api.testing.RubricConfiguration;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;

import static org.tudalgo.algoutils.tutor.general.jagr.RubricUtils.criterion;

public class H02_RubricProvider implements RubricProvider {

    public static final Rubric RUBRIC = Rubric.builder()
        .title("H02 | Vier Gewinnt")
        .addChildCriteria(
            Criterion.builder()
                .shortDescription("H2.1 | Grundlagen-Training")
                .minPoints(0)
                .addChildCriteria(
                    Criterion.builder()
                        .shortDescription("H2.1.1 | Fibonacci mit 1D Array")
                        .minPoints(0)
                        .addChildCriteria(
                            criterion(
                                "Methode push: Das letzte Element des Ergebnis-Arrays ist das übergebene Element.",
                                JUnitTestRef.ofMethod(
                                    () -> OneDimensionalArrayStuffTest.class.getDeclaredMethod(
                                        "testPushLastElementCorrect",
                                        JsonParameterSet.class
                                    )
                                )
                            ),
                            criterion(
                                "Methode push: Die Elemente des Ergebnis-Arrays sind korrekt.",
                                JUnitTestRef.ofMethod(
                                    () -> OneDimensionalArrayStuffTest.class.getDeclaredMethod(
                                        "testPushAllElementsCorrect",
                                        JsonParameterSet.class
                                    )
                                )
                            ),
                            criterion(
                                "Methode push: Das Eingabe-Array wird nicht verändert.",
                                JUnitTestRef.ofMethod(
                                    () -> OneDimensionalArrayStuffTest.class.getDeclaredMethod(
                                        "testPushOriginalArrayUnchanged",
                                        JsonParameterSet.class
                                    )
                                )
                            ),
                            criterion(
                                "Methode calculateNextFibonacci: Das Ergebnis ist korrekt mit zwei positiven Zahlen.",
                                JUnitTestRef.ofMethod(
                                    () -> OneDimensionalArrayStuffTest.class.getDeclaredMethod(
                                        "testCalculateNextFibonacciPositiveOnly", JsonParameterSet.class)
                                )
                            ),
                            criterion(
                                "Methode calculateNextFibonacci: Das Ergebnis ist korrekt mit beliebigen Eingaben.",
                                JUnitTestRef.ofMethod(
                                    () -> OneDimensionalArrayStuffTest.class.getDeclaredMethod(
                                        "testCalculateNextFibonacciAllNumbers", JsonParameterSet.class)
                                )
                            ),
                            criterion(
                                "Methode calculateNextFibonacci: Eine verbindliche Anforderung wurde verletzt.",
                                JUnitTestRef.ofMethod(
                                    () -> OneDimensionalArrayStuffTest.class.getDeclaredMethod(
                                        "testCalculateNextFibonacciVanforderungen", JsonParameterSet.class)
                                ),
                                -1
                            ),
                            criterion(
                                "Methode fibonacci: Das Ergebnis ist korrekt für n < 2.",
                                JUnitTestRef.ofMethod(
                                    () -> OneDimensionalArrayStuffTest.class.getDeclaredMethod(
                                        "testFibonacciSmallerThanTwo", JsonParameterSet.class)
                                )
                            ),
                            criterion(
                                "Methode fibonacci: Das Ergebnis ist korrekt für n >= 2.",
                                JUnitTestRef.ofMethod(
                                    () -> OneDimensionalArrayStuffTest.class.getDeclaredMethod(
                                        "testFibonacciBigNumbers", JsonParameterSet.class)
                                )
                            ),
                            criterion(
                                "Methode fibonacci: Eine Verbindliche Anforderung wurde verletzt.",
                                JUnitTestRef.and(
                                    JUnitTestRef.ofMethod(
                                        () -> OneDimensionalArrayStuffTest.class.getDeclaredMethod(
                                            "testFibonacciVanforderungen", JsonParameterSet.class)
                                    ),
                                    JUnitTestRef.ofMethod(
                                        () -> OneDimensionalArrayStuffTest.class.getDeclaredMethod(
                                            "testFibonacciNonIterativeVanforderungen")
                                    )
                                ),
                                -1
                            )
                        )
                        .build(),
                    Criterion.builder()
                        .shortDescription("H2.1.2 | Textsuche mit 2D Arrays")
                        .minPoints(0)
                        .addChildCriteria(
                            criterion(
                                "Methode occurrences: Die Methode funktioniert korrekt mit einem leeren Array.",
                                JUnitTestRef.ofMethod(
                                    () -> TwoDimensionalArrayStuffTest.class.getDeclaredMethod(
                                        "testOccurrencesEmptyArray", JsonParameterSet.class)
                                )
                            ),
                            criterion(
                                "Methode occurrences: Die Methode funktioniert mit einem Satz.",
                                JUnitTestRef.ofMethod(
                                    () -> TwoDimensionalArrayStuffTest.class.getDeclaredMethod(
                                        "testOccurrencesSingleSentence", JsonParameterSet.class)
                                )
                            ),
                            criterion(
                                "Methode occurrences: Die Methode funktioniert mit mehreren Sätzen.",
                                JUnitTestRef.ofMethod(
                                    () -> TwoDimensionalArrayStuffTest.class.getDeclaredMethod(
                                        "testOccurrencesMultipleSentences", JsonParameterSet.class)
                                )
                            ),
                            criterion(
                                "Methode mean: Methode funktioniert mit ganzzahligen Rechenwerten.",
                                JUnitTestRef.ofMethod(
                                    () -> TwoDimensionalArrayStuffTest.class.getDeclaredMethod(
                                        "testMeanInteger", JsonParameterSet.class)
                                )
                            ),
                            criterion(
                                "Methode mean: Die Methode funktioniert auch dann korrekt, wenn das Ergebnis eine fließkommazahl ist.",
                                JUnitTestRef.ofMethod(
                                    () -> TwoDimensionalArrayStuffTest.class.getDeclaredMethod(
                                        "testMeanFloat", JsonParameterSet.class)
                                )
                            )
                        )
                        .build()
                )
                .build()
        )
        .addChildCriteria(
            Criterion.builder()
                .shortDescription("H2.2 | Vier Gewinnt")
                .minPoints(0)
                .addChildCriteria(
                    Criterion.builder()
                        .shortDescription("H2.2.1 | Slot Prüfen")
                        .addChildCriteria(
                            criterion(
                                "Methode validateInput: Methode ist vollständig korrekt implementiert.",
                                JUnitTestRef.and(
                                    JUnitTestRef.ofMethod(
                                        () -> FourWinsTest.class.getDeclaredMethod(
                                            "testValidateInputEdgeCases", JsonParameterSet.class)
                                    ),
                                    JUnitTestRef.ofMethod(
                                        () -> FourWinsTest.class.getDeclaredMethod(
                                            "testValidateInputRandomCases", JsonParameterSet.class)
                                    )
                                )
                            )
                        )
                        .build(),
                    Criterion.builder()
                        .shortDescription("H2.2.2 | Münzen fallen lassen")
                        .minPoints(0)
                        .addChildCriteria(
                            criterion(
                                "Methode getDestinationRow: Die Rückgabe ist korrekt, wenn ein freier Slot existiert.",
                                JUnitTestRef.ofMethod(
                                    () -> FourWinsTest.class.getDeclaredMethod(
                                        "testGetDestinationRowFreeSlot", JsonParameterSet.class)
                                )
                            ),
                            criterion(
                                "Methode getDestinationRow: Die Rückgabe ist korrekt, wenn KEIN freier Slot existiert.",
                                JUnitTestRef.ofMethod(
                                    () -> FourWinsTest.class.getDeclaredMethod(
                                        "testGetDestinationRowBlockedSlot", JsonParameterSet.class)
                                )
                            ),
                            criterion(
                                "Methode getDestinationRow: Verbindliche Anforderung 'genau eine Schleife' wurde verletzt.",
                                JUnitTestRef.ofMethod(
                                    () -> FourWinsTest.class.getDeclaredMethod(
                                        "testGetDestinationRowVAnforderung")
                                ),
                                -1
                            ),
                            criterion(
                                "Methode dropStone: Robot wird mit korrekten Parametern erstellt.",
                                JUnitTestRef.ofMethod(
                                    () -> FourWinsTest.class.getDeclaredMethod(
                                        "testDropStoneRobotCorrect", JsonParameterSet.class)
                                )
                            ),
                            criterion(
                                "Methode dropStone: getDestinationRow wird korrekt aufgerufen.",
                                JUnitTestRef.ofMethod(
                                    () -> FourWinsTest.class.getDeclaredMethod(
                                        "testDropStoneCallsGetDestinationRow")
                                )
                            ),
                            criterion(
                                "Methode dropStone: Robot führt die korrekte Bewegung aus.",
                                JUnitTestRef.ofMethod(
                                    () -> FourWinsTest.class.getDeclaredMethod(
                                        "testDropStoneMovementCorrect", JsonParameterSet.class)
                                )
                            ),
                            criterion(
                                "Methode dropStone: Verbindliche Anforderung 'genau eine Schleife' wurde verletzt.",
                                JUnitTestRef.ofMethod(
                                    () -> FourWinsTest.class.getDeclaredMethod("testDropStoneVAnforderung")
                                ),
                                -1
                            )
                        )
                        .build(),
                    Criterion.builder()
                        .shortDescription("H2.2.3 | Gewinnbedingung prüfen")
                        .addChildCriteria(
                            Criterion.builder()
                                .shortDescription("Methode testWinHorizontal: ")
                                .addChildCriteria(
                                    criterion(
                                        "Methode erkennt richtige horizontale Steinfolgen.",
                                        JUnitTestRef.ofMethod(
                                            () -> FourWinsTest.class.getDeclaredMethod(
                                                "testTestWinHorizontal", JsonParameterSet.class)
                                        ),
                                        2
                                    ),
                                    criterion(
                                        "Methode nutzt genau zwei verschachtelte Schleifen.",
                                        JUnitTestRef.ofMethod(
                                            () -> FourWinsTest.class.getDeclaredMethod(
                                                "testTestWinHorizontalVAnforderung1")
                                        ),
                                        1
                                    ),
                                    criterion(
                                        "Methode erkennt keine falschen Steinfolgen.",
                                        JUnitTestRef.ofMethod(
                                            () -> FourWinsTest.class.getDeclaredMethod("testTestWinHorizontalVAnforderung2")
                                        ),
                                        -2
                                    )
                                ).minPoints(0).build(),
                            Criterion.builder()
                                .shortDescription("Methode testWinVertical: ")
                                .addChildCriteria(
                                    criterion(
                                        "Methode erkennt richtige vertikale Steinfolgen.",
                                        JUnitTestRef.ofMethod(
                                            () -> FourWinsTest.class.getDeclaredMethod(
                                                "testTestWinVertical", JsonParameterSet.class)
                                        ),
                                        2
                                    ),
                                    criterion(
                                        "Methode nutzt genau zwei verschachtelte Schleifen.",
                                        JUnitTestRef.ofMethod(
                                            () -> FourWinsTest.class.getDeclaredMethod(
                                                "testTestWinVerticalVAnforderung1")
                                        ),
                                        1
                                    ),
                                    criterion(
                                        "Methode erkennt keine falschen Steinfolgen.",
                                        JUnitTestRef.ofMethod(
                                            () -> FourWinsTest.class.getDeclaredMethod(
                                                "testTestWinVerticalVAnforderung2")
                                        ),
                                        -2
                                    )
                                ).minPoints(0).build(),
                            Criterion.builder()
                                .shortDescription("Methode testWinConditions: ")
                                .addChildCriteria(
                                    criterion(
                                        "Die Rückgabe ist in allen Fällen korrekt.",
                                        JUnitTestRef.ofMethod(
                                            () -> FourWinsTest.class.getDeclaredMethod(
                                                "testTestWinConditions", int.class)
                                        )
                                    ),
                                    criterion(
                                        "testWinHorizontal, testWinVertical und testWinDiagonal werden korrekt aufgerufen.",
                                        JUnitTestRef.ofMethod(
                                            () -> FourWinsTest.class.getDeclaredMethod(
                                                "testTestWinConditionsVAnforderung")
                                        )
                                    )
                                ).build()
                        )
                        .build(),
                    Criterion.builder()
                        .shortDescription("H2.2.4 | Game Loop")
                        .minPoints(0)
                        .addChildCriteria(
                            criterion(
                                "Methode nextPlayer: Die Rückgabe für beide RobotFamily.SQUARE_BLUE und SQUARE_RED korrekt.",
                                JUnitTestRef.ofMethod(
                                    () -> FourWinsTest.class.getDeclaredMethod(
                                        "testNextPlayer", boolean.class)
                                )
                            ),
                            criterion(
                                "Methode colorFieldBackground: Das Spielfeld wird korrekt eingefärbt.",
                                JUnitTestRef.ofMethod(
                                    () -> FourWinsTest.class.getDeclaredMethod(
                                        "testColorFieldBackground", boolean.class)
                                )
                            ),
                            criterion(
                                "Methoden writeDrawMessage, writeWinnerMessage: Die Ausgabe in die Konsole ist korrekt.",
                                JUnitTestRef.ofMethod(
                                    () -> FourWinsTest.class.getDeclaredMethod(
                                        "testWriteMessages")
                                )
                            ),
                            criterion(
                                "Methode gameLoop: nextPlayer wird mit korrektem Parameter aufgerufen.",
                                JUnitTestRef.ofMethod(
                                    () -> FourWinsTest.class.getDeclaredMethod(
                                        "testGameLoopCallsNextPlayer")
                                )
                            ),
                            criterion(
                                "Methode gameLoop: dropStone wird mit korrekten Parametern aufgerufen.",
                                JUnitTestRef.ofMethod(
                                    () -> FourWinsTest.class.getDeclaredMethod(
                                        "testGameLoopCallsDropStone")
                                )
                            ),
                            criterion(
                                "Methode gameLoop: testWinConditions wird mit korrekten Parametern aufgerufen.",
                                JUnitTestRef.ofMethod(
                                    () -> FourWinsTest.class.getDeclaredMethod(
                                        "testGameLoopCallsGetWinConditions")
                                )
                            )
                        )
                        .build()
                )
                .build()
        )
        .build();

    @Override
    public Rubric getRubric() {
        return RUBRIC;
    }

    @Override
    public void configure(RubricConfiguration configuration) {
        configuration.addTransformer(new SubmissionTransformer());
    }
}
