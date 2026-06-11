package server.net.command;

import common.Protocol;
import server.net.RequestContext;

public class ByeCommand implements ServerCommand {

    @Override
    public String execute(RequestContext ctx) {
        return Protocol.OK + Protocol.SEPARATOR + Protocol.CMD_BYE;
    }

    @Override
    public boolean terminatesSession() {
        return true;
    }
}
