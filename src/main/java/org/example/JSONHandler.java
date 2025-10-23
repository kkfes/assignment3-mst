package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
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
            JsonElement root = JsonParser.parseReader(reader);

            // If the file contains an object with "graphs": [ ... ] take the first graph
            if (root.isJsonObject() && root.getAsJsonObject().has("graphs")) {
                JsonElement graphsElem = root.getAsJsonObject().get("graphs");
                if (graphsElem.isJsonArray() && graphsElem.getAsJsonArray().size() > 0) {
                    JsonElement firstGraphElem = graphsElem.getAsJsonArray().get(0);
                    GraphData graphData = gson.fromJson(firstGraphElem, GraphData.class);
                    return convertGraphDataToGraph(graphData);
                }
            }

            // Try to parse as a single GraphData object (either with nodes/from/to or vertices/source/destination)
            GraphData maybeGraph = gson.fromJson(root, GraphData.class);
            if (maybeGraph != null) {
                // If it's in the form with nodes/from/to
                if (maybeGraph.getNodes() != null && !maybeGraph.getNodes().isEmpty()) {
                    return convertGraphDataToGraph(maybeGraph);
                }
                // If it's in the form with numeric vertices and edges with source/destination
                if (maybeGraph.getVertices() > 0 && maybeGraph.getEdgesByIndex() != null) {
                    Graph graph = new Graph(maybeGraph.getVertices());
                    for (GraphData.EdgeData edge : maybeGraph.getEdgesByIndex()) {
                        graph.addEdge(edge.getSource(), edge.getDestination(), edge.getWeight());
                    }
                    return graph;
                }
            }

            return null;
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

    private static Graph convertGraphDataToGraph(GraphData graphData) {
        Map<String, Integer> nodeIndexMap = mapNodeNamesToIndices(graphData.getNodes());
        Graph graph = new Graph(nodeIndexMap.size(), graphData.getNodes());

        if (graphData.getEdges() != null) {
            for (GraphData.EdgeData edge : graphData.getEdges()) {
                int source = nodeIndexMap.get(edge.getFrom());
                int destination = nodeIndexMap.get(edge.getTo());
                graph.addEdge(source, destination, edge.getWeight());
            }
        }

        return graph;
    }

    public static List<Graph> readGraphsFromJSON(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            Type graphsType = new TypeToken<GraphsData>() {}.getType();
            GraphsData graphsData = gson.fromJson(reader, graphsType);

            List<Graph> graphs = new ArrayList<>();
            if (graphsData != null && graphsData.getGraphs() != null) {
                for (GraphData graphData : graphsData.getGraphs()) {
                    graphs.add(convertGraphDataToGraph(graphData));
                }
            }
            return graphs;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static class GraphData {
        // this class supports two variants of input:
        // 1) { "nodes": ["A","B"], "edges": [{"from":"A","to":"B","weight":1}] }
        // 2) { "vertices": 3, "edges": [{"source":0,"destination":1,"weight":1}] }

        private int vertices;
        private List<EdgeData> edgesByIndex;
        private List<EdgeData> edges;
        private List<String> nodes;

        public int getVertices() {
            return vertices;
        }

        // Gson will map both forms into EdgeData objects; for numeric-edge format edges may be in edgesByIndex
        public List<EdgeData> getEdgesByIndex() {
            return edgesByIndex != null ? edgesByIndex : edges;
        }

        public List<EdgeData> getEdges() {
            return edges;
        }

        public List<String> getNodes() {
            return nodes;
        }

        private static class EdgeData {
            // supports both index-based and name-based representations
            private Integer source;
            private Integer destination;
            private Integer weight;
            private String from;
            private String to;

            public int getSource() {
                return source != null ? source : -1;
            }

            public int getDestination() {
                return destination != null ? destination : -1;
            }

            public int getWeight() {
                return weight != null ? weight : 0;
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
