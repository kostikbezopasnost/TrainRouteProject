package trainroute.servlet;

import trainroute.data.Station;
import trainroute.data.TrainRoute;
import trainroute.service.TrainRouteService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@WebServlet("/")
public class TrainRouteServlet extends HttpServlet {
    private TrainRouteService trainRouteService;

    @Override
    public void init() throws ServletException {
        trainRouteService = new TrainRouteService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<TrainRoute> routes = trainRouteService.getAllRoutes();
        request.setAttribute("routes", routes);

        String trainNumber = request.getParameter("trainNumber");
        if (trainNumber != null) {
            Optional<TrainRoute> route = trainRouteService.getRouteByNumber(trainNumber);
            route.ifPresent(value -> request.setAttribute("route", value));
        }

        request.getRequestDispatcher("/WEB-INF/trainroutes.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            String trainNumber = request.getParameter("trainNumber");
            String routeName = request.getParameter("routeName");

            TrainRoute newRoute = new TrainRoute(trainNumber, routeName);
            trainRouteService.addRoute(newRoute);
        } else if ("edit".equals(action)) {
            String trainNumber = request.getParameter("trainNumber");
            String routeName = request.getParameter("routeName");

            TrainRoute updatedRoute = new TrainRoute(trainNumber, routeName);
            trainRouteService.updateRoute(updatedRoute);
        } else if ("delete".equals(action)) {
            String trainNumber = request.getParameter("trainNumber");
            trainRouteService.deleteRoute(trainNumber);
        }

        response.sendRedirect(request.getContextPath() + "/");
    }
}
