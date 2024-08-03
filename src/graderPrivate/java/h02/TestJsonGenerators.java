package h02;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;

import java.io.IOException;

public class TestJsonGenerators {
    @Test
    @DisabledIf("org.tudalgo.algoutils.tutor.general.Utils#isJagrRun()")
    public void generateOneDimensionalArrayStuffTestRandomNumbers() throws IOException {
        TestUtils.generateJsonTestData(
            (mapper, index, rnd) -> {
                final ObjectNode objectNode = mapper.createObjectNode();
                objectNode.put("value", rnd.nextInt((int) -2e5, (int) 2e5));
                final ArrayNode arrayNode = mapper.createArrayNode();
                if (index < 99) {
                    for (int i = 0; i < rnd.nextInt(5, 10); i++) {
                        arrayNode.add(rnd.nextInt((int) -2e5, (int) 2e5));
                    }
                }
                objectNode.set("array", arrayNode);
                return objectNode;
            },
            100,
            "OneDimensionalArrayStuffTestRandomNumbers.json"
        );
    }
}
