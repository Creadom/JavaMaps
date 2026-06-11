package server.repository;

import server.domain.model.RoutingGraph;

public interface MapRepository {
    RoutingGraph load();
}