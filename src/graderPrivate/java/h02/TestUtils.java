package h02;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Random;

import static h02.TestConstants.RANDOM_SEED;

public abstract class TestUtils {
    public static <E extends ObjectNode> String generateJsonArrayString(final Iterator<E> iterator) {
        final ObjectMapper mapper = new ObjectMapper();
        final ArrayNode arrayNode = mapper.createArrayNode();
        while (iterator.hasNext()) {
            arrayNode.add(iterator.next());
        }
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrayNode);
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> void saveToFile(final String filename, final T content) {
        // Save content to file
        final File file = new File(filename);
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(file, content);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * A generator for JSON test data.
     */
    public interface JsonGenerator {
        /**
         * Generates a JSON object node.
         *
         * @param mapper The object mapper to use.
         * @param index  The index of the object node.
         * @param rnd    The random number generator to use.
         * @return The generated JSON object node.
         */
        ObjectNode generateJson(ObjectMapper mapper, int index, Random rnd);
    }

    /**
     * Generates and saves JSON test data.
     *
     * @param generator The generator to use.
     * @param amount    The amount of test data to generate.
     * @param fileName  The file name to save the test data to.
     * @throws IOException If an I/O error occurs.
     */
    public static void generateJsonTestData(final JsonGenerator generator, final int amount, final String fileName) throws IOException {
        final var seed = RANDOM_SEED;
        final var random = new java.util.Random(seed);
        final ObjectMapper mapper = new ObjectMapper();
        final ArrayNode arrayNode = mapper.createArrayNode();
        System.out.println("Generating test data with seed: " + seed);
        for (int i = 0; i < amount; i++) {
            arrayNode.add(generator.generateJson(mapper, i, random));
        }

        // convert `ObjectNode` to pretty-print JSON
//        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrayNode));

        final var path = Paths.get(
            "src",
            "graderPrivate",
            "resources",
            "h02",
            fileName
        ).toAbsolutePath();
        System.out.printf("Saving to file: %s%n", path);
        final var file = path.toFile();
        file.createNewFile();
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, arrayNode);
    }
}
