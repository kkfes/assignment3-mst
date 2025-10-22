package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONHandler {

    public static Graph readGraphFromJSON(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            Type graphType = new TypeToken<GraphData>() {}.getType();
            GraphData graphData = gson.fromJson(reader, graphType);

            Graph graph = new Graph(graphData.getVertices());
            if (graphData.getEdges() != null) {
                for (GraphData.EdgeData edge : graphData.getEdges()) {
                    graph.addEdge(edge.getSource(), edge.getDestination(), edge.getWeight());
                }
            }
            return graph;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void writeResultsToJSON(String filePath, Object result) {
        try (FileWriter writer = new FileWriter(filePath)) {
            Gson gson = new Gson();
            gson.toJson(result, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Integer> mapNodeNamesToIndices(List<String> nodes) {
        Map<String, Integer> nodeIndexMap = new HashMap<>();
        for (int i = 0; i < nodes.size(); i++) {
            nodeIndexMap.put(nodes.get(i), i);
        }
        return nodeIndexMap;
    }

    public static Graph convertGraphDataToGraph(GraphData graphData) {
        Map<String, Integer> nodeIndexMap = mapNodeNamesToIndices(graphData.getNodes());
        Graph graph = new Graph(nodeIndexMap.size());

        for (GraphData.EdgeData edge : graphData.getEdges()) {
            int source = nodeIndexMap.get(edge.getFrom());
            int destination = nodeIndexMap.get(edge.getTo());
            graph.addEdge(source, destination, edge.getWeight());
        }

        return graph;
    }

    public static List<Graph> readGraphsFromJSON(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            Type graphsType = new TypeToken<GraphsData>() {}.getType();
            GraphsData graphsData = gson.fromJson(reader, graphsType);

            List<Graph> graphs = new ArrayList<>();
            for (GraphData graphData : graphsData.getGraphs()) {
                graphs.add(convertGraphDataToGraph(graphData));
            }
            return graphs;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static class GraphData {
        private int vertices;
        private List<EdgeData> edges;
        private List<String> nodes;

        public int getVertices() {
            return vertices;
        }

        public List<EdgeData> getEdges() {
            return edges;
        }

        public List<String> getNodes() {
            return nodes;
        }

        private static class EdgeData {
            private int source;
            private int destination;
            private int weight;
            private String from;
            private String to;

            public int getSource() {
                return source;
            }

            public int getDestination() {
                return destination;
            }

            public int getWeight() {
                return weight;
            }

            public String getFrom() {
                return from;
            }

            public String getTo() {
                return to;
            }
        }
    }

    private static class GraphsData {
        private List<GraphData> graphs;

        public List<GraphData> getGraphs() {
            return graphs;
        }
    }
}
