package server.net.command;

import common.Protocol;
import server.net.RequestContext;

/**
 * Lists every city known to the map, so clients can offer a selection
 * instead of free-text input.
 */
public class CitiesCommand implements ServerCommand {

    @Override
    public String execute(RequestContext ctx) {
        StringBuilder reply = new StringBuilder();
        reply.append(Protocol.OK).append(Protocol.SEPARATOR).append(Protocol.CMD_CITIES);
        for (String name : ctx.graph().getCityNames()) {
            reply.append(Protocol.SEPARATOR).append(name);
        }
        return reply.toString();
    }
}
