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

    $('#datePickerFrom').datetimepicker({
        icons: {
            time: 'far fa-clock',
            date: 'far fa-calendar',
            up: 'fas fa-arrow-up',
            down: 'fas fa-arrow-down',
            previous: 'fas fa-chevron-left',
            next: 'fas fa-chevron-right',
        },
        minDate: moment(),
        maxDate: moment().add(3, 'months'),
        format: 'YYYY-MM-DD'
    });

    $('#datePickerUntil').datetimepicker({
        icons: {
            time: 'far fa-clock',
            date: 'far fa-calendar',
            up: 'fas fa-arrow-up',
            down: 'fas fa-arrow-down',
            previous: 'fas fa-chevron-left',
            next: 'fas fa-chevron-right',
        },
        minDate: moment(),
        maxDate: moment().add(3, 'months'),
        format: 'YYYY-MM-DD',
        useCurrent: false
    });

    $("#datePickerFrom").on("change.datetimepicker", function (e) {
        $('#datePickerUntil').datetimepicker('minDate', e.date);
    });
    $("#datePickerUntil").on("change.datetimepicker", function (e) {
        $('#datePickerFrom').datetimepicker('maxDate', e.date);
    });

    $('#timePicker').datetimepicker({
        icons: {
            time: 'far fa-clock',
            date: 'far fa-calendar',
            up: 'fas fa-arrow-up',
            down: 'fas fa-arrow-down',
            previous: 'fas fa-chevron-left',
            next: 'fas fa-chevron-right',
        },
        format: 'HH:mm'
    });

    $('#dateOfBirthPicker, #dateOfBirthPicker2').datetimepicker({
        icons: {
            time: 'far fa-clock',
            date: 'far fa-calendar',
            up: 'fas fa-arrow-up',
            down: 'fas fa-arrow-down',
            previous: 'fas fa-chevron-left',
            next: 'fas fa-chevron-right',
        },
        format: 'YYYY-MM-DD',
        maxDate: moment().subtract(16, 'years'),
        viewMode: 'years'
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

    $('#scheduleRoutesMenu').click(function () {
        $('#linesRoutesScheduleList').toggle();
    });

    $('#NorthernSchedule').click(function () {
        $('#routesNorthernSchedule').toggle();
    });

    $('#EasternSchedule').click(function () {
        $('#routesEasternSchedule').toggle();
    });

    $('#SouthernSchedule').click(function () {
        $('#routesSouthernSchedule').toggle();
    });

    $('#WesternSchedule').click(function () {
        $('#routesWesternSchedule').toggle();
    });

    $('#signUpButton').click(function () {
        $('#signUpModal').modal();
    });

    $('#signInButton').click(function () {
        $('#signInModal').modal();
    })

});

function showAddStationForm(order, before, after) {
    $("#order").val(order);
    $("#rowBefore, #rowAfter").hide();
    $("#distanceBefore, #distanceAfter").prop("required", false);
    if (before) {
        $("#rowBefore").show();
        $("#distanceBefore").prop("required", true);
    }
    if (after) {
        $("#rowAfter").show();
        $("#distanceAfter").prop("required", true);
    }
    $("#addStationModal").modal();
}

function showScheduleRoutesForm(routeId) {
    $('#routeId').val(routeId);
    $('#scheduleRouteModal').modal();
}