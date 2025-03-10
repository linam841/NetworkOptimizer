package com.manilvit;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for parsing a file content that contains network connections.
 * It reads the number of nodes and the subsequent network connections with their costs.
 */
public class NetworkObjectParser {

    /**
     * Parses the provided file content to extract network connections.
     *
     * @param fileContent The content of the file as a string.
     * @return A list of {@link NetworkConnection} objects representing the connections.
     * @throws IOException If there is an error in reading the file content or invalid format.
     */
    public static List<NetworkConnection> parse(String fileContent) throws IOException {
        // List to store the network connections
        List<NetworkConnection> connections = new ArrayList<>();

        // Using BufferedReader to read the file content from a StringReader
        try (BufferedReader br = new BufferedReader(new StringReader(fileContent))) {
            // Read the number of nodes from the first line
            int numNodes = Integer.parseInt(br.readLine().trim());

            String line;
            int actualNodeCount = 0; // Counter to keep track of the actual number of connections

            // Read the remaining lines which contain the network connection data
            while ((line = br.readLine()) != null) {
                // Split the line into parts by whitespace
                String[] parts = line.trim().split("\\s+");

                // Check if the line contains exactly three parts: node1, node2, and cost
                if (parts.length == 3) {
                    int node1 = Integer.parseInt(parts[0]); // The first node
                    int node2 = Integer.parseInt(parts[1]); // The second node
                    int cost = Integer.parseInt(parts[2]); // The cost of the connection

                    // Create a NetworkConnection object and add it to the list
                    connections.add(new NetworkConnection(node1, node2, cost));
                    actualNodeCount++; // Increment the connection count
                } else {
                    // If the line format is invalid, throw an IOException
                    throw new IOException("Invalid line format: " + line);
                }
            }

            // Check if the actual number of connections matches the expected number of nodes
            if (actualNodeCount != numNodes) {
                throw new IOException("The number of connections does not match the number of nodes. Expected: " + numNodes + ", found: " + actualNodeCount);
            }
        }

        // Return the list of parsed connections
        return connections;
    }
}