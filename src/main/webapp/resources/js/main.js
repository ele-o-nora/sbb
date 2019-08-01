$(document).ready(function () {
    $('#datetimepickerTrains, #datetimepickerSchedule').datetimepicker({
        icons: {
            time: 'far fa-clock',
            date: 'far fa-calendar',
            up: 'fas fa-arrow-up',
            down: 'fas fa-arrow-down',
            previous: 'fas fa-chevron-left',
            next: 'fas fa-chevron-right',
        },
        minDate: moment(),
        maxDate: moment().add(2, 'months'),
        format: 'YYYY-MM-DD HH:mm'
    });

    $('#addStationMenu').click(function () {
        $('#linesStationsList').toggle();
    });

    $('#modifyRouteMenu').click(function () {
        $('#linesRoutesList').toggle();
    });

    $('#Northern').click(function () {
        $('#routesNorthern').toggle();
    });

    $('#Eastern').click(function () {
        $('#routesEastern').toggle();
    });

    $('#Western').click(function () {
        $('#routesWestern').toggle();
    });

    $('#Southern').click(function () {
        $('#routesSouthern').toggle();
    });

    $('#trainModelsMenu').click(function () {
        $('#trainModelsList').toggle();
    });

    $('#addTrainMenu').click(function () {
        $('#addTrainModal').modal();
    });

});

function showAddStationForm(zone, order, before, after) {
    $("#zone").val(zone);
    $("#order").val(order);
    $("#rowBefore, #rowAfter").hide();
    if (before) {
        $("#rowBefore").show();
    }
    if (after) {
        $("#rowAfter").show();
    }
    $("#addStationModal").modal();
}