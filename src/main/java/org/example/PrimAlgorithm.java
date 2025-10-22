package org.example;

import java.util.*;

public class PrimAlgorithm {

    public static MSTResult findMST(Graph graph) {
        int vertices = graph.getVertices();
        boolean[] visited = new boolean[vertices];
        PriorityQueue<Edge> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(Edge::getWeight));
        List<Edge> mstEdges = new ArrayList<>();
        int totalCost = 0;
        int operations = 0;

        if (vertices == 0 || graph.getEdges().isEmpty()) {
            return new MSTResult(new ArrayList<>(), 0, 0);
        }

        // Start from vertex 0
        visited[0] = true;
        for (Edge edge : graph.getEdges()) {
            if (edge.getSource() == 0 || edge.getDestination() == 0) {
                priorityQueue.add(edge);
            }
        }

        while (!priorityQueue.isEmpty() && mstEdges.size() < vertices - 1) {
            Edge edge = priorityQueue.poll();
            operations++;

            int nextVertex = -1;
            if (!visited[edge.getSource()]) {
                nextVertex = edge.getSource();
            } else if (!visited[edge.getDestination()]) {
                nextVertex = edge.getDestination();
            }

            if (nextVertex != -1) {
                visited[nextVertex] = true;
                mstEdges.add(edge);
                totalCost += edge.getWeight();

                for (Edge nextEdge : graph.getEdges()) {
                    if ((nextEdge.getSource() == nextVertex && !visited[nextEdge.getDestination()]) ||
                        (nextEdge.getDestination() == nextVertex && !visited[nextEdge.getSource()])) {
                        priorityQueue.add(nextEdge);
                    }
                }
            }
        }

        return new MSTResult(mstEdges, totalCost, operations);
    }

    public static class MSTResult {
        private final List<Edge> edges;
        private final int totalCost;
        private final int operations;

        public MSTResult(List<Edge> edges, int totalCost, int operations) {
            this.edges = edges;
            this.totalCost = totalCost;
            this.operations = operations;
        }

        public List<Edge> getEdges() {
            return edges;
        }

        public int getTotalCost() {
            return totalCost;
        }

        public int getOperations() {
            return operations;
        }
    }
}
