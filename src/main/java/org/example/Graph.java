package org.example;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private final int vertices;
    private final List<Edge> edges;

    public Graph(int vertices) {
        this.vertices = vertices;
        this.edges = new ArrayList<>();
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
}
