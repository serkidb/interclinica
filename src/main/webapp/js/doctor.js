// A $( document ).ready() block.
// Print welcome message for doctor.
$(document).ready(function () {
    $("#days").change(function () {
        if ($(this).val() == "Sunday") {
            $("#hours").hide();
        } else {
            $("#hours").show();
        }
    });

    $.ajax({
        url: "info",
        cache: false,
        success: function (data) {
            console.log(data);
            $('#welcome_person').append('<h3>Doctor: ' + data[0]['first_name'] + ' ' + data[0]['last_name'] + '</h3><h4> Specialty: ' + data[0]['specialty'] + '</h4>');
        }
    });

    $.ajax({
        url: "appointment",
        cache: false,
        data: {type: "doctor"},
        success: function (data) {
            console.log(data);
            var length = data.length;
            var i = 0;
            data.forEach(function (row) {
                $('#appointments_table tbody').append('<tr>');
                $('#appointments_table tbody').append('<td> Appointment ' + (length - i) + '<br>' + row['date_time'] + '</td>');
                $('#appointments_table tbody').append('<td>' + row['first_name'] + ' ' + row['last_name'] + '</td>');
                if ((row['app_status'] == 'Active') || (row['app_status'] == 'active')) {
                    $('#appointments_table tbody').append('<td><br><button class="appointment_complete" type="button" data-app="' + row['app_id'] + '">Complete</button><br><br><button class="appointment_cancel" type="button" data-app="' + row['app_id'] + '">Cancel</button><br><br></td>');
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

    // Complete Appointment
    $(document).on('click', '.appointment_complete', function () {
        $.ajax({
            url: "changestate",
            data: {app_id: $(this).data("app"), status: "Completed"},
            cache: false,
            success: function (data) {
                location.reload();
            }
        });
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

    // Retrieve current availability
    $.ajax({
        url: "curavailability",
        cache: false,
        success: function (data) {
            console.log(data);
            if (data.length !== 0) {
                if (data[0]['days'] === "Mon-Fri") {
                    if (data[0]['hours'] === "morning") {
                        $('#curr_availability').append('<h3>From Monday to Friday, 9AM to 5PM</h3>');
                    } else
                        $('#curr_availability').append('<h3>From Monday to Friday, 5PM to 9PM</h3>');
                } else if (data[0]['days'] === "Mon-Sat") {
                    if (data[0]['hours'] === "morning") {
                        $('#curr_availability').append('<h3>From Monday to Saturday, 9AM to 5PM</h3>');
                    } else
                        $('#curr_availability').append('<h3>From Monday to Saturday, 5PM to 9PM</h3>');
                } else if (data[0]['days'] === "Sunday") {
                    $('#curr_availability').append('<h3>Every Sunday, 9AM to 9PM</h3>');
                }
            } else {
                $('#curr_availability').append('<h3>You have not registered as available to patients yet.</h3>');
            }
        }
    });
});
