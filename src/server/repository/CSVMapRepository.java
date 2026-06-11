package server.repository;
import server.domain.model.City;
import server.domain.model.RoutingGraph;

import java.io.*;
import java.util.*;

public class CSVMapRepository implements MapRepository {

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
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
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