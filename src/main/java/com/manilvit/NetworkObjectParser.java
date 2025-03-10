package com.manilvit;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class NetworkObjectParser {

    // Метод для парсинга файла
    public static List<NetworkConnection> parse(String fileContent) throws IOException {
        List<NetworkConnection> connections = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new StringReader(fileContent))) {
            // Read the number of nodes (first line)
            int numNodes = Integer.parseInt(br.readLine().trim());

            String line;
            int actualNodeCount = 0; // Counter for actual number of connections

            // Read the remaining lines with data
            while ((line = br.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");

                if (parts.length == 3) {
                    int node1 = Integer.parseInt(parts[0]);
                    int node2 = Integer.parseInt(parts[1]);
                    int cost = Integer.parseInt(parts[2]);

                    // Create a NetworkConnection object and add it to the list
                    connections.add(new NetworkConnection(node1, node2, cost));
                    actualNodeCount++;
                } else {
                    // If the line does not match the expected format
                    throw new IOException("Invalid line format: " + line);
                }
            }

            // Check if the number of connections matches the number of nodes
            if (actualNodeCount != numNodes) {
                throw new IOException("The number of connections does not match the number of nodes. Expected: " + numNodes + ", found: " + actualNodeCount);
            }
        }

        // Return the list of connections
        return connections;
    }
}