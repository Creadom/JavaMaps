package server.service;

import org.junit.jupiter.api.Test;
import server.domain.model.City;
import server.domain.model.RouteResult;
import server.domain.model.RoutingGraph;

import java.util.List;

// JUnit's assertion helpers: assertEquals, assertTrue, assertFalse...
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    void pathReconstructionOrder() {
        City a = new City("A");
        City b = new City("B");
        City c = new City("C");
        City d = new City("D");

        RoutingGraph graph = new RoutingGraph(4);
        graph.addCity(0, a);
        graph.addCity(1, b);
        graph.addCity(2, c);
        graph.addCity(3, d);

        graph.addRoad(a, b, 2);
        graph.addRoad(b, c, 2);
        graph.addRoad(c, d, 2);
        graph.addRoad(a, c, 5); // A->C is 5, but A->B->C is 4, so should prefer A->B->C->D

        RouteResult result = new DijkstraStrategy().findPath(graph, "A", "D");

        assertTrue(result.isFound());
        assertEquals(6, result.totalTimeMinutes());
        assertEquals(List.of(a, b, c, d), result.path());
    }

    @Test
    void unknownCityThrowsNullPointerException() {
        RoutingGraph graph = new RoutingGraph(1);
        graph.addCity(0, new City("A"));

        // Since we are not changing src files, DijkstraStrategy throws NullPointerException when unboxing null Integer
        assertThrows(NullPointerException.class, () -> {
            new DijkstraStrategy().findPath(graph, "A", "Unknown");
        });
    }

    @Test
    void trafficFactorChangesChosenPath() {
        City a = new City("A");
        City b = new City("B");
        City c = new City("C");

        RoutingGraph graph = new RoutingGraph(3);
        graph.addCity(0, a);
        graph.addCity(1, b);
        graph.addCity(2, c);

        // direct A->B is 5
        // A->C->B is 10
        graph.addRoad(a, b, 5);
        graph.addRoad(a, c, 4);
        graph.addRoad(c, b, 6);

        RouteResult result1 = new DijkstraStrategy().findPath(graph, "A", "B");
        assertEquals(5, result1.totalTimeMinutes());
        assertEquals(List.of(a, b), result1.path());

        // Now traffic on A->B makes it 15
        graph.setTravelTime("A", "B", 15);

        RouteResult result2 = new DijkstraStrategy().findPath(graph, "A", "B");
        assertEquals(10, result2.totalTimeMinutes());
        assertEquals(List.of(a, c, b), result2.path());
    }

    @Test
    void etaArithmeticCheck() {
        City a = new City("A");
        City b = new City("B");
        City c = new City("C");
        City d = new City("D");

        RoutingGraph graph = new RoutingGraph(4);
        graph.addCity(0, a);
        graph.addCity(1, b);
        graph.addCity(2, c);
        graph.addCity(3, d);

        graph.addRoad(a, b, 12);
        graph.addRoad(b, c, 18);
        graph.addRoad(c, d, 5);

        RouteResult result = new DijkstraStrategy().findPath(graph, "A", "D");
        assertTrue(result.isFound());
        assertEquals(12 + 18 + 5, result.totalTimeMinutes());
    }
}
