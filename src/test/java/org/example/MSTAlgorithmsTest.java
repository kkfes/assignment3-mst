package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class MSTAlgorithmsTest {

    @Test
    public void testPrimAlgorithm() {
        Graph graph = JSONHandler.readGraphFromJSON("data/input.json");
        assertNotNull(graph);

        PrimAlgorithm.MSTResult result = PrimAlgorithm.findMST(graph);
        assertNotNull(result);
        if (graph.getVertices() > 1 && !graph.getEdges().isEmpty()) {
            assertTrue(result.getTotalCost() > 0, "Total cost should be greater than 0 for connected graphs.");
        } else {
            assertEquals(0, result.getTotalCost(), "Total cost should be 0 for disconnected or empty graphs.");
        }
    }

    @Test
    public void testKruskalAlgorithm() {
        Graph graph = JSONHandler.readGraphFromJSON("data/input.json");
        assertNotNull(graph);

        KruskalAlgorithm.MSTResult result = KruskalAlgorithm.findMST(graph);
        assertNotNull(result);
        if (graph.getVertices() > 1 && !graph.getEdges().isEmpty()) {
            assertTrue(result.getTotalCost() > 0, "Total cost should be greater than 0 for connected graphs.");
        } else {
            assertEquals(0, result.getTotalCost(), "Total cost should be 0 for disconnected or empty graphs.");
        }
    }

    @Test
    public void testAlgorithmsComparison() {
        Graph graph = JSONHandler.readGraphFromJSON("data/input.json");
        assertNotNull(graph);

        PrimAlgorithm.MSTResult primResult = PrimAlgorithm.findMST(graph);
        KruskalAlgorithm.MSTResult kruskalResult = KruskalAlgorithm.findMST(graph);

        assertNotNull(primResult);
        assertNotNull(kruskalResult);
        assertEquals(primResult.getTotalCost(), kruskalResult.getTotalCost());
    }
}
