package server.net.command;

import common.Protocol;
import server.domain.model.City;
import server.domain.model.RouteResult;
import server.net.RequestContext;

public class RouteCommand implements ServerCommand {

    private final String from;
    private final String to;

    public RouteCommand(String from, String to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public String execute(RequestContext ctx) {
        if (!ctx.getGraph().hasCity(from)) {
            return Protocol.ERR + Protocol.SEPARATOR + "Unknown city '" + from + "'";
        }
        if (!ctx.getGraph().hasCity(to)) {
            return Protocol.ERR + Protocol.SEPARATOR + "Unknown city '" + to + "'";
        }

        RouteResult result = ctx.getRoutingService().findRoute(ctx.getGraph(), from, to);

        if (!result.isFound()) {
            return Protocol.ERR + Protocol.SEPARATOR + "No route between " + from + " and " + to;
        }

        StringBuilder reply = new StringBuilder();
        reply.append(Protocol.OK).append(Protocol.SEPARATOR).append(Protocol.CMD_ROUTE).append(Protocol.SEPARATOR).append(result.getTotalTimeMinutes());
        for (City city : result.getPath()) {
            reply.append(Protocol.SEPARATOR).append(city.getName());
        }
        return reply.toString();
    }
}
