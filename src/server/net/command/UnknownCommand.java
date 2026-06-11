package server.net.command;

import common.Protocol;
import server.net.RequestContext;

/**
 * Returned by the factory when a request can't be parsed.
 * Keeps ClientHandler free of any error-handling branches.
 */
public class UnknownCommand implements ServerCommand {

    private final String reason;

    public UnknownCommand(String reason) {
        this.reason = reason;
    }

    @Override
    public String execute(RequestContext ctx) {
        return Protocol.ERR + Protocol.SEPARATOR + reason;
    }
}
