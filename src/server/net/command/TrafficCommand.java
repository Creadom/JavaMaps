package server.net.command;

import common.Protocol;
import server.net.RequestContext;

public class TrafficCommand implements ServerCommand {

    private final String from;
    private final String to;
    private final int addedMinutes;

    public TrafficCommand(String from, String to, int addedMinutes) {
        this.from = from;
        this.to = to;
        this.addedMinutes = addedMinutes;
    }

    @Override
    public String execute(RequestContext ctx) {
        ctx.getTrafficService().reportTraffic(from, to, addedMinutes);
        return Protocol.OK + Protocol.SEPARATOR + Protocol.CMD_TRAFFIC;
    }
}
