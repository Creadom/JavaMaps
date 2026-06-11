package server.service;

import server.domain.model.RouteResult;
import server.domain.model.RoutingGraph;

public class RoutingService {
    private final PathFindingStrategy strategy;

    public RoutingService(PathFindingStrategy strategy) {
        this.strategy = strategy;
    }

    public RouteResult findRoute(RoutingGraph graph, String from, String to) {
        // Same lock as TrafficService: Dijkstra writes shared arrays (lambda,
        // predecesseur) inside Graphe, so concurrent ROUTE requests would
        // corrupt each other even without any traffic update in flight.
        synchronized (graph) {
            return strategy.findPath(graph, from, to);
        }
    }
}