package h02;

import org.sourcegrade.jagr.api.rubric.Criterion;
import org.sourcegrade.jagr.api.rubric.JUnitTestRef;
import org.sourcegrade.jagr.api.rubric.Rubric;
import org.sourcegrade.jagr.api.rubric.RubricProvider;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;

import static org.tudalgo.algoutils.tutor.general.jagr.RubricUtils.criterion;


public class H02_RubricProvider implements RubricProvider {

    public static final Rubric RUBRIC = Rubric.builder()
        .title("H01 | Foreign Contaminants")
        .addChildCriteria(
            Criterion.builder()
                .shortDescription("H2.1 | Grundlagen-Training")
                .addChildCriteria(
                    Criterion.builder()
                        .shortDescription("H2.1.1 | Fibonacci mit 1D Array")
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
                .shortDescription("H2.2 | Roboter-Training")
                .addChildCriteria(
                    Criterion.builder()
                        .shortDescription("H2.2.1 | Slot Prüfen")
                        .addChildCriteria(
                            criterion(
                                "Die Methode validateInput ist vollständig korrekt implementiert."
                            )
                        )
                        .build(),
                    Criterion.builder()
                        .shortDescription("H2.2.2 | Münzen fallen lassen")
                        .addChildCriteria(
                            criterion(
                                "Methode getDestinationRow: die Rückgabe ist korrekt, wenn ein freier Slot existiert."
                            ),
                            criterion(
                                "Methode getDestinationRow: die Rückgabe ist korrekt, wenn kein freier Slot existiert."
                            )
                            ,
                            criterion(
                                "Methode dropStone: die Rückgabe ist korrekt, wenn kein freier Slot existiert."
                            )
                        )
                        .build(),
                    Criterion.builder()
                        .shortDescription("H2.2.2 | Münzen fallen lassen")
                        .addChildCriteria(
                            criterion(
                                "Methode getDestinationRow: die Rückgabe ist korrekt, wenn ein freier Slot existiert."
                            ),
                            criterion(
                                "Methode getDestinationRow: die Rückgabe ist korrekt, wenn kein freier Slot existiert."
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
