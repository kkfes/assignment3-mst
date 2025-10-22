package org.example;

import java.util.*;

public class KruskalAlgorithm {

    public static MSTResult findMST(Graph graph) {
        if (graph.getVertices() == 0 || graph.getEdges().isEmpty()) {
            return new MSTResult(new ArrayList<>(), 0, 0);
        }

        List<Edge> edges = new ArrayList<>(graph.getEdges());
        edges.sort(Comparator.comparingInt(Edge::getWeight));

        int[] parent = new int[graph.getVertices()];
        for (int i = 0; i < parent.length; i++) {
            parent[i] = i;
        }

        List<Edge> mstEdges = new ArrayList<>();
        int totalCost = 0;
        int operations = 0;

        for (Edge edge : edges) {
            int root1 = find(parent, edge.getSource());
            int root2 = find(parent, edge.getDestination());
            operations++;

            if (root1 != root2) {
                mstEdges.add(edge);
                totalCost += edge.getWeight();
                union(parent, root1, root2);
            }

            if (mstEdges.size() == graph.getVertices() - 1) {
                break;
            }
        }

        return new MSTResult(mstEdges, totalCost, operations);
    }

    private static int find(int[] parent, int vertex) {
        if (parent[vertex] != vertex) {
            parent[vertex] = find(parent, parent[vertex]);
        }
        return parent[vertex];
    }

    private static void union(int[] parent, int root1, int root2) {
        parent[root2] = root1;
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
