$(function () {
    $('#login-form-link').click(function (e) {
        $("#login-form").delay(100).fadeIn(100);
        $("#register-form").fadeOut(100);
        $('#register-form-link').removeClass('active');
        $("#register-form2").fadeOut(100);
        $('#register-form-link2').removeClass('active');
        $(this).addClass('active');
        e.preventDefault();
    });
    $('#register-form-link').click(function (e) {
        $("#register-form").delay(100).fadeIn(100);
        $("#login-form").fadeOut(100);
        $('#login-form-link').removeClass('active');
        $("#register-form2").fadeOut(100);
        $('#register-form-link2').removeClass('active');
        $(this).addClass('active');
        e.preventDefault();
    });
    $('#register-form-link2').click(function (e) {
        $("#register-form2").delay(100).fadeIn(100);
        $("#login-form").fadeOut(100);
        $('#login-form-link').removeClass('active');
        $("#register-form").fadeOut(100);
        $('#register-form-link').removeClass('active');
        $(this).addClass('active');
        e.preventDefault();
    });
});

// A $( document ).ready() block.
// Print welcome message for admin.
$(document).ready(function () {
    $.ajax({
        url: "info",
        cache: false,
        success: function (data) {
            console.log(data);
            $('#welcome_person').append('<h3>Administrator: ' + data[0]['first_name'] + ' ' + data[0]['last_name'] + '</h3>');
        }
    });
});

// A $( document ).ready() block.
// Print registered doctors that exist in the database.
$(document).ready(function () {
    $.ajax({
        url: "doctors",
        cache: false,
        success: function (data) {
            console.log(data);
            data.forEach(function (row) {
                $('#doctors_registered tbody').append('<tr>');
                $('#doctors_registered tbody').append('<td>Doctor ID: ' + row['u_id'] + '<br>' + row['first_name'] + ' ' + row['last_name'] + '</td>');
                $('#doctors_registered tbody').append('<td>' + row['specialty'] + '</td>');
                $('#doctors_registered tbody').append('<td><button id="doctors_delete" type="button">Deregister</button></td>');
                $('#doctors_registered tbody').append('</tr>');
            });
        }
    });
});
