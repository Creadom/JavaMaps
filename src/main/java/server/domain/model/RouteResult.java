package server.domain.model;

import java.util.List;

/**
 * @param path ordered, from source to destination
 */
public record RouteResult(List<City> path, int totalTimeMinutes) {

    public static RouteResult noRoute() {
        return new RouteResult(List.of(), -1);
    }

    public boolean isFound() {
        return !path.isEmpty();
    }
}