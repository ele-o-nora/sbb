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
    const linesLayer = L.featureGroup().addTo(map);
    const stationsLayer = L.featureGroup().addTo(map);
    const northern = L.polyline({}, {color:'black', weight:5, opacity:0.7}).addTo(linesLayer);
    northern.bindPopup("Northern line");
    const western = L.polyline({}, {color:'red', weight:5, opacity:0.7}).addTo(linesLayer);
    western.bindPopup("Western line");
    const southern = L.polyline({}, {color:'green', weight:5, opacity:0.7}).addTo(linesLayer);
    southern.bindPopup("Southern line");
    const eastern = L.polyline({}, {color:'blue', weight:5, opacity:0.7}).addTo(linesLayer);
    eastern.bindPopup("Eastern line");
    <c:forEach items="${stationsNorthern}" var="station">
    northern.addLatLng([${station.y}, ${station.x}]);
    <c:choose>
    <c:when test="${station.id ne 1}">
    stations[${station.id}] = L.circle([${station.y}, ${station.x}],
        {color: 'black', fillColor: 'black', fillOpacity: 0.9, radius: 15}).addTo(stationsLayer);
    stations[${station.id}].bindPopup("<span class='font-weight-bold'>${station.name}</span><br><form method='post' action='${pageContext.request.contextPath}/curSchedule'><input type='hidden' name='stationName' value=\"${station.name}\"/><input type='submit' class='btn btn-outline-secondary btn-sm' value='Current schedule'/><input type='hidden' name='${_csrf.parameterName}'' value='${_csrf.token}'/></form>");
    </c:when>
    <c:otherwise>
    stations[${station.id}] = L.circle([${station.y}, ${station.x}],
        {color: 'gold', fillColor: 'gold', fillOpacity: 0.9, radius: 20}).addTo(stationsLayer);
    stations[${station.id}].bindPopup("<span class='font-weight-bold'>${station.name}</span><br><form method='post' action='${pageContext.request.contextPath}/curSchedule'><input type='hidden' name='stationName' value=\"${station.name}\"/><input type='submit' class='btn btn-outline-secondary btn-sm' value='Current schedule'/><input type='hidden' name='${_csrf.parameterName}'' value='${_csrf.token}'/></form>");
    </c:otherwise>
    </c:choose>
    </c:forEach>
    <c:forEach items="${stationsWestern}" var="station">
    western.addLatLng([${station.y}, ${station.x}]);
    <c:if test="${station.id ne 1}">
    stations[${station.id}] = L.circle([${station.y}, ${station.x}],
        {color: 'red', fillColor: 'red', fillOpacity: 0.9, radius: 15}).addTo(stationsLayer);
    stations[${station.id}].bindPopup("<span class='font-weight-bold'>${station.name}</span><br><form method='post' action='${pageContext.request.contextPath}/curSchedule'><input type='hidden' name='stationName' value=\"${station.name}\"/><input type='submit' class='btn btn-outline-secondary btn-sm' value='Current schedule'/><input type='hidden' name='${_csrf.parameterName}'' value='${_csrf.token}'/></form>");
    </c:if>
    </c:forEach>
    <c:forEach items="${stationsSouthern}" var="station">
    southern.addLatLng([${station.y}, ${station.x}]);
    <c:if test="${station.id ne 1}">
    stations[${station.id}] = L.circle([${station.y}, ${station.x}],
        {color: 'green', fillColor: 'green', fillOpacity: 0.9, radius: 15}).addTo(stationsLayer);
    stations[${station.id}].bindPopup("<span class='font-weight-bold'>${station.name}</span><br><form method='post' action='${pageContext.request.contextPath}/curSchedule'><input type='hidden' name='stationName' value=\"${station.name}\"/><input type='submit' class='btn btn-outline-secondary btn-sm' value='Current schedule'/><input type='hidden' name='${_csrf.parameterName}'' value='${_csrf.token}'/></form>");
    </c:if>
    </c:forEach>
    <c:forEach items="${stationsEastern}" var="station">
    eastern.addLatLng([${station.y}, ${station.x}]);
    <c:if test="${station.id ne 1}">
    stations[${station.id}] = L.circle([${station.y}, ${station.x}],
        {color: 'blue', fillColor: 'blue', fillOpacity: 0.9, radius: 15}).addTo(stationsLayer);
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

    northern.on('editable:vertex:click', function(e) {
        e.cancel();
        console.log("lineId: 1, order: " + (e.vertex.getIndex() + 1) + ", x: " + Math.round(e.latlng.lng) + ", y: " + Math.round(e.latlng.lat));
    });
    western.on('editable:vertex:click', function(e) {
        e.cancel();
        console.log("lineId: 2, order: " + (e.vertex.getIndex() + 1) + ", x: " + Math.round(e.latlng.lng) + ", y: " + Math.round(e.latlng.lat));
    });
    southern.on('editable:vertex:click', function(e) {
        e.cancel();
        console.log("lineId: 3, order: " + (e.vertex.getIndex() + 1) + ", x: " + Math.round(e.latlng.lng) + ", y: " + Math.round(e.latlng.lat));
    });
    eastern.on('editable:vertex:click', function(e) {
        e.cancel();
        console.log("lineId: 4, order: " + (e.vertex.getIndex() + 1) + ", x: " + Math.round(e.latlng.lng) + ", y: " + Math.round(e.latlng.lat));
    });
    </sec:authorize>
</script>
</body>
</html>
