package server.service;

import server.domain.model.RouteResult;
import server.domain.model.RoutingGraph;

public interface PathFindingStrategy {
    /**
     * Never returns null. When no path exists, returns a RouteResult
     * with isFound() == false.
     */
    RouteResult findPath(RoutingGraph graph, String from, String to);
}