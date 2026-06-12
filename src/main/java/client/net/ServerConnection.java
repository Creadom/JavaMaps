package client.net;

import common.Protocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Owns the socket to the server. The rest of the client talks in Strings
 * and never sees networking details (Single Responsibility).
 */
public class ServerConnection {
    private final String host;
    private final int port;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ServerConnection(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() throws IOException {
        socket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
    }

    /**
     * Sends one request line and blocks until the server's one-line reply arrives.
     */
    public String send(String request) throws IOException {
        out.println(request);
        String reply = in.readLine();
        if (reply == null) {
            throw new IOException("Connection closed by server");
        }
        return reply;
    }

    /**
     * Says BYE to the server, then closes the socket (and with it both streams).
     */
    public void close() {
        try {
            if (out != null) {
                out.println(Protocol.CMD_BYE);
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error while closing: " + e.getMessage());
        }
    }
}
