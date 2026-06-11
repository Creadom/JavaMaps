package server.service;

import server.domain.model.City;
import server.domain.model.Graphe;
import server.domain.model.RouteResult;
import server.domain.model.RoutingGraph;

import java.util.ArrayList;
import java.util.List;

public class DijkstraStrategy implements PathFindingStrategy{

    @Override
    public RouteResult findPath(RoutingGraph graph, String from, String to) {
        Graphe g = graph.getGraphe();
        int source = graph.getIndex(from);
        int dest   = graph.getIndex(to);

        g.dijkstra(source); // sets g.lambda and g.predecesseur

        // reconstruct path by walking predecesseur[] backwards
        List<City> path = new ArrayList<>();
        int current = dest;
        while (current != -1) {
            path.add(0, graph.getCityByIndex(current)); // prepend
            current = g.predecesseur[current];
        }

        return new RouteResult(path, g.lambda[dest]);
    }
}
