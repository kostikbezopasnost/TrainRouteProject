package trainroute.repository;

import trainroute.data.Station;
import trainroute.data.TrainRoute;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TrainRouteRepository {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "root";

    public TrainRouteRepository() {

    }

    public void addRoute(TrainRoute route) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String insertRouteSQL = "INSERT INTO TrainRoutes (train_number, route_name) VALUES (?, ?) RETURNING id";
            try (PreparedStatement routeStmt = connection.prepareStatement(insertRouteSQL)) {
                routeStmt.setString(1, route.getTrainNumber());
                routeStmt.setString(2, route.getRouteName());

                ResultSet rs = routeStmt.executeQuery();
                if (rs.next()) {
                    int routeId = rs.getInt(1);

                    String insertStationSQL = "INSERT INTO Stations (route_id, name, arrival_datetime, departure_datetime) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement stationStmt = connection.prepareStatement(insertStationSQL)) {
                        for (Station station : route.getIntermediateStations()) {
                            stationStmt.setInt(1, routeId);
                            stationStmt.setString(2, station.getName());
                            stationStmt.setTimestamp(3, Timestamp.valueOf(station.getArrivalTime()));
                            stationStmt.setTimestamp(4, Timestamp.valueOf(station.getDepartureTime()));
                            stationStmt.addBatch();
                        }
                        stationStmt.executeBatch();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<TrainRoute> getRouteByNumber(String trainNumber) {
        TrainRoute route = null;
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String queryRouteSQL = "SELECT id, route_name FROM TrainRoutes WHERE train_number = ?";
            try (PreparedStatement routeStmt = connection.prepareStatement(queryRouteSQL)) {
                routeStmt.setString(1, trainNumber);
                ResultSet routeRs = routeStmt.executeQuery();

                if (routeRs.next()) {
                    int routeId = routeRs.getInt("id");
                    String routeName = routeRs.getString("route_name");

                    route = new TrainRoute(trainNumber, routeName);

                    String queryStationsSQL = "SELECT name, arrival_datetime, departure_datetime FROM Stations WHERE route_id = ?";
                    try (PreparedStatement stationStmt = connection.prepareStatement(queryStationsSQL)) {
                        stationStmt.setInt(1, routeId);
                        ResultSet stationRs = stationStmt.executeQuery();

                        while (stationRs.next()) {
                            String stationName = stationRs.getString("name");
                            LocalDateTime arrivalTime = stationRs.getTimestamp("arrival_datetime").toLocalDateTime();
                            LocalDateTime departureTime = stationRs.getTimestamp("departure_datetime").toLocalDateTime();
                            route.addIntermediateStation(new Station(stationName, arrivalTime, departureTime));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(route);
    }

    public void updateRoute(TrainRoute updatedRoute) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String updateRouteSQL = "UPDATE TrainRoutes SET route_name = ? WHERE train_number = ?";
            try (PreparedStatement routeStmt = connection.prepareStatement(updateRouteSQL)) {
                routeStmt.setString(1, updatedRoute.getRouteName());
                routeStmt.setString(2, updatedRoute.getTrainNumber());
                routeStmt.executeUpdate();
            }

            String deleteStationsSQL = "DELETE FROM Stations WHERE route_id = (SELECT id FROM TrainRoutes WHERE train_number = ?)";
            try (PreparedStatement deleteStmt = connection.prepareStatement(deleteStationsSQL)) {
                deleteStmt.setString(1, updatedRoute.getTrainNumber());
                deleteStmt.executeUpdate();
            }

            String insertStationSQL = "INSERT INTO Stations (route_id, name, arrival_datetime, departure_datetime) VALUES ((SELECT id FROM TrainRoutes WHERE train_number = ?), ?, ?, ?)";
            try (PreparedStatement stationStmt = connection.prepareStatement(insertStationSQL)) {
                for (Station station : updatedRoute.getIntermediateStations()) {
                    stationStmt.setString(1, updatedRoute.getTrainNumber());
                    stationStmt.setString(2, station.getName());
                    stationStmt.setTimestamp(3, Timestamp.valueOf(station.getArrivalTime()));
                    stationStmt.setTimestamp(4, Timestamp.valueOf(station.getDepartureTime()));
                    stationStmt.addBatch();
                }
                stationStmt.executeBatch();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRoute(String trainNumber) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String deleteRouteSQL = "DELETE FROM TrainRoutes WHERE train_number = ?";
            try (PreparedStatement stmt = connection.prepareStatement(deleteRouteSQL)) {
                stmt.setString(1, trainNumber);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<TrainRoute> getAllRoutes() {
        List<TrainRoute> routes = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String queryRoutesSQL = "SELECT id, train_number, route_name FROM TrainRoutes";
            try (Statement routeStmt = connection.createStatement()) {
                ResultSet routeRs = routeStmt.executeQuery(queryRoutesSQL);

                while (routeRs.next()) {
                    int routeId = routeRs.getInt("id");
                    String trainNumber = routeRs.getString("train_number");
                    String routeName = routeRs.getString("route_name");

                    TrainRoute route = new TrainRoute(trainNumber, routeName);

                    String queryStationsSQL = "SELECT name, arrival_datetime, departure_datetime FROM Stations WHERE route_id = ?";
                    try (PreparedStatement stationStmt = connection.prepareStatement(queryStationsSQL)) {
                        stationStmt.setInt(1, routeId);
                        ResultSet stationRs = stationStmt.executeQuery();

                        while (stationRs.next()) {
                            String stationName = stationRs.getString("name");
                            LocalDateTime arrivalTime = stationRs.getTimestamp("arrival_datetime").toLocalDateTime();
                            LocalDateTime departureTime = stationRs.getTimestamp("departure_datetime").toLocalDateTime();
                            route.addIntermediateStation(new Station(stationName, arrivalTime, departureTime));
                        }
                    }

                    routes.add(route);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return routes;
    }
}
