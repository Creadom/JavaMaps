package server;

import server.domain.model.RoutingGraph;
import server.net.ClientHandler;
import server.net.RequestContext;
import server.repository.CSVMapRepository;
import server.repository.MapRepository;
import server.service.DijkstraStrategy;
import server.service.RoutingService;
import server.service.TrafficService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerApp {
    private static final int PORT = 8888;

    public static void main(String[] args) {
        MapRepository repo = new CSVMapRepository("valais.csv");
        RoutingGraph graph = repo.load();

        RoutingService routingService = new RoutingService(new DijkstraStrategy());
        TrafficService trafficService = new TrafficService(graph);
        // Shared across all client threads (thread-safety addressed later)
        RequestContext context = new RequestContext(graph, routingService, trafficService);

        System.out.println("Server started on port " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket client = serverSocket.accept();
                new Thread(new ClientHandler(client, context)).start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
}
