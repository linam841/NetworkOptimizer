package com.manilvit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
/**
 * This class implements Kruskal's algorithm to find the Minimum Spanning Tree (MST) of a graph.
 * The algorithm uses a Union-Find data structure to efficiently merge sets of nodes.
 */
public class KruskalAlgorithm {

    /**
     * Finds the Minimum Spanning Tree (MST) of a graph using Kruskal's algorithm.
     *
     * @param edges A list of edges in the graph, where each edge is represented by a {@link NetworkConnection}.
     * @param numNodes The number of nodes in the graph.
     * @return A list of edges that form the MST.
     */
    public static List<NetworkConnection> findMinimumSpanningTree(List<NetworkConnection> edges, int numNodes) {
        // Sort edges by their cost (weight)
        List<NetworkConnection> sortedEdges = new ArrayList<>(edges);
        sortedEdges.sort(Comparator.comparingInt(NetworkConnection::getCost));

        // This will hold the edges of the MST
        List<NetworkConnection> mst = new ArrayList<>();

        // Create a UnionFind structure to keep track of connected components
        UnionFind uf = new UnionFind(numNodes);

        // Iterate over the sorted edges and add them to the MST if they don't form a cycle
        for (NetworkConnection edge : sortedEdges) {
            int node1 = edge.getNode1();
            int node2 = edge.getNode2();

            // If nodes are not in the same set, add the edge to the MST and union the nodes
            if (uf.find(node1) != uf.find(node2)) {
                uf.union(node1, node2);
                mst.add(edge);
            }
        }

        // Return the resulting Minimum Spanning Tree
        return mst;
    }

    /**
     * A Union-Find (also known as Disjoint Set) data structure to efficiently handle the merging
     * of disjoint sets and checking if two elements are in the same set.
     */
    private static class UnionFind {
        private int[] parent;
        private int[] rank;

        /**
         * Constructs a UnionFind structure with the specified number of nodes.
         * Initially, each node is its own parent, and all ranks are 0.
         *
         * @param size The number of nodes.
         */
        public UnionFind(int size) {
            parent = new int[size];
            rank = new int[size];
            for (int i = 0; i < size; i++) {
                parent[i] = i;  // Each node is its own parent
                rank[i] = 0;    // Initial rank is 0
            }
        }

        /**
         * Finds the root of the set that contains the given node using path compression.
         *
         * @param node The node whose set root we want to find.
         * @return The root of the set containing the node.
         */
        public int find(int node) {
            if (parent[node] != node) {
                parent[node] = find(parent[node]); // Path compression
            }
            return parent[node];
        }

        /**
         * Merges the sets containing two nodes. The node with the higher rank becomes the parent of the other.
         *
         * @param node1 One of the nodes to union.
         * @param node2 The other node to union.
         */
        public void union(int node1, int node2) {
            int root1 = find(node1);
            int root2 = find(node2);

            // If both nodes are already in the same set, do nothing
            if (root1 == root2) return;

            // Union by rank: attach the smaller tree under the larger tree
            if (rank[root1] > rank[root2]) {
                parent[root2] = root1;
            } else if (rank[root1] < rank[root2]) {
                parent[root1] = root2;
            } else {
                parent[root2] = root1;
                rank[root1]++; // Increment rank if trees have the same rank
            }
        }
    }
}






