package server.repository;

import server.domain.model.City;
import server.domain.model.RoutingGraph;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CSVMapRepository implements server.repository.MapRepository {

    private final String filePath;

    public CSVMapRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public RoutingGraph load() {
        // First pass: collect all unique city names
        List<String[]> rows = readRows();
        Set<String> names = new LinkedHashSet<>();
        for (String[] row : rows) {
            names.add(row[0]);
            names.add(row[1]);
        }

        // Assign an index to each city and build the graph
        RoutingGraph graph = new RoutingGraph(names.size());
        Map<String, City> cityMap = new HashMap<>();
        int index = 0;
        for (String name : names) {
            City city = new City(name);
            cityMap.put(name, city);
            graph.addCity(index++, city);
        }

        // Second pass: add roads
        for (String[] row : rows) {
            City from = cityMap.get(row[0]);
            City to   = cityMap.get(row[1]);
            int time  = Integer.parseInt(row[2].trim());
            graph.addRoad(from, to, time);
        }

        return graph;
    }

    private List<String[]> readRows() {
        List<String[]> rows = new ArrayList<>();
        InputStream stream = getClass().getResourceAsStream("/" + filePath);
        if (stream == null) {
            throw new RuntimeException("Map resource not found on classpath: " + filePath);
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                rows.add(line.split(";"));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load map from: " + filePath, e);
        }
        return rows;
    }
}