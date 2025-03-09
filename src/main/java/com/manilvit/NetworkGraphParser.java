package com.manilvit;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class NetworkGraphParser {

    // Метод для парсинга файла
    public static List<NetworkConnection> parse(String fileContent) throws IOException {
        List<NetworkConnection> connections = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new StringReader(fileContent))) {
            // Считываем количество узлов (первая строка)
            int numNodes = Integer.parseInt(br.readLine().trim());

            String line;
            // Считываем остальные строки с данными
            while ((line = br.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");

                if (parts.length == 3) {
                    int node1 = Integer.parseInt(parts[0]);
                    int node2 = Integer.parseInt(parts[1]);
                    int cost = Integer.parseInt(parts[2]);

                    // Создаем объект NetworkConnection и добавляем в список
                    connections.add(new NetworkConnection(node1, node2, cost));
                }
            }
        }

        // Возвращаем список соединений
        return connections;
    }
}