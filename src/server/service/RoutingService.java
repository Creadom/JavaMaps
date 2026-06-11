package server.service;

import server.domain.model.RouteResult;
import server.domain.model.RoutingGraph;

public class RoutingService {
    private final PathFindingStrategy strategy;

    public RoutingService(PathFindingStrategy strategy) {
        this.strategy = strategy;
    }

    public RouteResult findRoute(RoutingGraph graph, String from, String to) {
        return strategy.findPath(graph, from, to);
    }
}