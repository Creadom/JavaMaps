package client.net;

import common.Protocol;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class RouteResponse {

    private final boolean success;
    private final String errorMessage;
    private final int etaMinutes;
    private final List<String> cities;

    private RouteResponse(boolean success, String errorMessage, int etaMinutes, List<String> cities) {
        this.success = success;
        this.errorMessage = errorMessage;
        this.etaMinutes = etaMinutes;
        this.cities = cities;
    }

    public static RouteResponse parse(String rawReply) {
        String[] parts = rawReply.split(Pattern.quote(Protocol.SEPARATOR));

        if (parts[0].equals(Protocol.ERR)) {
            return new RouteResponse(false, parts.length > 1 ? parts[1] : "Unknown error", 0, List.of());
        }

        // expected: OK|ROUTE|<eta>|<city1>|...|<cityN>
        int eta = Integer.parseInt(parts[2]);
        List<String> cities = new ArrayList<>();
        for (int i = 3; i < parts.length; i++) {
            cities.add(parts[i]);
        }
        return new RouteResponse(true, null, eta, cities);
    }

    public boolean isSuccess()        { return success; }
    public String getErrorMessage()   { return errorMessage; }
    public int getEtaMinutes()        { return etaMinutes; }
    public List<String> getCities()   { return cities; }
}
