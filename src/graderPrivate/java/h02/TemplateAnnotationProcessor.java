package h02;

import org.junit.jupiter.api.Test;
import org.tudalgo.algoutils.student.annotation.SolutionOnly;
import org.tudalgo.algoutils.student.annotation.StudentCreationRequired;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;
import org.tudalgo.algoutils.tutor.general.reflections.BasicPackageLink;
import org.tudalgo.algoutils.tutor.general.reflections.BasicTypeLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;
import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtRecord;
import spoon.reflect.factory.Factory;
import spoon.support.sniper.SniperJavaPrettyPrinter;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static org.tudalgo.algoutils.tutor.general.ResourceUtils.toPathString;

public class TemplateAnnotationProcessor {
    private static Launcher createSpoonLauncher(final boolean sniper) {
        final Launcher spoon = new Launcher();
        spoon.getEnvironment().setAutoImports(true);
        spoon.getEnvironment().setComplianceLevel(17);
        spoon.getEnvironment().setNoClasspath(true);
        spoon.getEnvironment().setCommentEnabled(true);
        spoon.getEnvironment().setIgnoreSyntaxErrors(true);
        if (sniper) {
            spoon.getEnvironment().setPrettyPrinterCreator(
                () -> new SniperJavaPrettyPrinter(spoon.getEnvironment())
            );
        }
        return spoon;
    }

    @Test
    public void execute() {
        // final Collection<BasicTypeLink> types = List.of(BasicTypeLink.of(TestClass.class));
        final Collection<BasicTypeLink> types = BasicPackageLink.of("h02", true).getTypes();
        Path tmpDir = null;
        try {
            tmpDir = Files.createTempDirectory("spoon_output");
            System.out.println("tmpdir: " + tmpDir.toString());
            for (final BasicTypeLink type : types) {
                if (type.reflection().getName().contains("TemplateAnnotationProcessor") || type.reflection().getName().contains("TestClass")) {
                    continue;
                }
                System.out.println("processing " + type.name());
                final AtomicBoolean modified = new AtomicBoolean(false);
                Launcher spoon = createSpoonLauncher(true);
                spoon.addInputResource(getFilePath(type).toString());
                CtModel model = spoon.buildModel();
                var spoonClass = model.getAllTypes().stream().filter(it -> it.getQualifiedName().equals(type.reflection().getName())).findFirst().orElse(null);
                if (spoonClass == null || spoonClass instanceof CtRecord) {
                    // workaround for records because spoon hates them
                    spoon = createSpoonLauncher(false);
                    spoon.addInputResource(getFilePath(type).toString());
                    model = spoon.buildModel();
                    spoonClass = model.getAllTypes().stream().filter(it -> it.getQualifiedName().equals(type.reflection().getName())).findFirst().orElseThrow();
                }
                final Factory factory = spoon.getFactory();
                processAnnotation(spoonClass, StudentImplementationRequired.class, element -> {
                    // get annotation text
                    final var exercise = element.getAnnotation(StudentImplementationRequired.class).value();
                    if (element instanceof final CtMethod<?> method) {
                        modified.set(true);
                        // Create a new code snippet statement with the desired code
                        String optionalReturn = "";
                        if (!method.getType().getSimpleName().equals("void")) {
                            optionalReturn = "return ";
                        }
                        final CtCodeSnippetStatement snippet = method.getFactory().Code().createCodeSnippetStatement("// TODO: " + exercise + "\n" + optionalReturn + "org.tudalgo.algoutils.student.Student.crash(\"" + exercise + " - Remove if implemented\")");
                        // Set the code snippet as the body of the method
                        method.setBody(snippet);
                    }
                });
                processAnnotation(spoonClass, Set.of(SolutionOnly.class, StudentCreationRequired.class), element -> {
                    if (element instanceof final CtMethod<?> method) {
                        modified.set(true);
                        // remove annotations
                        method.getAnnotations().forEach(CtAnnotation::delete);
                        // remove method
                        method.delete();
                    }
                });
                if (!modified.get()) {
                    continue;
                }
                // Save the modified class
                spoon.getEnvironment().setSourceOutputDirectory(new File(tmpDir.toString()));
                // overwrite the file if it already exists
//                spoon.getEnvironment().setAutoImports(true);

                spoon.prettyprint();
                final var path = getFilePath(type);
                final var outputPath = Path.of(tmpDir.toString(), toPathString(type.reflection().getName()));
                Files.deleteIfExists(path);
                Files.move(outputPath, path);
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        } finally {
            try (final var walk = Files.walk(tmpDir)) {
                walk.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
            } catch (final IOException e) {
                throw new RuntimeException("Could not delete tmp dir", e);
            }
        }
    }

    public static Path getFilePath(final TypeLink type) {
        final String pathString = toPathString(type.reflection().getName());
        // find the file
        try (final var walk = Files.walk(Path.of("."))) {
            return walk.filter(p -> p.endsWith(pathString)).findFirst().orElseThrow();
        } catch (final IOException e) {
            throw new RuntimeException("an error occurred while reading a source files ", e);
        }
    }

    public static void processAnnotation(
        final CtElement root, final Set<Class<? extends Annotation>> annotations,
        final Consumer<CtElement> consumer
    ) {
        CtElement element = root.filterChildren(c -> annotations.stream().anyMatch(c::hasAnnotation)).first();
        final Set<CtElement> processed = new HashSet<>();
        while (element != null && !processed.contains(element)) {
            consumer.accept(element);
            processed.add(element);
            element = root.filterChildren(c -> !processed.contains(c) && annotations.stream().anyMatch(c::hasAnnotation)).first();
        }
    }

    public static void processAnnotation(
        final CtElement root, final Class<? extends Annotation> annotation,
        final Consumer<CtElement> consumer
    ) {
        processAnnotation(root, Set.of(annotation), consumer);
    }
}
