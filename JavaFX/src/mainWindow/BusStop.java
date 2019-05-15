package mainWindow;

public class BusStop {

    private String stopName;
    private String stopId;
    private String direction;

    public BusStop(String stopName, String stopId, String direction) {
        this.stopName = stopName;
        this.stopId = stopId;
        this.direction = direction;
    }

    public String getStopName() {
        return stopName;
    }

    public String getStopId() {
        return stopId;
    }

    public String getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return "Peatuse nimi on " + stopName + " ja suund on: " + direction;
    }
}
