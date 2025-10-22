# Assignment 3: Minimum Spanning Tree (MST) Algorithms

## Overview
This project implements and compares two algorithms for finding the Minimum Spanning Tree (MST) of a graph:
- **Prim's Algorithm**
- **Kruskal's Algorithm**

The program processes graphs described in a JSON file (`input.json`) and outputs the results of the algorithms in another JSON file (`output.json`).

## Input Data
The input file `input.json` contains a list of graphs. Each graph is defined by:
- **`vertices`**: The number of vertices in the graph.
- **`edges`**: A list of edges, where each edge is represented as:
  - `source`: The starting vertex of the edge.
  - `destination`: The ending vertex of the edge.
  - `weight`: The weight of the edge.

### Example Input:
```json
{
  "vertices": 5,
  "edges": [
    { "source": 0, "destination": 1, "weight": 4 },
    { "source": 0, "destination": 2, "weight": 3 },
    { "source": 1, "destination": 2, "weight": 2 },
    { "source": 1, "destination": 3, "weight": 5 },
    { "source": 3, "destination": 4, "weight": 6 }
  ]
}
```

## Output Data
The output file `output.json` contains the results for each graph, including:
- **`input_stats`**: The number of vertices and edges in the graph.
- **`prim`** and **`kruskal`**: Results of the respective algorithms, including:
  - `mst_edges`: The edges in the MST.
  - `total_cost`: The total weight of the MST.
  - `execution_time_ms`: The time taken to compute the MST (in milliseconds).
  - `operations_count`: The number of operations performed by the algorithm.

### Example Output:
```json
{
  "results": [
    {
      "graph_id": 1,
      "input_stats": {
        "vertices": 5,
        "edges": 7
      },
      "prim": {
        "mst_edges": [
          { "source": 0, "destination": 2, "weight": 3 },
          { "source": 1, "destination": 2, "weight": 2 },
          { "source": 1, "destination": 3, "weight": 5 },
          { "source": 3, "destination": 4, "weight": 6 }
        ],
        "total_cost": 16,
        "execution_time_ms": 4.0785,
        "operations_count": 5
      },
      "kruskal": {
        "mst_edges": [
          { "source": 1, "destination": 2, "weight": 2 },
          { "source": 0, "destination": 2, "weight": 3 },
          { "source": 1, "destination": 3, "weight": 5 },
          { "source": 3, "destination": 4, "weight": 6 }
        ],
        "total_cost": 16,
        "execution_time_ms": 2.0673,
        "operations_count": 5
      }
    }
  ]
}
```

## Comparison of Algorithms
The project provides a detailed comparison of Prim's and Kruskal's algorithms based on:
- **Execution Time**: Measured in milliseconds.
- **Operations Count**: The number of operations performed during MST computation.
- **MST Edges and Total Cost**: Both algorithms produce the same MST for the same graph, ensuring correctness.

### Observations:
1. **Prim's Algorithm**:
   - Performs better on dense graphs due to its greedy approach.
   - Execution time: Slightly higher for sparse graphs.
2. **Kruskal's Algorithm**:
   - Performs better on sparse graphs due to edge sorting.
   - Execution time: Faster for graphs with fewer edges.

## How to Run
1. Place the input file `input.json` in the `data/` directory.
2. Run the program using the `Main` class.
3. The results will be saved in `data/output.json`.

## Requirements
- Java 11 or higher.
- Dependencies:
  - Gson (for JSON parsing).

## Conclusion
This project demonstrates the implementation and comparison of two classic MST algorithms. The results highlight their strengths and weaknesses, providing insights into their performance on different types of graphs.
