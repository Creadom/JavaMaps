package server.net;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("Client connected: " + socket.getInetAddress());
        try (socket;
             BufferedReader in  = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
             PrintWriter    out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true)) {

            String line;
            while ((line = in.readLine()) != null) {
                if (line.equals(common.Protocol.CMD_BYE)) {
                    out.println("OK|BYE");
                    break;
                }
                out.println(line); // echo for now
            }

        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }
        System.out.println("Client disconnected: " + socket.getInetAddress());
    }
}
