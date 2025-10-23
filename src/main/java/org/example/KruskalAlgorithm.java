package org.example;

import java.util.*;

public class KruskalAlgorithm {

    public static MSTResult findMST(Graph graph) {
        if (graph.getVertices() == 0 || graph.getEdges().isEmpty()) {
            return new MSTResult(new ArrayList<>(), 0, 0, 0, 0, 0);
        }

        List<Edge> edges = new ArrayList<>(graph.getEdges());
        edges.sort(Comparator.comparingInt(Edge::getWeight));

        int[] parent = new int[graph.getVertices()];
        for (int i = 0; i < parent.length; i++) {
            parent[i] = i;
        }

        List<Edge> mstEdges = new ArrayList<>();
        int totalCost = 0;

        // operation counters
        int comparisons = 0; // checks whether roots are equal
        int findCalls = 0;
        int unionCalls = 0;

        for (Edge edge : edges) {
            findCalls++;
            int root1 = find(parent, edge.getSource());
            findCalls++;
            int root2 = find(parent, edge.getDestination());
            comparisons++;

            if (root1 != root2) {
                mstEdges.add(edge);
                totalCost += edge.getWeight();
                unionCalls++;
                union(parent, root1, root2);
            }

            if (mstEdges.size() == graph.getVertices() - 1) {
                break;
            }
        }

        int totalOperations = comparisons + findCalls + unionCalls;
        return new MSTResult(mstEdges, totalCost, totalOperations, comparisons, findCalls, unionCalls);
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
        private final int operations; // summary

        // detailed
        private final int comparisons;
        private final int findCalls;
        private final int unionCalls;

        public MSTResult(List<Edge> edges, int totalCost, int operations, int comparisons, int findCalls, int unionCalls) {
            this.edges = edges;
            this.totalCost = totalCost;
            this.operations = operations;
            this.comparisons = comparisons;
            this.findCalls = findCalls;
            this.unionCalls = unionCalls;
        }

        // legacy constructor
        public MSTResult(List<Edge> edges, int totalCost, int operations) {
            this(edges, totalCost, operations, 0, 0, 0);
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

        public int getComparisons() { return comparisons; }
        public int getFindCalls() { return findCalls; }
        public int getUnionCalls() { return unionCalls; }
    }
}
