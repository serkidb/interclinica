// A $( document ).ready() block.
$( document ).ready(function() {
    $.ajax({
	  url: "appointment",
	  cache: false,
	  data : { id : 2},
	  success: function(data){
		console.log(data);
                
                data.forEach(function (row) {
                    $('#appointments_table tbody').append('<tr>');
                    $('#appointments_table tbody').append('<td> Appointment ' + row['app_id'] + '<br>' + row['date_time'] + '</td>');
                    $('#appointments_table tbody').append('<td>' + row['first_name'] + ' ' + row['last_name'] + '<br>' + row['specialty'] + '</td>');
                    $('#appointments_table tbody').append('<td>' + row['app_status'] + '</td>');
                    $('#appointments_table tbody').append('</tr>');
                });
            }
    });
});