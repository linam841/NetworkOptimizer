package com.manilvit;

import java.util.Objects;

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


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof NetworkConnection)) return false;
        NetworkConnection other = (NetworkConnection) obj;
        // An edge is considered equal if the set of nodes matches and the cost is the same.
        return this.cost == other.cost &&
                ((this.node1 == other.node1 && this.node2 == other.node2) ||
                        (this.node1 == other.node2 && this.node2 == other.node1));
    }

    @Override
    public int hashCode() {
        // To ensure (1,2) and (2,1) produce the same hashCode, use the sum and product of the nodes
        int sum = node1 + node2;
        int prod = node1 * node2;
        return Objects.hash(sum, prod, cost);
    }
}

