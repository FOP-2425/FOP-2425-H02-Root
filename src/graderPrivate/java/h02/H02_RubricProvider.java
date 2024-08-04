package h02;

import org.sourcegrade.jagr.api.rubric.Criterion;
import org.sourcegrade.jagr.api.rubric.JUnitTestRef;
import org.sourcegrade.jagr.api.rubric.Rubric;
import org.sourcegrade.jagr.api.rubric.RubricProvider;
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
                                "Methode fibonacci: eine Verbindliche Anforderung wurde verletzt.",
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
                                "Methode occurrences: Die Methode funktioniert korrekt mit einem leeren Array."
                            ),
                            criterion(
                                "Methode occurrences: Die Methode funktioniert mit einem Satz."
                            ),
                            criterion(
                                "Methode occurrences: Die Methode funktioniert mit mehreren Sätzen."
                            ),
                            criterion(
                                "Methode mean: Methode funktioniert mit ganzzahligen Rechenwerten."
                            ),
                            criterion(
                                "Methode mean: Die Methode funktioniert auch dann korrekt, wenn das Ergebnis eine fließkommazahl ist."
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
                                "Methode validateInput: Methode ist vollständig korrekt implementiert."
                            )
                        )
                        .build(),
                    Criterion.builder()
                        .shortDescription("H2.2.2 | Münzen fallen lassen")
                        .minPoints(0)
                        .addChildCriteria(
                            criterion(
                                "Methode getDestinationRow: die Rückgabe ist korrekt, wenn ein freier Slot existiert."
                            ),
                            criterion(
                                "Methode getDestinationRow: die Rückgabe ist korrekt, wenn KEIN freier Slot existiert."
                            ),
                            criterion(
                                "Methode getDestinationRow: Verbindliche Anforderung 'genau eine Schleife' wurde verletzt.",
                                JUnitTestRef.ofMethod(
                                    () -> OneDimensionalArrayStuffTest.class.getDeclaredMethod("noTestYet", JsonParameterSet.class)
                                ),
                                -1
                            ),
                            criterion(
                                "Methode dropStone: Robot wird mit korrekten Parametern erstellt."
                            ),
                            criterion(
                                "Methode dropStone: getDestinationRow wird korrekt aufgerufen."
                            ),
                            criterion(
                                "Methode dropStone: Robot führt die korrekte Bewegung aus."
                            ),
                            criterion(
                                "Methode dropStone: Verbindliche Anforderung 'genau eine Schleife' wurde verletzt.",
                                JUnitTestRef.ofMethod(
                                    () -> OneDimensionalArrayStuffTest.class.getDeclaredMethod("noTestYet", JsonParameterSet.class)
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
                                        "Methode nutzt genau zwei verschachtelte Schleifen.",
                                        JUnitTestRef.ofMethod(
                                            () -> FourWinsTest.class.getDeclaredMethod(
                                                "noTestYet", JsonParameterSet.class)
                                        ),
                                        1
                                    ),
                                    criterion(
                                        "Methode erkennt richtige horizontale Steinfolgen.",
                                        JUnitTestRef.ofMethod(
                                            () -> FourWinsTest.class.getDeclaredMethod(
                                                "noTestYet", JsonParameterSet.class)
                                        ),
                                        2
                                    ),
                                    criterion(
                                        "Methode erkennt keine falschen Steinfolgen.",
                                        JUnitTestRef.ofMethod(
                                            () -> FourWinsTest.class.getDeclaredMethod("noTestYet", JsonParameterSet.class)
                                        ),
                                        -3
                                    )
                                ).minPoints(0).build(),
                            Criterion.builder()
                                .shortDescription("Methode testWinVertical: ")
                                .addChildCriteria(
                                    criterion(
                                        "Methode nutzt genau zwei verschachtelte Schleifen.",
                                        JUnitTestRef.ofMethod(
                                            () -> FourWinsTest.class.getDeclaredMethod(
                                                "noTestYet", JsonParameterSet.class)
                                        ),
                                        1
                                    ),
                                    criterion(
                                        "Methode erkennt richtige vertikale Steinfolgen.",
                                        JUnitTestRef.ofMethod(
                                            () -> FourWinsTest.class.getDeclaredMethod(
                                                "noTestYet", JsonParameterSet.class)
                                        ),
                                        2
                                    ),
                                    criterion(
                                        "Methode erkennt keine falschen Steinfolgen.",
                                        JUnitTestRef.ofMethod(
                                            () -> FourWinsTest.class.getDeclaredMethod(
                                                "noTestYet", JsonParameterSet.class)
                                        ),
                                        -3
                                    )
                                ).minPoints(0).build(),
                            Criterion.builder()
                                .shortDescription("Methode testWinVertical: ")
                                .addChildCriteria(
                                    criterion(
                                        "testWinHorizontal, testWinVertical und testWinDiagonal werden korrekt aufgerufen."
                                    ),
                                    criterion(
                                        "die Rückgabe ist in allen Fällen korrekt."
                                    )
                                ).build()
                        )
                        .build(),
                    Criterion.builder()
                        .shortDescription("H2.2.4 | Game Loop")
                        .minPoints(0)
                        .addChildCriteria(
                            criterion(
                                "Methode nextPlayer: die Rückgabe für beide RobotFamily.SQUARE_BLUE und SQUARE_RED korrekt."
                            ),
                            criterion(
                                "Methode displayWinner: die Ausgabe in die Konsole ist korrekt."
                            ),
                            criterion(
                                "Methode displayWinner: das Spielfeld wird korrekt eingefärbt."
                            ),
                            criterion(
                                "Methode displayWinner: Verbindliche Anforderung 'genau zwei verschachtelte Schleifen' wurde verletzt.",
                                JUnitTestRef.ofMethod(
                                    () -> FourWinsTest.class.getDeclaredMethod("noTestYet", JsonParameterSet.class)
                                ),
                                -1
                            ),
                            criterion(
                                "Methode gameLoop: nextPlayer und testWinConditions werden korrekt aufgerufen."
                            ),
                            criterion(
                                "Methode gameLoop: dropStone wird mit korrekten Parametern aufgerufen."
                            ),
                            criterion(
                                "Methode gameLoop: displayWinner wird mit korrekten Parametern aufgerufen."
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

//    @Override
//    public void configure(final RubricConfiguration configuration) {
//        configuration.addTransformer(new AccessTransformer());
//    }
}
