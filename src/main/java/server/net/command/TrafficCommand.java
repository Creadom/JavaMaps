package server.net.command;

import common.Protocol;
import server.net.RequestContext;

public class TrafficCommand implements ServerCommand {

    private final String from;
    private final String to;
    private final int observedMinutes;

    public TrafficCommand(String from, String to, int observedMinutes) {
        this.from = from;
        this.to = to;
        this.observedMinutes = observedMinutes;
    }

    @Override
    public String execute(RequestContext ctx) {
        if (observedMinutes < 1 || observedMinutes > 600) {
            return Protocol.ERR + Protocol.SEPARATOR
                    + "Observed time must be between 1 and 600 minutes";
        }
        boolean applied = ctx.trafficService().reportTraffic(from, to, observedMinutes);
        if (!applied) {
            return Protocol.ERR + Protocol.SEPARATOR
                    + "No direct road between " + from + " and " + to;
        }

        return Protocol.OK + Protocol.SEPARATOR + Protocol.CMD_TRAFFIC;
    }
}
