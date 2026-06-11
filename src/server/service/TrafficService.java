package server.service;

import server.domain.model.RoutingGraph;

/**
 * Stub. The real traffic model (graph mutation + Observer) is implemented later.
 */
public class TrafficService {
    private final RoutingGraph graph;

    public TrafficService(RoutingGraph graph) {
        this.graph = graph;
    }

    public void reportTraffic(String from, String to, int addedMinutes) {
        // TODO: mutate the matching Road's travel time and notify observers
        System.out.println("[TRAFFIC] " + from + " -> " + to + " +" + addedMinutes + " min");
    }
}
