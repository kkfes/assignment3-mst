package org.example;

import java.util.*;

public class PrimAlgorithm {

    public static MSTResult findMST(Graph graph) {
        int vertices = graph.getVertices();
        boolean[] visited = new boolean[vertices];
        PriorityQueue<Edge> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(Edge::getWeight));
        List<Edge> mstEdges = new ArrayList<>();
        int totalCost = 0;

        // operation counters
        int comparisons = 0; // checks of visited status
        int queueAdds = 0;   // number of times an edge is added to PQ
        int polls = 0;       // number of times PQ.poll() is called

        if (vertices == 0 || graph.getEdges().isEmpty()) {
            return new MSTResult(new ArrayList<>(), 0, 0, 0, 0, 0);
        }

        // Start from vertex 0
        visited[0] = true;
        for (Edge edge : graph.getEdges()) {
            if (edge.getSource() == 0 || edge.getDestination() == 0) {
                priorityQueue.add(edge);
                queueAdds++;
            }
        }

        while (!priorityQueue.isEmpty() && mstEdges.size() < vertices - 1) {
            Edge edge = priorityQueue.poll();
            polls++;

            int nextVertex = -1;
            comparisons++;
            if (!visited[edge.getSource()]) {
                nextVertex = edge.getSource();
            } else {
                comparisons++;
                if (!visited[edge.getDestination()]) {
                    nextVertex = edge.getDestination();
                }
            }

            if (nextVertex != -1) {
                visited[nextVertex] = true;
                mstEdges.add(edge);
                totalCost += edge.getWeight();

                for (Edge nextEdge : graph.getEdges()) {
                    comparisons++;
                    if ((nextEdge.getSource() == nextVertex && !visited[nextEdge.getDestination()]) ||
                        (nextEdge.getDestination() == nextVertex && !visited[nextEdge.getSource()])) {
                        priorityQueue.add(nextEdge);
                        queueAdds++;
                    }
                }
            }
        }

        int totalOperations = comparisons + queueAdds + polls;
        return new MSTResult(mstEdges, totalCost, totalOperations, comparisons, queueAdds, polls);
    }

    public static class MSTResult {
        private final List<Edge> edges;
        private final int totalCost;
        private final int operations; // summary

        // detailed
        private final int comparisons;
        private final int queueAdds;
        private final int polls;

        public MSTResult(List<Edge> edges, int totalCost, int operations, int comparisons, int queueAdds, int polls) {
            this.edges = edges;
            this.totalCost = totalCost;
            this.operations = operations;
            this.comparisons = comparisons;
            this.queueAdds = queueAdds;
            this.polls = polls;
        }

        // legacy constructor for compatibility
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
        public int getQueueAdds() { return queueAdds; }
        public int getPolls() { return polls; }
    }
}
