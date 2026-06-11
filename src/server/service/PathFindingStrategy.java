package server.service;

import server.domain.model.RouteResult;
import server.domain.model.RoutingGraph;

public interface PathFindingStrategy {
    RouteResult findPath(RoutingGraph graph, String from, String to);
}