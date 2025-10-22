package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        String inputFilePath = "data/input.json";
        String outputFilePath = "data/output.json";

        List<Graph> graphs = JSONHandler.readGraphsFromJSON(inputFilePath);
        if (graphs == null) {
            System.out.println("Failed to read graphs from input file.");
            return;
        }

        List<Map<String, Object>> results = new ArrayList<>();

        for (int i = 0; i < graphs.size(); i++) {
            Graph graph = graphs.get(i);
            Map<String, Object> graphResult = new HashMap<>();
            graphResult.put("graph_id", i + 1);

            Map<String, Integer> inputStats = new HashMap<>();
            inputStats.put("vertices", graph.getVertices());
            inputStats.put("edges", graph.getEdges().size());
            graphResult.put("input_stats", inputStats);

            // Prim's Algorithm
            long startTime = System.nanoTime();
            PrimAlgorithm.MSTResult primResult = PrimAlgorithm.findMST(graph);
            long endTime = System.nanoTime();
            double executionTimeMs = (endTime - startTime) / 1_000_000.0;

            Map<String, Object> primStats = new HashMap<>();
            primStats.put("mst_edges", primResult.getEdges());
            primStats.put("total_cost", primResult.getTotalCost());
            primStats.put("operations_count", primResult.getOperations());
            primStats.put("execution_time_ms", executionTimeMs);
            graphResult.put("prim", primStats);

            // Kruskal's Algorithm
            startTime = System.nanoTime();
            KruskalAlgorithm.MSTResult kruskalResult = KruskalAlgorithm.findMST(graph);
            endTime = System.nanoTime();
            executionTimeMs = (endTime - startTime) / 1_000_000.0;

            Map<String, Object> kruskalStats = new HashMap<>();
            kruskalStats.put("mst_edges", kruskalResult.getEdges());
            kruskalStats.put("total_cost", kruskalResult.getTotalCost());
            kruskalStats.put("operations_count", kruskalResult.getOperations());
            kruskalStats.put("execution_time_ms", executionTimeMs);
            graphResult.put("kruskal", kruskalStats);

            results.add(graphResult);
        }


        JSONHandler.writeResultsToJSON(outputFilePath, Map.of("results", results));
        System.out.println("Results written to " + outputFilePath);
    }
}