package ru.tsystems.sbb.services.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tsystems.sbb.model.dao.AdminDao;
import ru.tsystems.sbb.model.dao.RouteDao;
import ru.tsystems.sbb.model.entities.LineStation;
import ru.tsystems.sbb.model.entities.Station;

import java.util.List;

@Service
public class AdminDataServiceImpl implements AdminDataService {

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private RouteDao routeDao;

    @Override
    public void addNewStation(String stationName, int lineId, int zone, int order) {
        Station station = new Station();
        station.setName(stationName);
        station.setZone(zone);
        adminDao.add(station);
        LineStation lineStation = new LineStation();
        lineStation.setLine(routeDao.getLineById(lineId));
        lineStation.setStation(station);
        lineStation.setOrder(order);
        recalculateOrders(lineId, order);
        adminDao.add(lineStation);
    }

    private void recalculateOrders(int lineId, int firstOrder) {
        List<LineStation> lineStations = routeDao
                .getLineStations(routeDao.getLineById(lineId), firstOrder);
        for (LineStation ls : lineStations) {
            ls.setOrder(ls.getOrder() + 1);
            adminDao.update(ls);
        }
    }
}
