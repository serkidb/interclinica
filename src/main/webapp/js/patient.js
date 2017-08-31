
// A $( document ).ready() block.
// Print welcome message for patient.
$(document).ready(function () {
    $.ajax({
        url: "info",
        cache: false,
        success: function (data) {
            console.log(data);
            $('#welcome_person').append('<h3>Patient: ' + data[0]['first_name'] + ' ' + data[0]['last_name'] + '</h3>');
        }
    });
});
// A $( document ).ready() block.
$(document).ready(function () {
    $.ajax({
        url: "appointment",
        cache: false,
        data: {type:"patient"},
        success: function (data) {
            console.log(data);
            var length = data.length;
            var i = 0;
            data.forEach(function (row) {
                $('#appointments_table tbody').append('<tr>');
                $('#appointments_table tbody').append('<td> Appointment ' + (length - i) + '<br>' + row['date_time'] + '</td>');
                $('#appointments_table tbody').append('<td>' + row['first_name'] + ' ' + row['last_name'] + '<br>' + row['specialty'] + '</td>');
                if ((row['app_status'] == 'Active') || (row['app_status'] == 'active')) {
                    $('#appointments_table tbody').append('<td><button id="appointment_cancel" type="button">Cancel</button></td>');
                } else if ((row['app_status'] == 'Completed') || (row['app_status'] == 'completed')) {
                    $('#appointments_table tbody').append('<td><p><font color="green">Completed!</font></p></td>');
                } else if ((row['app_status'] == 'Cancelled') || (row['app_status'] == 'cancelled') || (row['app_status'] == 'Canceled') || (row['app_status'] == 'canceled')) {
                    $('#appointments_table tbody').append('<td><p><font color="red">Cancelled!</font></p></td>');
                }
                $('#appointments_table tbody').append('</tr>');
                i = i + 1;
            });
        }
    });
});