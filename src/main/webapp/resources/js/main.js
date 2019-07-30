$(document).ready(function () {
    $('#datetimepicker').datetimepicker({
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
        $('#linesList').toggle();
    });

    $('#addTrainMenu').click(function () {
        $('#addTrainForm').toggle();
    });

});

function showAddStationForm(zone, order) {
    $("#addStationForm").show();
    $("#zone").val(zone);
    $("#order").val(order);
    $("#addStationName").focus();
}