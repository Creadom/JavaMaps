package server.service;

import org.junit.jupiter.api.Test;
import server.domain.model.City;
import server.domain.model.RouteResult;
import server.domain.model.RoutingGraph;

import java.util.List;

// JUnit's assertion helpers: assertEquals, assertTrue, assertFalse...
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DijkstraStrategyTest {

    @Test
    void findsTheFasterDetourOverTheDirectRoad() {
        // --- Arrange: a tiny graph where the direct road is the slow one ---
        //   A --10--> B          (direct, slow)
        //   A --3--> C --3--> B   (detour, total 6, faster)
        City a = new City("A");
        City b = new City("B");
        City c = new City("C");

        RoutingGraph graph = new RoutingGraph(3);
        graph.addCity(0, a);
        graph.addCity(1, b);
        graph.addCity(2, c);
        graph.addRoad(a, b, 10);
        graph.addRoad(a, c, 3);
        graph.addRoad(c, b, 3);

        // --- Act: ask the strategy for the best route ---
        RouteResult result = new DijkstraStrategy().findPath(graph, "A", "B");

        // --- Assert: it must pick the detour, not the direct road ---
        assertTrue(result.isFound());
        assertEquals(6, result.totalTimeMinutes());
        assertEquals(List.of(a, c, b), result.path());
    }

    @Test
    void returnsNoRouteWhenDestinationIsUnreachable() {
        // --- Arrange: D is an island, connected to nothing ---
        City a = new City("A");
        City b = new City("B");
        City island = new City("Island");

        RoutingGraph graph = new RoutingGraph(3);
        graph.addCity(0, a);
        graph.addCity(1, b);
        graph.addCity(2, island);
        graph.addRoad(a, b, 5);
        // no road touches Island

        // --- Act ---
        RouteResult result = new DijkstraStrategy().findPath(graph, "A", "Island");

        // --- Assert: a clean "not found", never a crash or a bogus route ---
        assertFalse(result.isFound());
    }
}
