package server.net;

import server.net.command.CommandFactory;
import server.net.command.ServerCommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final RequestContext context;

    public ClientHandler(Socket socket, RequestContext context) {
        this.socket = socket;
        this.context = context;
    }

    @Override
    public void run() {
        System.out.println("Client connected: " + socket.getInetAddress());
        try (socket;
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)) {

            String line;
            while ((line = in.readLine()) != null) {
                ServerCommand command = CommandFactory.fromLine(line);
                out.println(command.execute(context));
                if (command.terminatesSession()) {
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }
        System.out.println("Client disconnected: " + socket.getInetAddress());
    }
}
