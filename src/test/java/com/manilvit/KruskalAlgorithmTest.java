package com.manilvit;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class KruskalAlgorithmTest {

    /**
     * Test for a graph with a single node.
     * Expect the MST to be empty since there are no edges.
     */
    @Test
    public void testSingleNodeGraph() {
        List<NetworkConnection> edges = new ArrayList<>();
        int numNodes = 1;
        List<NetworkConnection> mst = KruskalAlgorithm.findMinimumSpanningTree(edges, numNodes);
        assertTrue(mst.isEmpty(), "MST should be empty for a graph with a single node");
    }

    /**
     * Test for a graph with two nodes and a single edge.
     * Expect the MST to contain the single edge.
     */
    @Test
    public void testTwoNodesSingleEdge() {
        List<NetworkConnection> edges = new ArrayList<>();
        edges.add(new NetworkConnection(0, 1, 10));
        int numNodes = 2;
        List<NetworkConnection> mst = KruskalAlgorithm.findMinimumSpanningTree(edges, numNodes);
        assertEquals(1, mst.size(), "MST should contain one edge");
        assertEquals(new NetworkConnection(0, 1, 10), mst.get(0), "The single edge should be in the MST");
    }

    /**
     * Test for a simple connected graph (example problem).
     * Graph with 4 nodes and edges:
     *   (0,1,3), (1,2,1), (2,3,4), (0,3,2)
     * Expected MST: edges (1,2,1), (0,3,2), and (0,1,3) with a total cost of 6.
     */
    @Test
    public void testSimpleGraphMST() {
        List<NetworkConnection> edges = new ArrayList<>();
        edges.add(new NetworkConnection(0, 1, 3));
        edges.add(new NetworkConnection(1, 2, 1));
        edges.add(new NetworkConnection(2, 3, 4));
        edges.add(new NetworkConnection(0, 3, 2));
        int numNodes = 4;
        List<NetworkConnection> mst = KruskalAlgorithm.findMinimumSpanningTree(edges, numNodes);

        // For a connected graph with 4 nodes, MST should contain 3 edges
        assertEquals(3, mst.size(), "MST should contain n-1 edges for a connected graph");

        // Check the total cost
        int totalCost = mst.stream().mapToInt(NetworkConnection::getCost).sum();
        assertEquals(6, totalCost, "Total cost of MST should be 6");

        // Expected edges (order may vary)
        List<NetworkConnection> expectedEdges = Arrays.asList(
                new NetworkConnection(1, 2, 1),
                new NetworkConnection(0, 3, 2),
                new NetworkConnection(0, 1, 3)
        );
        for (NetworkConnection expected : expectedEdges) {
            assertTrue(mst.contains(expected), "MST should contain the edge: " + expected);
        }
    }

    /**
     * Test for a graph with a cycle.
     * Graph with 4 nodes and edges:
     *   (0,1,1), (1,2,1), (2,3,1), (3,0,1), (0,2,2)
     * Expected MST should contain 3 edges with a total cost of 3.
     */
    @Test
    public void testGraphWithCycle() {
        List<NetworkConnection> edges = new ArrayList<>();
        edges.add(new NetworkConnection(0, 1, 1));
        edges.add(new NetworkConnection(1, 2, 1));
        edges.add(new NetworkConnection(2, 3, 1));
        edges.add(new NetworkConnection(3, 0, 1));
        edges.add(new NetworkConnection(0, 2, 2));
        int numNodes = 4;
        List<NetworkConnection> mst = KruskalAlgorithm.findMinimumSpanningTree(edges, numNodes);

        assertEquals(3, mst.size(), "MST should contain 3 edges for a graph with 4 nodes");

        int totalCost = mst.stream().mapToInt(NetworkConnection::getCost).sum();
        // There can be different MSTs, but the minimum cost should be 3
        assertEquals(3, totalCost, "Total cost of MST should be 3");
    }

    /**
     * Test for a disconnected graph.
     * Graph with 4 nodes, where only 0-1 and 2-3 are connected.
     * Expect the algorithm to form a forest where the total number of edges is less than n-1.
     */
    @Test
    public void testDisconnectedGraph() {
        List<NetworkConnection> edges = new ArrayList<>();
        edges.add(new NetworkConnection(0, 1, 5));
        edges.add(new NetworkConnection(2, 3, 7));
        int numNodes = 4;
        List<NetworkConnection> mst = KruskalAlgorithm.findMinimumSpanningTree(edges, numNodes);
        // Expect 2 edges since the graph is disconnected
        assertEquals(2, mst.size(), "MST for a disconnected graph should contain fewer than n-1 edges");
    }
}