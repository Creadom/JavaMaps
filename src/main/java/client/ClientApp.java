package client;

import client.net.RouteResponse;
import client.net.ServerConnection;
import common.Protocol;

import java.io.IOException;
import java.util.Scanner;

public class ClientApp {

    public static void main(String[] args) {
        ServerConnection connection = new ServerConnection("localhost", 8888);
        Scanner keyboard = new Scanner(System.in);

        try {
            connection.connect();
            System.out.println("Connected to JavaMaps server.");
        } catch (Exception e) {
            System.out.println("Could not reach the server: " + e.getMessage());
            return;   // no server, no point showing a menu
        }

        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("=== JavaMaps ===");
            System.out.println("1) Find a route");
            System.out.println("2) Report traffic");
            System.out.println("3) Quit");
            System.out.print("> ");

            String choice = keyboard.nextLine().trim();
            switch (choice) {
                case "1" -> findRoute(connection, keyboard);
                case "2" -> reportTraffic(connection, keyboard);
                case "3" -> running = false;
                default  -> System.out.println("Please pick 1, 2 or 3.");
            }
        }

        connection.close();   // sends BYE, closes the socket
        System.out.println("Goodbye!");
    }

    private static void findRoute(ServerConnection connection, Scanner keyboard) {
        System.out.print("From: ");
        String from = keyboard.nextLine().trim();
        System.out.print("To: ");
        String to = keyboard.nextLine().trim();

        try {
            String reply = connection.send(
                    Protocol.CMD_ROUTE + Protocol.SEPARATOR + from + Protocol.SEPARATOR + to);

            RouteResponse response = RouteResponse.parse(reply);
            if (!response.isSuccess()) {
                System.out.println("Sorry: " + response.getErrorMessage());
                return;
            }

            System.out.println(from + " -> " + to + " : " + response.getEtaMinutes() + " min");
            System.out.println("Via: " + String.join(", ", response.getCities()));
        } catch (IOException e) {
            System.out.println("Connection problem: " + e.getMessage());
        }
    }

    private static void reportTraffic(ServerConnection connection, Scanner keyboard) {
        System.out.print("From: ");
        String from = keyboard.nextLine().trim();
        System.out.print("To: ");
        String to = keyboard.nextLine().trim();

        System.out.print("How many minutes did this segment take you? ");
        int minutes;
        try {
            minutes = Integer.parseInt(keyboard.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("That's not a number — report cancelled.");
            return;
        }

        try {
            String reply = connection.send(
                    Protocol.CMD_TRAFFIC + Protocol.SEPARATOR + from
                            + Protocol.SEPARATOR + to
                            + Protocol.SEPARATOR + minutes);

            if (reply.startsWith(Protocol.OK)) {
                System.out.println("Traffic report sent, thank you for your help!");
            } else {
                System.out.println("Sorry, the server refused the report: " + reply);
            }
        } catch (IOException e) {
            System.out.println("Connection problem: " + e.getMessage());
        }
    }
}