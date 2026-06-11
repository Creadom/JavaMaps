package server;

import server.domain.model.RoutingGraph;
import server.net.ClientHandler;
import server.repository.CSVMapRepository;
import server.repository.MapRepository;
import server.service.DijkstraStrategy;
import server.service.RoutingService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerApp {
    private static final int PORT = 8888;

    public static void main(String[] args) {
        MapRepository repo = new CSVMapRepository("src/server/resources/valais.csv");
        RoutingGraph graph = repo.load();
        RoutingService service = new RoutingService(new DijkstraStrategy());

        System.out.println("Server started on port " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket client = serverSocket.accept();
                new Thread(new ClientHandler(client)).start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
}
