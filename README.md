# Assignment 3: Optimization of a City Transportation Network

## Objective
The goal of this assignment is to optimize a city's transportation network using Prim's and Kruskal's algorithms to find the Minimum Spanning Tree (MST). The MST minimizes the total construction cost while ensuring all districts are connected.

## Project Structure
- **src/main/java/org/example/**: Contains the implementation of the algorithms and supporting classes.
- **src/test/java/org/example/**: Contains automated tests for verifying correctness and performance.
- **data/**: Contains input datasets and output results in JSON format.

## Algorithms Implemented
1. **Prim's Algorithm**
2. **Kruskal's Algorithm**

## Features
- Reads graph data from JSON files.
- Computes MST using both algorithms.
- Records results (total cost, execution time, operations) in JSON format.
- Compares the efficiency and performance of the algorithms.

## How to Run
1. Ensure you have Java 21 and Maven installed.
2. Build the project:
   ```
   mvn clean install
   ```
3. Run the tests:
   ```
   mvn test
   ```

## Results
The results of the algorithms, including total cost, execution time, and operation count, are stored in `data/output.json`.

## Analysis
A detailed comparison of the algorithms' performance is provided in the report.

## References
- [Prim's Algorithm](https://en.wikipedia.org/wiki/Prim%27s_algorithm)
- [Kruskal's Algorithm](https://en.wikipedia.org/wiki/Kruskal%27s_algorithm)
