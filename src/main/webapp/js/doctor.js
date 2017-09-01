// A $( document ).ready() block.
// Print welcome message for doctor.
$(document).ready(function () {
    $.ajax({
        url: "info",
        cache: false,
        success: function (data) {
            console.log(data);
            $('#welcome_person').append('<h3>Doctor: ' + data[0]['first_name'] + ' ' + data[0]['last_name'] + '</h3><h4> Specialty: ' + data[0]['specialty'] + '</h4>');
        }
    });
});

// A $( document ).ready() block.
$(document).ready(function () {
    $.ajax({
        url: "appointment",
        cache: false,
        data: {type:"doctor"},
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
            data: {app_id: $(this).data("app"), status:"Completed"},
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
            data: {app_id: $(this).data("app"), status:"Cancelled"},
            cache: false,
            success: function (data) {
                location.reload();
            }
        });
    });
});
