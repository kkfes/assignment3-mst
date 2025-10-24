package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class MSTAlgorithmsTest {

    // helper union-find to validate acyclic and connected properties
    private int find(int[] parent, int v) {
        if (parent[v] != v) parent[v] = find(parent, parent[v]);
        return parent[v];
    }

    private void union(int[] parent, int a, int b) {
        int ra = find(parent, a);
        int rb = find(parent, b);
        parent[ra] = rb;
    }

    private boolean isAcyclic(List<Edge> edges, int vertices) {
        int[] parent = new int[vertices];
        for (int i = 0; i < vertices; i++) parent[i] = i;
        for (Edge e : edges) {
            int u = e.getSource();
            int v = e.getDestination();
            int ru = find(parent, u);
            int rv = find(parent, v);
            if (ru == rv) return false; // cycle
            union(parent, ru, rv);
        }
        return true;
    }

    private boolean isConnected(List<Edge> edges, int vertices) {
        if (vertices == 0) return true;
        int[] parent = new int[vertices];
        for (int i = 0; i < vertices; i++) parent[i] = i;
        for (Edge e : edges) union(parent, e.getSource(), e.getDestination());
        int root = find(parent, 0);
        for (int i = 1; i < vertices; i++) if (find(parent, i) != root) return false;
        return true;
    }

    @Test
    public void testAlgorithmsOnProvidedGraph() {
        Graph graph = JSONHandler.readGraphFromJSON("data/input.json");
        assertNotNull(graph, "Graph should be read from input.json");

        // Run Prim
        long t0 = System.nanoTime();
        PrimAlgorithm.MSTResult primResult = PrimAlgorithm.findMST(graph);
        long t1 = System.nanoTime();
        long primTimeMs = (t1 - t0) / 1_000_000;

        // Run Kruskal
        long t2 = System.nanoTime();
        KruskalAlgorithm.MSTResult kruskalResult = KruskalAlgorithm.findMST(graph);
        long t3 = System.nanoTime();
        long kruskalTimeMs = (t3 - t2) / 1_000_000;

        assertNotNull(primResult);
        assertNotNull(kruskalResult);

        // Costs must be equal
        assertEquals(primResult.getTotalCost(), kruskalResult.getTotalCost(), "Total MST cost must be identical");

        int v = graph.getVertices();

        // Both MSTs should have V - 1 edges for a connected graph
        assertEquals(v - 1, primResult.getEdges().size(), "Prim MST should have V-1 edges");
        assertEquals(v - 1, kruskalResult.getEdges().size(), "Kruskal MST should have V-1 edges");

        // Acyclic
        assertTrue(isAcyclic(primResult.getEdges(), v), "Prim MST should be acyclic");
        assertTrue(isAcyclic(kruskalResult.getEdges(), v), "Kruskal MST should be acyclic");

        // Connected
        assertTrue(isConnected(primResult.getEdges(), v), "Prim MST should connect all vertices");
        assertTrue(isConnected(kruskalResult.getEdges(), v), "Kruskal MST should connect all vertices");

        // Operation counts non-negative
        assertTrue(primResult.getOperations() >= 0, "Prim operations should be non-negative");
        assertTrue(kruskalResult.getOperations() >= 0, "Kruskal operations should be non-negative");

        // Execution times non-negative
        assertTrue(primTimeMs >= 0, "Prim execution time should be non-negative");
        assertTrue(kruskalTimeMs >= 0, "Kruskal execution time should be non-negative");

        // Reproducibility: running again should produce the same total cost
        PrimAlgorithm.MSTResult primResult2 = PrimAlgorithm.findMST(graph);
        KruskalAlgorithm.MSTResult kruskalResult2 = KruskalAlgorithm.findMST(graph);
        assertEquals(primResult.getTotalCost(), primResult2.getTotalCost(), "Prim results should be reproducible");
        assertEquals(kruskalResult.getTotalCost(), kruskalResult2.getTotalCost(), "Kruskal results should be reproducible");
    }

    @Test
    public void testAlgorithmsOnDisconnectedGraph() {
        // Build a simple disconnected graph: 4 vertices, only one edge between 0 and 1
        Graph g = new Graph(4);
        g.addEdge(0, 1, 10);

        PrimAlgorithm.MSTResult prim = PrimAlgorithm.findMST(g);
        KruskalAlgorithm.MSTResult kruskal = KruskalAlgorithm.findMST(g);

        // Both algorithms should handle gracefully: they may return fewer than V-1 edges
        assertNotNull(prim);
        assertNotNull(kruskal);
        assertTrue(prim.getEdges().size() < g.getVertices() - 1 || prim.getEdges().isEmpty(), "Prim should not produce full MST for disconnected graph");
        assertTrue(kruskal.getEdges().size() < g.getVertices() - 1 || kruskal.getEdges().isEmpty(), "Kruskal should not produce full MST for disconnected graph");
    }

    @Test
    public void testAllGraphsFromInputFile() {
        List<Graph> graphs = JSONHandler.readGraphsFromJSON("data/input.json");
        assertNotNull(graphs, "Should read list of graphs from input.json");
        for (int i = 0; i < graphs.size(); i++) {
            Graph g = graphs.get(i);
            PrimAlgorithm.MSTResult p = PrimAlgorithm.findMST(g);
            KruskalAlgorithm.MSTResult k = KruskalAlgorithm.findMST(g);
            assertNotNull(p);
            assertNotNull(k);
            assertEquals(p.getTotalCost(), k.getTotalCost(), "Graph " + i + ": Prim and Kruskal should have equal MST cost");
        }
    }
}
