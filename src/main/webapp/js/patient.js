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
            var names = [];
            var j = 0;
            var uniqueNames = [];

            data.forEach(function (row) {
                names[j] = row['specialty'];
                j++;
            });

            $.each(names, function (i, el) {
                if ($.inArray(el, uniqueNames) === -1)
                    uniqueNames.push(el);
            });

            $.each(uniqueNames, function (i, el) {
                $('#doctorSpecialty').append('<option value="' + uniqueNames[i] + '">' + uniqueNames[i] + '</option>');
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
                console.log(data);
                if (data == 'notupdated')
                {
                    alert("You can't cancel at this point");
                } else
                {
                    location.reload();
                }
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

    // Check for available appointments.
    $(document).on('click', '.check_doctors', function () {
        $.ajax({
            url: "checkavailability",
            cache: false,
            data: {doctor_type: $('#doctorSpecialty').val(), app_date: $('#datePicker').val(), time_of_day: $("input[name='day']:checked").val()},
            success: function (data) {
                console.log(data);
                $('#availability_table tbody').remove();
                $('#availability_table').append("<tbody></tbody>");
                var timeOfDayLength = 0;
                var hour = 0;
                var stringHour = "";
                var date_time = "";
                var appLength = 0;
                data.forEach(function (row) {
                    appLength = row['appointments'].length;
                    if (($("input[name='day']:checked").val() == "Morning") || ($("input[name='day']:checked").val() == "morning")) {
                        timeOfDayLength = 8;
                        hour = 9;
                        for (i = 0; i < timeOfDayLength; i++) {
                            if (hour === 9)
                            {
                                stringHour = "09:00:00";
                            } else
                            {
                                stringHour = hour + ":00:00";
                            }

                            date_time = $('#datePicker').val() + " " + stringHour;

                            if (appLength == 0) {
                                $('#availability_table tbody').append('<tr>');
                                $('#availability_table tbody').append('<td>' + date_time + '</td>');
                                $('#availability_table tbody').append('<td>' + row['first_name'] + ' ' + row['last_name'] + '<br>' + row['specialty'] + '</td>');
                                $('#availability_table tbody').append('<td><button class="appointment_book" type="button" data-doctor="' + row['u_id'] + '" data-date="' + date_time + '">Book This</button></td>');
                                $('#availability_table tbody').append('</tr>');
                                hour = hour + 1;
                            } else {
                                var exist = "false";
                                for (j = 0; j < appLength; j++) {
                                    if (row['appointments'][j].hour == date_time) {
                                        exist = "true";
                                        break;
                                    }
                                }
                                $('#availability_table tbody').append('<tr>');
                                $('#availability_table tbody').append('<td>' + date_time + '</td>');
                                $('#availability_table tbody').append('<td>' + row['first_name'] + ' ' + row['last_name'] + '<br>' + row['specialty'] + '</td>');
                                if (exist == "true") {
                                    $('#availability_table tbody').append('<td><p><font color="red">Not Available!</font></p></td>');
                                } else
                                    $('#availability_table tbody').append('<td><button class="appointment_book" type="button" data-doctor="' + row['u_id'] + '"data-date="' + date_time + '">Book This</button></td>');
                                $('#availability_table tbody').append('</tr>');
                                hour = hour + 1;

                            }

                        }
                    } else if (($("input[name='day']:checked").val() == "Evening") || ($("input[name='day']:checked").val() == "evening")) {
                        timeOfDayLength = 5;
                        hour = 17;
                        for (i = 0; i < timeOfDayLength; i++) {
                            stringHour = hour + ":00:00";
                            date_time = $('#datePicker').val() + " " + stringHour;

                            if (appLength == 0) {
                                $('#availability_table tbody').append('<tr>');
                                $('#availability_table tbody').append('<td>' + date_time + '</td>');
                                $('#availability_table tbody').append('<td>' + row['first_name'] + ' ' + row['last_name'] + '<br>' + row['specialty'] + '</td>');
                                $('#availability_table tbody').append('<td><button class="appointment_book" type="button" data-doctor="' + row['u_id'] + '"data-date="' + date_time + '">Book This</button></td>');
                                $('#availability_table tbody').append('</tr>');
                                hour = hour + 1;
                            } else {
                                var exist = "false";
                                for (j = 0; j < appLength; j++) {
                                    if (row['appointments'][j].hour == date_time) {
                                        exist = "true";
                                        break;
                                    }
                                }
                                $('#availability_table tbody').append('<tr>');
                                $('#availability_table tbody').append('<td>' + date_time + '</td>');
                                $('#availability_table tbody').append('<td>' + row['first_name'] + ' ' + row['last_name'] + '<br>' + row['specialty'] + '</td>');
                                if (exist == "true") {
                                    $('#availability_table tbody').append('<td><p><font color="red">Not Available!</font></p></td>');
                                } else
                                    $('#availability_table tbody').append('<td><button class="appointment_book" type="button" data-doctor="' + row['u_id'] + '"data-date="' + date_time + '">Book This</button></td>');
                                $('#availability_table tbody').append('</tr>');
                                hour = hour + 1;
                            }
                        }
                    }
                });
            }
        });
    });

    // Book Appointment.
    $(document).on('click', '.appointment_book', function () {
        console.log($(this).data("doctor") + $(this).data("date"));
        $.ajax({
            url: "bookapp",
            data: {doctor_id: $(this).data("doctor"), date_time: $(this).data("date")},
            cache: false,
            success: function (data) {
                location.reload();
            }
        });
    });
});
