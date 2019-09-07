<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Railway network map</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/webjars/leaflet/1.5.1/leaflet.css"/>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/webjars/leaflet/1.5.1/leaflet.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/resources/js/Leaflet.Editable.js"></script>
</head>
<body class="text-center">
<%@include file="header.jsp" %>
<div class="row justify-content-center">
    <div class="col-sm-8">
        <div id="map"></div>
    </div>
</div>
<script type="text/javascript">
    const map = L.map('map', {
        crs: L.CRS.Simple,
        maxZoom: 0,
        minZoom: -3,
        maxBounds: [[-1,-1], [6038,3001]],
        editable: true
    });
    map.doubleClickZoom.disable();
    const bounds = [[0,0], [6037,3000]];
    const image = L.imageOverlay('${pageContext.request.contextPath}/resources/westeros.jpg', bounds).addTo(map);
    map.setView([2112, 2090], -1);
    const stations = {};
    const stationNames = {};
    const linesLayer = L.featureGroup().addTo(map);
    const stationsLayer = L.featureGroup().addTo(map);
    const northern = L.polyline({}, {color:'black', weight:5, opacity:0.8}).addTo(linesLayer);
    northern.bindPopup("Northern line");
    const western = L.polyline({}, {color:'red', weight:5, opacity:0.8}).addTo(linesLayer);
    western.bindPopup("Western line");
    const southern = L.polyline({}, {color:'green', weight:5, opacity:0.8}).addTo(linesLayer);
    southern.bindPopup("Southern line");
    const eastern = L.polyline({}, {color:'blue', weight:5, opacity:0.8}).addTo(linesLayer);
    eastern.bindPopup("Eastern line");
    <c:forEach items="${stationsNorthern}" var="station">
    northern.addLatLng([${station.y}, ${station.x}]);
    stationNames[${station.id}] = "${station.name}";
    <c:choose>
    <c:when test="${station.id ne 1}">
    stations[${station.id}] = L.circle([${station.y}, ${station.x}],
        {color: 'black', fillColor: 'black', fillOpacity: 1.0, radius: 12}).addTo(stationsLayer);
    stations[${station.id}].bindPopup("<span class='font-weight-bold'>${station.name}</span><br><form method='post' action='${pageContext.request.contextPath}/curSchedule'><input type='hidden' name='stationName' value=\"${station.name}\"/><input type='submit' class='btn btn-outline-secondary btn-sm' value='Current schedule'/><input type='hidden' name='${_csrf.parameterName}'' value='${_csrf.token}'/></form>");
    </c:when>
    <c:otherwise>
    stations[${station.id}] = L.circle([${station.y}, ${station.x}],
        {color: 'gold', fillColor: 'gold', fillOpacity: 1.0, radius: 20}).addTo(stationsLayer);
    stations[${station.id}].bindPopup("<span class='font-weight-bold'>${station.name}</span><br><form method='post' action='${pageContext.request.contextPath}/curSchedule'><input type='hidden' name='stationName' value=\"${station.name}\"/><input type='submit' class='btn btn-outline-secondary btn-sm' value='Current schedule'/><input type='hidden' name='${_csrf.parameterName}'' value='${_csrf.token}'/></form>");
    </c:otherwise>
    </c:choose>
    </c:forEach>
    <c:forEach items="${stationsWestern}" var="station">
    western.addLatLng([${station.y}, ${station.x}]);
    stationNames[${station.id}] = "${station.name}";
    <c:if test="${station.id ne 1}">
    stations[${station.id}] = L.circle([${station.y}, ${station.x}],
        {color: 'red', fillColor: 'red', fillOpacity: 1.0, radius: 12}).addTo(stationsLayer);
    stations[${station.id}].bindPopup("<span class='font-weight-bold'>${station.name}</span><br><form method='post' action='${pageContext.request.contextPath}/curSchedule'><input type='hidden' name='stationName' value=\"${station.name}\"/><input type='submit' class='btn btn-outline-secondary btn-sm' value='Current schedule'/><input type='hidden' name='${_csrf.parameterName}'' value='${_csrf.token}'/></form>");
    </c:if>
    </c:forEach>
    <c:forEach items="${stationsSouthern}" var="station">
    southern.addLatLng([${station.y}, ${station.x}]);
    stationNames[${station.id}] = "${station.name}";
    <c:if test="${station.id ne 1}">
    stations[${station.id}] = L.circle([${station.y}, ${station.x}],
        {color: 'green', fillColor: 'green', fillOpacity: 1.0, radius: 12}).addTo(stationsLayer);
    stations[${station.id}].bindPopup("<span class='font-weight-bold'>${station.name}</span><br><form method='post' action='${pageContext.request.contextPath}/curSchedule'><input type='hidden' name='stationName' value=\"${station.name}\"/><input type='submit' class='btn btn-outline-secondary btn-sm' value='Current schedule'/><input type='hidden' name='${_csrf.parameterName}'' value='${_csrf.token}'/></form>");
    </c:if>
    </c:forEach>
    <c:forEach items="${stationsEastern}" var="station">
    eastern.addLatLng([${station.y}, ${station.x}]);
    stationNames[${station.id}] = "${station.name}";
    <c:if test="${station.id ne 1}">
    stations[${station.id}] = L.circle([${station.y}, ${station.x}],
        {color: 'blue', fillColor: 'blue', fillOpacity: 1.0, radius: 12}).addTo(stationsLayer);
    stations[${station.id}].bindPopup("<span class='font-weight-bold'>${station.name}</span><br><form method='post' action='${pageContext.request.contextPath}/curSchedule'><input type='hidden' name='stationName' value=\"${station.name}\"/><input type='submit' class='btn btn-outline-secondary btn-sm' value='Current schedule'/><input type='hidden' name='${_csrf.parameterName}'' value='${_csrf.token}'/></form>");
    </c:if>
    </c:forEach>
    <sec:authorize access="hasRole('ADMIN')">
    const coords = L.popup();

    function onMapClick(e) {
        coords.setLatLng(e.latlng).setContent("x: " + e.latlng.lng.toFixed(0) + ", y: " + e.latlng.lat.toFixed(0)).openOn(map);
    }

    map.on('click', onMapClick);

    northern.enableEdit();
    western.enableEdit();
    southern.enableEdit();
    eastern.enableEdit();

    let northernAdded = false;
    let westernAdded = false;
    let southernAdded = false;
    let easternAdded = false;

    map.on('editable:vertex:dragstart', function (e) {
        for (let key in stations) {
            if (stations[key].getLatLng().lat == e.vertex.latlng.lat
                && stations[key].getLatLng().lng == e.vertex.latlng.lng) {
                e.vertex.dragging.disable();
            }
        }
    });

    northern.on('editable:vertex:new', function () {
        northernAdded = true;
    });

    northern.on('editable:middlemarker:mousedown', function(e) {
        if (northernAdded) {
            e.cancel();
        }
    });

    western.on('editable:vertex:new', function () {
        westernAdded = true;
    });

    western.on('editable:middlemarker:mousedown', function(e) {
        if (westernAdded) {
            e.cancel();
        }
    });

    southern.on('editable:vertex:new', function () {
        southernAdded = true;
    });

    southern.on('editable:middlemarker:mousedown', function(e) {
        if (southernAdded) {
            e.cancel();
        }
    });

    eastern.on('editable:vertex:new', function () {
        easternAdded = true;
    });

    eastern.on('editable:middlemarker:mousedown', function(e) {
        if (easternAdded) {
            e.cancel();
        }
    });

    northern.on('editable:vertex:click', function(e) {
        e.cancel();
        for (let key in stations) {
            if (stations[key].getLatLng().lat == e.vertex.latlng.lat
                && stations[key].getLatLng().lng == e.vertex.latlng.lng) {
                showRenameStationForm(key, stationNames[key]);
                return;
            }
        }
        showAddStationFromMapForm(1, (e.vertex.getIndex() + 1), Math.round(e.latlng.lng), Math.round(e.latlng.lat));
    });
    western.on('editable:vertex:click', function(e) {
        e.cancel();
        for (let key in stations) {
            if (stations[key].getLatLng().lat == e.vertex.latlng.lat
                && stations[key].getLatLng().lng == e.vertex.latlng.lng) {
                showRenameStationForm(key, stationNames[key]);
                return;
            }
        }
        showAddStationFromMapForm(2, (e.vertex.getIndex() + 1), Math.round(e.latlng.lng), Math.round(e.latlng.lat));
    });
    southern.on('editable:vertex:click', function(e) {
        e.cancel();
        for (let key in stations) {
            if (stations[key].getLatLng().lat == e.vertex.latlng.lat
                && stations[key].getLatLng().lng == e.vertex.latlng.lng) {
                showRenameStationForm(key, stationNames[key]);
                return;
            }
        }
        showAddStationFromMapForm(3, (e.vertex.getIndex() + 1), Math.round(e.latlng.lng), Math.round(e.latlng.lat));
    });
    eastern.on('editable:vertex:click', function(e) {
        e.cancel();
        for (let key in stations) {
            if (stations[key].getLatLng().lat == e.vertex.latlng.lat
                && stations[key].getLatLng().lng == e.vertex.latlng.lng) {
                showRenameStationForm(key, stationNames[key]);
                return;
            }
        }
        showAddStationFromMapForm(4, (e.vertex.getIndex() + 1), Math.round(e.latlng.lng), Math.round(e.latlng.lat));
    });
    </sec:authorize>
</script>
<div class="modal fade" id="addStationFromMapModal" tabindex="-1" role="dialog" aria-labelledby="stationAddTitle"
     aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="stationAddTitle">Add new station</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form action="${pageContext.request.contextPath}/admin/addNewStation"
                      method="post" id="addStationForm" class="form-horizontal">
                    <input type="hidden" name="order" id="order" value="0">
                    <input type="hidden" name="line" id="line" value="0">
                    <div class="form-row justify-content-center m-2">
                        <div class="col-sm-9">
                            <input type="text" placeholder="Station name" name="name"
                                   id="addStationName" class="form-control" required>
                        </div>
                    </div>
                    <input type="hidden" name="x" id="x" value="-1">
                    <input type="hidden" name="y" id="y" value="-1">
                    <div class="form-row justify-content-center m-2">
                        <input type="submit" value="Add station" class="btn btn-outline-secondary">
                    </div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="renameStationModal" tabindex="-1" role="dialog" aria-labelledby="renameStationTitle"
     aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="renameStationTitle">Rename station</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form action="${pageContext.request.contextPath}/admin/renameStation"
                      method="post" id="renameStationForm" class="form-horizontal">
                    <input type="hidden" name="id" id="id" value="0">
                    <div class="form-row justify-content-center m-2">
                        <label class="col-sm-3 col-form-label" for="newName">New name:</label>
                        <div class="col-sm-8">
                            <input type="text" name="name" id="newName" class="form-control" required>
                        </div>
                    </div>
                    <div class="form-row justify-content-center m-2">
                        <input type="submit" value="Save" class="btn btn-outline-secondary">
                    </div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
