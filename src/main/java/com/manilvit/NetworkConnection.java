package com.manilvit;

/**
 * Represents a network connection between two nodes with an associated cost.
 * This class encapsulates the details of a connection between two nodes, including the nodes' identifiers and the cost associated with the connection.
 * It is used as part of a network optimization algorithm to process connections and find the minimum spanning tree.
 */
public class NetworkConnection {

    private int node1;
    private int node2;
    private int cost;

    /**
     * Constructs a new NetworkConnection instance.
     *
     * @param node1 The identifier of the first node.
     * @param node2 The identifier of the second node.
     * @param cost The cost associated with the connection between the two nodes.
     */
    public NetworkConnection(int node1, int node2, int cost) {
        this.node1 = node1;
        this.node2 = node2;
        this.cost = cost;
    }

    /**
     * Gets the identifier of the first node in the connection.
     *
     * @return The identifier of the first node.
     */
    public int getNode1() {
        return node1;
    }

    /**
     * Gets the identifier of the second node in the connection.
     *
     * @return The identifier of the second node.
     */
    public int getNode2() {
        return node2;
    }

    /**
     * Gets the cost associated with the connection between the two nodes.
     *
     * @return The cost of the connection.
     */
    public int getCost() {
        return cost;
    }

    /**
     * Returns a string representation of this network connection.
     * The format includes the identifiers of the two nodes and the cost of the connection.
     *
     * @return A string representing the network connection.
     */
    @Override
    public String toString() {
        return "Node1: " + node1 + ", Node2: " + node2 + ", Cost: " + cost;
    }
}

