package server;

import server.domain.model.*;
import server.repository.CSVMapRepository;
import server.repository.MapRepository;
import server.service.DijkstraStrategy;
import server.service.RoutingService;

public class ServerApp {
    static void main(String[] args) {

        MapRepository repo = new CSVMapRepository("src/server/resources/valais.csv");
        RoutingGraph graph = repo.load();

        RoutingService service = new RoutingService(new DijkstraStrategy());
        RouteResult result = service.findRoute(graph, "Martigny", "Brig");

        System.out.println("Time: " + result.getTotalTimeMinutes() + " min");
        result.getPath().forEach(c -> System.out.print(c.getName() + " → "));
    }
}
