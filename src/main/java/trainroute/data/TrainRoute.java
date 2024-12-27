package trainroute.data;

import java.util.ArrayList;
import java.util.List;

public class TrainRoute {
    private String trainNumber;
    private String routeName;
    private List<Station> intermediateStations;

    public TrainRoute(String trainNumber, String routeName) {
        this.trainNumber = trainNumber;
        this.routeName = routeName;
        this.intermediateStations = new ArrayList<>();
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public List<Station> getIntermediateStations() {
        return intermediateStations;
    }

    public void addIntermediateStation(Station station) {
        intermediateStations.add(station);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Поезд: ").append(trainNumber).append(", Маршрут: ").append(routeName).append("\n");
        for (Station station : intermediateStations) {
            sb.append("    ").append(station).append("\n");
        }
        return sb.toString();
    }
}
