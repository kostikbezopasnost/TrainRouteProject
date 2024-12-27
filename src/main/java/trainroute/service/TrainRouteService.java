package trainroute.service;

import trainroute.data.TrainRoute;
import trainroute.repository.TrainRouteRepository;

import java.util.List;
import java.util.Optional;

public class TrainRouteService {
    private TrainRouteRepository trainRouteRepository;

    public TrainRouteService() {
        trainRouteRepository = new TrainRouteRepository();
    }

    public void addRoute(TrainRoute route) {
        trainRouteRepository.addRoute(route);
    }

    public Optional<TrainRoute> getRouteByNumber(String trainNumber) {
        return trainRouteRepository.getRouteByNumber(trainNumber);
    }

    public void updateRoute(TrainRoute updatedRoute) {
        trainRouteRepository.updateRoute(updatedRoute);
    }

    public void deleteRoute(String trainNumber) {
        trainRouteRepository.deleteRoute(trainNumber);
    }

    public List<TrainRoute> getAllRoutes() {
        return trainRouteRepository.getAllRoutes();
    }
}