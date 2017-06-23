for (var i = 13; i < 33; i++) {
    $.ajax({
        url: '/CXF/REST/Agency/bookingTicket/14/913/' + i + '/0',
        success: function () {
            console.log(arguments);
        },
        error: function () {
            console.log(arguments);
        },
        headers:{
            token: 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE0OTgxOTUwMDAsInN1YiI6IntcIm5hbWVcIjpcIuWwj-m4oeeCluiYkeiPhzHlj7dcIixcInVzZXJJZFwiOlwiMTc4Mzk5MjM0NTRcIn0iLCJpc3MiOiJaWlUiLCJhdWQiOiIxNCIsImV4cCI6MTQ5ODI4MTQwMCwibmJmIjoxNDk4MTk1MDAwfQ.8bQWwdY0m8uob62vNhrVBOGTKGkeFuw7or3sJhzW_xw',
            Accept: 'application/json'
        }
    });
}
