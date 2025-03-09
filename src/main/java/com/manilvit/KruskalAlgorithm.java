package com.manilvit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class KruskalAlgorithm {

    public static List<NetworkConnection> findMinimumSpanningTree(List<NetworkConnection> edges, int numNodes) {
        List<NetworkConnection> sortedEdges = new ArrayList<>(edges);
        Collections.sort(sortedEdges, Comparator.comparingInt(NetworkConnection::getCost));
        List<NetworkConnection> mst = new ArrayList<>();
        UnionFind uf = new UnionFind(numNodes);
        for (NetworkConnection edge : sortedEdges) {
            int node1 = edge.getNode1();
            int node2 = edge.getNode2();
            if (uf.find(node1) != uf.find(node2)) {
                uf.union(node1, node2);
                mst.add(edge);
            }
        }
        return mst;
    }

    private static class UnionFind {
        private int[] parent;
        private int[] rank;

        public UnionFind(int size) {
            parent = new int[size];
            rank = new int[size];
            for (int i = 0; i < size; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
        }

        public int find(int node) {
            if (parent[node] != node) {
                parent[node] = find(parent[node]);
            }
            return parent[node];
        }

        public void union(int node1, int node2) {
            int root1 = find(node1);
            int root2 = find(node2);
            if (root1 == root2) return;
            if (rank[root1] > rank[root2]) {
                parent[root2] = root1;
            } else if (rank[root1] < rank[root2]) {
                parent[root1] = root2;
            } else {
                parent[root2] = root1;
                rank[root1]++;
            }
        }
    }
}






