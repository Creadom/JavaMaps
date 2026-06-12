package client;

import client.net.RouteResponse;
import client.net.ServerConnection;
import client.ui.CityPicker;
import common.Protocol;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

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

        List<String> cities;
        try {
            cities = fetchCities(connection);
        } catch (IOException e) {
            System.out.println("Could not load the city list: " + e.getMessage());
            connection.close();
            return;
        }

        CityPicker picker = new CityPicker(buildTerminal(), keyboard);

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
                case "1" -> findRoute(connection, picker, cities);
                case "2" -> reportTraffic(connection, picker, cities, keyboard);
                case "3" -> running = false;
                default -> System.out.println("Please pick 1, 2 or 3.");
            }
        }

        connection.close();   // sends BYE, closes the socket
        System.out.println("Goodbye!");
    }

    /** Raw-mode terminal for arrow-key selection; null means the picker falls back to numbers. */
    private static Terminal buildTerminal() {
        try {
            return TerminalBuilder.builder().system(true).dumb(true).build();
        } catch (IOException e) {
            return null;
        }
    }

    /** Asks the server for all known city names (OK|CITIES|name|name|...). */
    private static List<String> fetchCities(ServerConnection connection) throws IOException {
        String reply = connection.send(Protocol.CMD_CITIES);
        String[] parts = reply.split(Pattern.quote(Protocol.SEPARATOR));
        if (!parts[0].equals(Protocol.OK) || parts.length < 3) {
            throw new IOException("Unexpected reply to CITIES: " + reply);
        }
        return Arrays.asList(parts).subList(2, parts.length);
    }

    private static void findRoute(ServerConnection connection, CityPicker picker, List<String> cities) {
        String from = picker.pick("Departure city:", cities);
        String to = picker.pick("Destination city:", cities);

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

    private static void reportTraffic(ServerConnection connection, CityPicker picker,
                                      List<String> cities, Scanner keyboard) {
        String from = picker.pick("Segment start:", cities);
        String to = picker.pick("Segment end:", cities);

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
