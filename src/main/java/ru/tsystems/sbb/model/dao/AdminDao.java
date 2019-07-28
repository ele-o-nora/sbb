package ru.tsystems.sbb.model.dao;

import ru.tsystems.sbb.model.entities.LineStation;
import ru.tsystems.sbb.model.entities.Station;

public interface AdminDao {
    void add(Station station);
    void add(LineStation lineStation);
    void update(LineStation lineStation);
}
