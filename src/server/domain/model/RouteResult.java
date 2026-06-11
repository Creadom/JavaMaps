package server.domain.model;

import java.util.List;

public class RouteResult {
    private final List<City> path;       // ordered, from source to destination
    private final int totalTimeMinutes;

    public RouteResult(List<City> path, int totalTimeMinutes) {
        this.path = path;
        this.totalTimeMinutes = totalTimeMinutes;
    }
    public List<City> getPath()        { return path; }
    public int getTotalTimeMinutes()   { return totalTimeMinutes; }
}