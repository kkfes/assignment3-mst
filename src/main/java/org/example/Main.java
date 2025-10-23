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

            results.add(graphResult);
        }


        JSONHandler.writeResultsToJSON(outputFilePath, Map.of("results", results));
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