package trainroute.data;

import java.time.LocalDateTime;

public class Station {
    private String name;
    private LocalDateTime arrivalTime;
    private LocalDateTime departureTime;

    public Station(String name, LocalDateTime arrivalTime, LocalDateTime departureTime) {
        if (!departureTime.isAfter(arrivalTime)) {
            throw new IllegalArgumentException("Время отправления должно быть позже времени прибытия.");
        }
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    @Override
    public String toString() {
        return "Станция: " + name + ", Прибытие: " + arrivalTime + ", Отправление: " + departureTime;
    }
}
