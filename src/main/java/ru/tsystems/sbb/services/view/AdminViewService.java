package ru.tsystems.sbb.services.view;

import java.util.Map;

public interface AdminViewService {
    Map<String, Object> getLinesList();
    Map<String, Object> getCurrentLineStations(int lineId);
}
