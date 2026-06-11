package server.domain.model;

public class Road {
    private final City destination;
    private int travelTimeMinutes; //time in minutes

    public Road(City destination, int travelTimeMinutes)
    {
        this.destination = destination;
        this.travelTimeMinutes = travelTimeMinutes;
    }

    public int getTravelTimeMinutes() {
        return travelTimeMinutes;
    }

    public void setTravelTimeMinutes(int travelTimeMinutes) {
        this.travelTimeMinutes = travelTimeMinutes;
    }

    public City getDestination() {
        return destination;
    }
}
