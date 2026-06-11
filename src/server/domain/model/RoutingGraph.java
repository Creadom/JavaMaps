package server.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class RoutingGraph {
    private final Graphe graphe;
    private final City[] cities;               // index → City
    private final Map<String, Integer> indexes; // city name → index

    public RoutingGraph(int numberOfCities) {
        this.graphe = new Graphe(numberOfCities);
        this.cities = new City[numberOfCities];
        this.indexes = new HashMap<>();
    }

    public void addCity(int index, City city) {
        cities[index] = city;
        indexes.put(city.getName(), index);
    }

    public void addRoad(City from, City to, int travelTimeMinutes) {
        int fromIdx = indexes.get(from.getName());
        int toIdx   = indexes.get(to.getName());
        // Info(valeur=destinationIndex, dist=travelTime)
        graphe.liste[fromIdx].enfile(new Noeud(new Info(toIdx, travelTimeMinutes)));
        graphe.liste[toIdx].enfile(new Noeud(new Info(fromIdx, travelTimeMinutes))); // bidirectional
    }

    public List<Road> getRoadsFrom(String cityName) {
        int idx = indexes.get(cityName);
        List<Road> roads = new ArrayList<>();
        Noeud current = graphe.liste[idx].getPremier();
        while (current != null) {
            City dest = cities[current.getInfo().getValeur()];
            int time  = current.getInfo().getDist();
            roads.add(new Road(dest, time));
            current = current.getSuivant();
        }
        return roads;
    }

    public Graphe getGraphe()
    {
        return graphe;
    }

    public int getIndex(String cityName) {
        return indexes.get(cityName);
    }

    public City getCityByIndex(int index) {
        return cities[index];
    }

    public int size() {
        return cities.length;
    }

    public boolean hasCity(String name) {
        return indexes.containsKey(name);
    }
}
