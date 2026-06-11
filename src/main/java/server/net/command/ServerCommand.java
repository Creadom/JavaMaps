package server.net.command;

import server.net.RequestContext;

/**
 * A single protocol request the server can execute.
 * execute() returns the reply line to send back to the client.
 */
public interface ServerCommand {

    String execute(RequestContext ctx);

    /** True when this command should close the client connection after replying. */
    default boolean terminatesSession() {
        return false;
    }
}
