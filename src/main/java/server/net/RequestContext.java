package server.net;

import server.domain.model.RoutingGraph;
import server.service.RoutingService;
import server.service.TrafficService;

/**
 * Carries the shared server-side services to every command's execute() call.
 * One instance is created in ServerApp and shared across all client threads.
 */
public record RequestContext(RoutingGraph graph, RoutingService routingService, TrafficService trafficService) {
}
