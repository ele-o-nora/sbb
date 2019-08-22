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
        maxDate: moment().subtract(10, 'years'),
        viewMode: 'years'
    });

    $('#validUntilPicker').datetimepicker({
        icons: {
            time: 'far fa-clock',
            date: 'far fa-calendar',
            up: 'fas fa-arrow-up',
            down: 'fas fa-arrow-down',
            previous: 'fas fa-chevron-left',
            next: 'fas fa-chevron-right',
        },
        format: 'MM/YYYY',
        minDate: moment()
    });

    $('#addStationMenu').click(function () {
        $('#linesStationsList').toggle();
    });

    $('#modifyRouteMenu').click(function () {
        $('#linesRoutesList').toggle();
    });

    $('#Northern').click(function () {
        $('#routesNorthern').toggle();
        return false;
    });

    $('#Eastern').click(function () {
        $('#routesEastern').toggle();
        return false;
    });

    $('#Western').click(function () {
        $('#routesWestern').toggle();
        return false;
    });

    $('#Southern').click(function () {
        $('#routesSouthern').toggle();
        return false;
    });

    $('#trainModelsMenu').click(function () {
        $('#trainModelsList').toggle();
    });

    $('#addTrainMenu').click(function () {
        $('#addTrainModal').modal();
    });

    $('#tariffMenu').click(function () {
        $('#updateTariffModal').modal();
    });

    $('#scheduleRoutesMenu').click(function () {
        $('#linesRoutesScheduleList').toggle();
    });

    $('#NorthernSchedule').click(function () {
        $('#routesNorthernSchedule').toggle();
        return false;
    });

    $('#EasternSchedule').click(function () {
        $('#routesEasternSchedule').toggle();
        return false;
    });

    $('#SouthernSchedule').click(function () {
        $('#routesSouthernSchedule').toggle();
        return false;
    });

    $('#WesternSchedule').click(function () {
        $('#routesWesternSchedule').toggle();
        return false;
    });

    $('#signUpButton').click(function () {
        $('#signUpModal').modal();
    });

    $('#signInButton').click(function () {
        $('#signInModal').modal();
    });

    $('#passwordRegister').keyup(function () {
        this.setCustomValidity(this.validity.patternMismatch ? this.title : '');
        $('#confirmPasswordRegister').prop('pattern', RegExp.escape(this.value));
    });

    $('#confirmPasswordRegister, #confirmNewPassword, #cvc, #cardNumber').keyup(function () {
        this.setCustomValidity(this.validity.patternMismatch ? this.title : '');
    });

    $('#newPassword').keyup(function () {
        this.setCustomValidity(this.validity.patternMismatch ? this.title : '');
        $('#confirmNewPassword').prop('pattern', RegExp.escape(this.value));
    });

    if(!RegExp.escape) {
        RegExp.escape = function(s) {
            return String(s).replace(/[\\^$*+?.()|[\]{}]/g, '\\$&');
        };
    }

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
    return false;
}

function showRouteStations(id) {
    $(id).toggle();
    return false;
}

function showAdjustJourneyModal(journeyId) {
    $('#delayId').val(journeyId);
    $('#cancelId').val(journeyId);
    $('#adjustJourneyModal').modal();
    return false;
}