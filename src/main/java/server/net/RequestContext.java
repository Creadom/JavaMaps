package server.net;

import server.domain.model.RoutingGraph;
import server.service.RoutingService;
import server.service.TrafficService;

/**
 * Carries the shared server-side services to every command's execute() call.
 * One instance is created in ServerApp and shared across all client threads.
 */
public class RequestContext {
    private final RoutingGraph graph;
    private final RoutingService routingService;
    private final TrafficService trafficService;

    public RequestContext(RoutingGraph graph, RoutingService routingService, TrafficService trafficService) {
        this.graph = graph;
        this.routingService = routingService;
        this.trafficService = trafficService;
    }

    public RoutingGraph getGraph() { return graph; }
    public RoutingService getRoutingService() { return routingService; }
    public TrafficService getTrafficService() { return trafficService; }
}
