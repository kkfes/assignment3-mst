package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.FileWriter;
import java.io.IOException;

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

            List<String> nodeNames = graph.getNodeNames();

            // Prim's Algorithm
            long startTime = System.nanoTime();
            PrimAlgorithm.MSTResult primResult = PrimAlgorithm.findMST(graph);
            long endTime = System.nanoTime();
            double executionTimeMs = (endTime - startTime) / 1_000_000.0;

            Map<String, Object> primStats = new HashMap<>();
            primStats.put("mst_edges", convertEdgesToOutputFormat(primResult.getEdges(), nodeNames));
            primStats.put("total_cost", primResult.getTotalCost());
            primStats.put("operations_count", primResult.getOperations());
            primStats.put("execution_time_ms", executionTimeMs);
            // detailed counters if available
            primStats.put("comparisons", primResult.getComparisons());
            primStats.put("queue_adds", primResult.getQueueAdds());
            primStats.put("polls", primResult.getPolls());
            graphResult.put("prim", primStats);

            // Kruskal's Algorithm
            startTime = System.nanoTime();
            KruskalAlgorithm.MSTResult kruskalResult = KruskalAlgorithm.findMST(graph);
            endTime = System.nanoTime();
            executionTimeMs = (endTime - startTime) / 1_000_000.0;

            Map<String, Object> kruskalStats = new HashMap<>();
            kruskalStats.put("mst_edges", convertEdgesToOutputFormat(kruskalResult.getEdges(), nodeNames));
            kruskalStats.put("total_cost", kruskalResult.getTotalCost());
            kruskalStats.put("operations_count", kruskalResult.getOperations());
            kruskalStats.put("execution_time_ms", executionTimeMs);
            // detailed counters if available
            kruskalStats.put("comparisons", kruskalResult.getComparisons());
            kruskalStats.put("find_calls", kruskalResult.getFindCalls());
            kruskalStats.put("union_calls", kruskalResult.getUnionCalls());
            graphResult.put("kruskal", kruskalStats);

            // Comparison summary (Prim vs Kruskal) required by the assignment
            Map<String, Object> comparison = new HashMap<>();
            int primTotal = primResult.getTotalCost();
            int kruskalTotal = kruskalResult.getTotalCost();
            double primTime = (double) primStats.get("execution_time_ms");
            double kruskalTime = (double) kruskalStats.get("execution_time_ms");
            int primOps = primResult.getOperations();
            int kruskalOps = kruskalResult.getOperations();

            comparison.put("prim_total_cost", primTotal);
            comparison.put("kruskal_total_cost", kruskalTotal);
            comparison.put("cost_equal", primTotal == kruskalTotal);
            comparison.put("cost_difference", primTotal - kruskalTotal);

            comparison.put("prim_execution_time_ms", primTime);
            comparison.put("kruskal_execution_time_ms", kruskalTime);
            comparison.put("time_difference_ms", primTime - kruskalTime);
            comparison.put("faster_algorithm", (primTime < kruskalTime) ? "prim" : (primTime > kruskalTime) ? "kruskal" : "equal");

            comparison.put("prim_operations", primOps);
            comparison.put("kruskal_operations", kruskalOps);
            comparison.put("operations_difference", primOps - kruskalOps);

            graphResult.put("comparison", comparison);

            results.add(graphResult);
        }

        // Write detailed JSON output
        JSONHandler.writeResultsToJSON(outputFilePath, Map.of("results", results));

        // Also write a concise CSV summary for easy comparison/analysis
        String csvPath = "data/summary.csv";
        try (FileWriter csvWriter = new FileWriter(csvPath)) {
            // header
            csvWriter.append("graph_id,vertices,edges,prim_total_cost,kruskal_total_cost,cost_diff,prim_time_ms,kruskal_time_ms,time_diff_ms,prim_ops,kruskal_ops,ops_diff\n");
            for (Map<String, Object> graphResult : results) {
                Map<String, Integer> inputStats = (Map<String, Integer>) graphResult.get("input_stats");
                Map<String, Object> comparison = (Map<String, Object>) graphResult.get("comparison");
                csvWriter.append(String.valueOf(graphResult.get("graph_id"))).append(',');
                csvWriter.append(String.valueOf(inputStats.get("vertices"))).append(',');
                csvWriter.append(String.valueOf(inputStats.get("edges"))).append(',');
                csvWriter.append(String.valueOf(comparison.get("prim_total_cost"))).append(',');
                csvWriter.append(String.valueOf(comparison.get("kruskal_total_cost"))).append(',');
                csvWriter.append(String.valueOf(comparison.get("cost_difference"))).append(',');
                csvWriter.append(String.valueOf(comparison.get("prim_execution_time_ms"))).append(',');
                csvWriter.append(String.valueOf(comparison.get("kruskal_execution_time_ms"))).append(',');
                csvWriter.append(String.valueOf(comparison.get("time_difference_ms"))).append(',');
                csvWriter.append(String.valueOf(comparison.get("prim_operations"))).append(',');
                csvWriter.append(String.valueOf(comparison.get("kruskal_operations"))).append(',');
                csvWriter.append(String.valueOf(comparison.get("operations_difference"))).append('\n');
            }
            System.out.println("Summary CSV written to " + csvPath);
        } catch (IOException e) {
            System.out.println("Failed to write CSV summary: " + e.getMessage());
        }

        System.out.println("Results written to " + outputFilePath);
    }

    private static List<Map<String, Object>> convertEdgesToOutputFormat(List<Edge> edges, List<String> nodeNames) {
        List<Map<String, Object>> out = new ArrayList<>();
        for (Edge e : edges) {
            Map<String, Object> m = new HashMap<>();
            String from;
            String to;
            if (nodeNames != null && e.getSource() >= 0 && e.getSource() < nodeNames.size()) {
                from = nodeNames.get(e.getSource());
            } else {
                from = String.valueOf(e.getSource());
            }
            if (nodeNames != null && e.getDestination() >= 0 && e.getDestination() < nodeNames.size()) {
                to = nodeNames.get(e.getDestination());
            } else {
                to = String.valueOf(e.getDestination());
            }
            m.put("from", from);
            m.put("to", to);
            m.put("weight", e.getWeight());
            out.add(m);
        }
        return out;
    }
}