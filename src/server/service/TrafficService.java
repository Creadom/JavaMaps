package server.service;

import server.domain.model.RoutingGraph;

/**
 * Applies driver traffic observations to the shared graph.
 * Reports are absolute ("this segment currently takes N minutes"), so
 * repeated reports of the same jam are idempotent instead of compounding.
 */
public class TrafficService {
    private final RoutingGraph graph;

    public TrafficService(RoutingGraph graph) {
        this.graph = graph;
    }

    /**
     * Returns false when the two cities share no direct road.
     * Synchronized on the graph: ROUTE computations hold the same lock,
     * so a route is never calculated against a half-applied update.
     */
    public boolean reportTraffic(String from, String to, int observedMinutes) {
        synchronized (graph) {
            boolean applied = graph.setTravelTime(from, to, observedMinutes);
            if (applied) {
                System.out.println("[TRAFFIC] " + from + " <-> " + to + " now " + observedMinutes + " min");
            }
            return applied;
        }
    }
}
