package ru.tsystems.sbb.services.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.sbb.model.dao.AdminDao;
import ru.tsystems.sbb.model.dao.RouteDao;
import ru.tsystems.sbb.model.entities.LineStation;
import ru.tsystems.sbb.model.entities.Station;
import ru.tsystems.sbb.model.entities.Train;

import java.util.List;

@Service
@Transactional
public class AdminDataServiceImpl implements AdminDataService {

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private RouteDao routeDao;

    @Override
    public void addNewStation(final String stationName, final int lineId,
                              final int zone, final int order) {
        recalculateOrders(lineId, order);
        Station station = new Station();
        station.setName(stationName);
        station.setZone(zone);
        adminDao.add(station);
        LineStation lineStation = new LineStation();
        lineStation.setLine(routeDao.getLineById(lineId));
        lineStation.setStation(station);
        lineStation.setOrder(order);
        adminDao.add(lineStation);
    }

    @Override
    public void addNewTrainModel(String model, int seats) {
        Train train = new Train();
        train.setModel(model);
        train.setSeats(seats);
        adminDao.add(train);
    }

    private void recalculateOrders(final int lineId, final int firstOrder) {
        List<LineStation> lineStations = routeDao
                .getLineStations(routeDao.getLineById(lineId), firstOrder);
        for (LineStation ls : lineStations) {
            ls.setOrder(ls.getOrder() + 1);
            adminDao.update(ls);
        }
    }
}
