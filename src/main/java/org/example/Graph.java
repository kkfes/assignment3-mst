package org.example;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private final int vertices;
    private final List<Edge> edges;
    private final List<String> nodeNames; // optional list of node names, may be null

    public Graph(int vertices) {
        this.vertices = vertices;
        this.edges = new ArrayList<>();
        this.nodeNames = null;
    }

    // new constructor that accepts node names (preserves order -> index mapping)
    public Graph(int vertices, List<String> nodeNames) {
        this.vertices = vertices;
        this.edges = new ArrayList<>();
        this.nodeNames = nodeNames;
    }

    public int getVertices() {
        return vertices;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void addEdge(int source, int destination, int weight) {
        edges.add(new Edge(source, destination, weight));
    }

    public List<String> getNodeNames() {
        return nodeNames;
    }
}
