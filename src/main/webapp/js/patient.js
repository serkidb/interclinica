// A $( document ).ready() block.
$(document).ready(function () {
    // Print welcome message for patient.
    $.ajax({
        url: "info",
        cache: false,
        success: function (data) {
            console.log(data);
            $('#welcome_person').append('<h3>Patient: ' + data[0]['first_name'] + ' ' + data[0]['last_name'] + '</h3>');
        }
    });

    $.ajax({
        url: "doctors",
        cache: false,
        success: function (data) {
            console.log(data);
            data.forEach(function (row) {
                $('#doctorSpecialty').append('<option value="' + row['specialty'] + '">' + row['specialty'] + '</option>');
            });
        }
    });

    $.ajax({
        url: "appointment",
        cache: false,
        data: {type: "patient"},
        success: function (data) {
            console.log(data);
            var length = data.length;
            var i = 0;
            data.forEach(function (row) {
                $('#appointments_table tbody').append('<tr>');
                $('#appointments_table tbody').append('<td> Appointment ' + (length - i) + '<br>' + row['date_time'] + '</td>');
                $('#appointments_table tbody').append('<td>' + row['first_name'] + ' ' + row['last_name'] + '<br>' + row['specialty'] + '</td>');
                if ((row['app_status'] == 'Active') || (row['app_status'] == 'active')) {
                    $('#appointments_table tbody').append('<td><button class="appointment_cancel" type="button" data-app="' + row['app_id'] + '">Cancel</button></td>');
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

    // Cancel Appointment
    $(document).on('click', '.appointment_cancel', function () {
        $.ajax({
            url: "changestate",
            data: {app_id: $(this).data("app"), status: "Cancelled"},
            cache: false,
            success: function (data) {
                location.reload();
            }
        });
    });

    // Get current date.
    var date = new Date();
    var day = date.getDate();
    var month = date.getMonth() + 1;
    var year = date.getFullYear();
    if (month < 10)
        month = "0" + month;
    if (day < 10)
        day = "0" + day;
    var today = year + "-" + month + "-" + day;
    $("#datePicker").attr("value", today);

    // Book appointment
    $(document).on('click', '.check_doctors', function () {
        $.ajax({
            url: "checkavailability",
            cache: false,
            data: {doctor_type: $('#doctorSpecialty').val(), app_date: $('#datePicker').val(), time_of_day: $("input[name='day']:checked").val()},
            success: function (data) {
                console.log(data);
//                data.forEach(function (row) {
//                    $('#availability_table tbody').append('<tr>');
//                    $('#availability_table tbody').append('<td>' + row['date_time'] + '</td>');
//                    $('#availability_table tbody').append('<td>' + row['first_name'] + ' ' + row['last_name'] + '<br>' + row['specialty'] + '</td>');
//                    $('#availability_table tbody').append('<td><button class="appointment_book" type="button" data-app="' + row['doc_id'] + row['date_time'] + '">Book This</button></td>');
//                    $('#availability_table tbody').append('</tr>');
//                });
            }
        });
    });
});