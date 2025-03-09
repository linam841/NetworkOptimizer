package com.manilvit;

public class NetworkConnection {
    private int node1;
    private int node2;
    private int cost;

    // Конструктор
    public NetworkConnection(int node1, int node2, int cost) {
        this.node1 = node1;
        this.node2 = node2;
        this.cost = cost;
    }

    // Геттеры
    public int getNode1() {
        return node1;
    }

    public int getNode2() {
        return node2;
    }

    public int getCost() {
        return cost;
    }

    // Переопределим toString для удобства вывода
    @Override
    public String toString() {
        return "Node1: " + node1 + ", Node2: " + node2 + ", Cost: " + cost;
    }
}
