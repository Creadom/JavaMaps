package server.net.command;

import common.Protocol;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommandFactoryTest {

    @Test
    void parseValidRouteCommand() {
        ServerCommand cmd = CommandFactory.fromLine(Protocol.CMD_ROUTE + Protocol.SEPARATOR + "Paris" + Protocol.SEPARATOR + "Lyon");
        assertTrue(cmd instanceof RouteCommand);
    }

    @Test
    void parseValidTrafficCommand() {
        ServerCommand cmd = CommandFactory.fromLine(Protocol.CMD_TRAFFIC + Protocol.SEPARATOR + "Paris" + Protocol.SEPARATOR + "Lyon" + Protocol.SEPARATOR + "42");
        assertTrue(cmd instanceof TrafficCommand);
    }

    @Test
    void parseValidByeCommand() {
        ServerCommand cmd = CommandFactory.fromLine(Protocol.CMD_BYE);
        assertTrue(cmd instanceof ByeCommand);
    }

    @Test
    void parseMalformedRouteCommandReturnsUnknown() {
        ServerCommand cmd = CommandFactory.fromLine(Protocol.CMD_ROUTE + Protocol.SEPARATOR + "Paris");
        assertTrue(cmd instanceof UnknownCommand);
    }

    @Test
    void parseMalformedTrafficCommandMissingArgsReturnsUnknown() {
        ServerCommand cmd = CommandFactory.fromLine(Protocol.CMD_TRAFFIC + Protocol.SEPARATOR + "Paris" + Protocol.SEPARATOR + "Lyon");
        assertTrue(cmd instanceof UnknownCommand);
    }

    @Test
    void parseMalformedTrafficCommandInvalidNumberReturnsUnknown() {
        ServerCommand cmd = CommandFactory.fromLine(Protocol.CMD_TRAFFIC + Protocol.SEPARATOR + "Paris" + Protocol.SEPARATOR + "Lyon" + Protocol.SEPARATOR + "fast");
        assertTrue(cmd instanceof UnknownCommand);
    }

    @Test
    void parseUnknownCommand() {
        ServerCommand cmd = CommandFactory.fromLine("JUMP" + Protocol.SEPARATOR + "High");
        assertTrue(cmd instanceof UnknownCommand);
    }

    @Test
    void parseEmptyLine() {
        ServerCommand cmd1 = CommandFactory.fromLine("");
        assertTrue(cmd1 instanceof UnknownCommand);

        ServerCommand cmd2 = CommandFactory.fromLine("   ");
        assertTrue(cmd2 instanceof UnknownCommand);

        ServerCommand cmd3 = CommandFactory.fromLine(null);
        assertTrue(cmd3 instanceof UnknownCommand);
    }
}
