package server.net.command;

import common.Protocol;

import java.util.regex.Pattern;

/**
 * Turns a raw protocol line into the matching ServerCommand.
 * Adding a new command means adding one case here plus its class —
 * ClientHandler never changes (Open/Closed principle).
 */
public class CommandFactory {

    private CommandFactory() {
    } // static utility, no instances

    public static ServerCommand fromLine(String line) {
        if (line == null || line.isBlank()) {
            return new UnknownCommand("Empty request");
        }

        String[] parts = line.split(Pattern.quote(Protocol.SEPARATOR));
        String keyword = parts[0].trim();

        switch (keyword) {
            case Protocol.CMD_ROUTE:
                if (parts.length < 3) {
                    return new UnknownCommand("ROUTE needs a source and destination");
                }
                return new RouteCommand(parts[1].trim(), parts[2].trim());

            case Protocol.CMD_TRAFFIC:
                if (parts.length < 4) {
                    return new UnknownCommand("TRAFFIC needs source, destination and minutes");
                }
                try {
                    int minutes = Integer.parseInt(parts[3].trim());
                    return new TrafficCommand(parts[1].trim(), parts[2].trim(), minutes);
                } catch (NumberFormatException e) {
                    return new UnknownCommand("TRAFFIC minutes must be a number");
                }

            case Protocol.CMD_BYE:
                return new ByeCommand();

            default:
                return new UnknownCommand("Unknown command '" + keyword + "'");
        }
    }
}
