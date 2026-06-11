package server;

import server.domain.model.*;

public class ServerApp {
    static void main(String[] args) {
        System.out.println("Hello World!");

        City sion     = new City("Sion");
        City sierre   = new City("Sierre");
        City stLeo    = new City("St-Léonard");

        RoutingGraph graph = new RoutingGraph(3);
        graph.addCity(0, sion);
        graph.addCity(1, stLeo);
        graph.addCity(2, sierre);

        graph.addRoad(sion, stLeo, 8);
        graph.addRoad(stLeo, sierre, 7);
        graph.addRoad(sion, sierre, 20);

        for (Road r : graph.getRoadsFrom("Sion")) {
            System.out.println("→ " + r.getDestination().getName() + " " + r.getTravelTimeMinutes() + " min");
        }
    }
}
