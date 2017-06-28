var labels = new Array();
labels ['A-C'] = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19];
labels ['D-G'] = [20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35];
labels ['H-J'] = [36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55];
labels ['K-N'] = [56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79];
labels ['O-V'] = [80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98];
labels ['W-Z'] = [99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122];

$(document).ready(function () {
    $('#from').querycity({'data': cities, 'tabs': labels, 'pos': $("#fromCity")});
    $('#to').querycity({'data': cities, 'tabs': labels, 'pos': $("#toCity")});
    $("#date").daterangepicker({singleDatePicker: true, startDate: new Date()});
});

var module = angular.module("queryApp", ['ngCookies']);
module.controller("query", function ($scope, $http, $cookieStore) {
    var d;

    function init() {
        $scope.agency = $cookieStore.get('agency');
        $scope.from = '';
        $scope.fromCode = '';
        $scope.to = '';
        $scope.toCode = '';
        $scope.date = '';
        $scope.historyID = '';
        $scope.flight = [];
        $scope.passengers = [];

        d = new Date();
        d.setHours(0);
        d.setMinutes(0);
        d.setSeconds(0);
        d.setMilliseconds(0);
        var search = Util.getSearch();
        var i;
        if (search['from'] !== undefined) {
            for (i = 0; i < cities.length; i++) {
                if (cities[i][0] === search['from']) {
                    $scope.fromCode = cities[i][0];
                    $scope.from = cities[i][1];
                    break;
                }
            }
        }
        if (search['to'] !== undefined) {
            for (i = 0; i < cities.length; i++) {
                if (cities[i][0] === search['to']) {
                    $scope.toCode = cities[i][0];
                    $scope.to = cities[i][1];
                    break;
                }
            }
        }
        if (search['date'] !== undefined) {
            if (search['date'].match(/^201[0-9]-[0-1][0-9]-[0-3][0-9]$/) !== null) {
                if (search['date'] < Util.dateFormat()) {
                    $scope.date = Util.dateFormat();
                } else {
                    $scope.date = search['date'];
                }
            }
        }

        if (search['from'] !== undefined && search['to'] !== undefined && search['date'] !== undefined) {
            Util.ajax({
                url: 'Agency/queryTicket/' + search['from'] + '/' + search['to'] + '/' + search['date'],
                success: function (request) {
                    $scope.flight = request['data'];
                    var d1 = new Date();
                    var d2 = new Date();
                    d1.setMilliseconds(0);
                    d1.setSeconds(0);
                    d2.setMilliseconds(0);
                    d2.setSeconds(0);

                    $scope.flight.forEach(function (v, i, f) {
                        d1.setHours(parseInt(v.flight.startTime.split(":")[0]));
                        d2.setHours(parseInt(v.flight.arriveTime.split(":")[0]));
                        d1.setMinutes(parseInt(v.flight.startTime.split(':')[1]));
                        d2.setMinutes(parseInt(v.flight.arriveTime.split(':')[1]));
                        if (d1 > d2) {
                            d2.setDate(d.getDate() + 1);
                        }
                        var t = Math.floor((d2 - d1) / 60000); //转成分钟
                        f[i]['during'] = (Math.floor(t / 60) < 10 ? '0' + Math.floor(t / 60) : Math.floor(t / 60)) + ':' + (t % 60);
                        f[i]['economyNum'] = parseInt(f[i]['economyNum']);
                        f[i]['businessNum'] = parseInt(f[i]['businessNum']);
                    });
                }
            }, $http);
        }
    }

    init();

    $scope.find = function () {
        $scope.from = $("#from").val();
        if ($scope.from === '') {
            alert('请选择出发地点');
            return false;
        }
        for (var i = 0; i < cities.length; i++) {
            if (cities[i][1] === $scope.from) {
                $scope.fromCode = cities[i][0];
                break;
            }
        }
        if (i === cities.length) {
            alert('出发地点有误，请刷新重试');
            return false;
        }
        $scope.to = $("#to").val();
        if ($scope.to === '') {
            alert('请选择到达地点');
            return false;
        }
        for (i = 0; i < cities.length; i++) {
            if (cities[i][1] === $scope.to) {
                $scope.toCode = cities[i][0];
                break;
            }
        }
        if (i === cities.length) {
            alert('到达地点有误，请刷新重试');
            return false;
        }
        if ($scope.toCode === $scope.fromCode) {
            alert('出发地点不能和结束地点相同');
            return false;
        }
        $scope.date = $("#date").val();
        if ($scope.date.match(/^20[0-9]{2}-[0-1][0-9]-[0-3][0-9]$/) === null) {
            alert('日期格式不正确');
            return false;
        }
        if (d > new Date($scope.date)) {
            alert('日期不正确');
            return false;
        }
        window.location.href = './query.html?from=' + $scope.fromCode + '&to=' + $scope.toCode + '&date=' + $scope.date;
        return false;
    };

    $scope.book = function (id) {
        if ($scope.agency === undefined) {
            location.href = 'login.html';
            return false;
        }
        $scope.historyID = id;
        $("#selectPassenger").modal();
    };

    $scope.findPassenger = function () {
        var v = $("#name").val();
        Util.ajax({
            url: 'Agency/searchPassenger/' + $scope.agency.id + '/name/' + v,
            method: 'GET',
            success: function (response) {
                $scope.passengers = response['data'];
                $scope.passengers.forEach(function (p, i, ps) {
                    p.type = 1;
                })
            },
            error: function () {

            }
        }, $http);
    };

    $scope.bookingTicket = function (pid, index) {
        Util.ajax({
            url: 'Agency/bookingTicket/' + $scope.agency.id + '/' + $scope.historyID + '/' + pid + '/' + $scope.passengers[index].type,
            method: 'GET',
            success: function (response) {
                if (response.headers('EntityClass') === 'Message') {
                    alert(response.data.msg);
                } else {
                    $scope.passengers[index]['code'] = response.data.id;
                }
            },
            error: function (response) {
                if (response.data.msg === undefined) {
                    alert('订票失败，请重试');
                } else {
                    alert(response.data.msg);
                }
            }
        }, $http);
    }
});
module.controller("find", function ($scope, $http) {
    function init() {
        $scope.code = '';
        $scope.idcard = '';
    }

    init();

    $scope.find = function () {
        if ($scope.code === '') {
            alert("请填写取票码");
            return false;
        }
        if ($scope.idcard === '') {
            alert("请填写身份证号");
            return false;
        }
        if (Util.checkCardId($scope.idcard)) {
            Util.ajax({
                url: 'Agency/printTicket/' + $scope.code + '/' + $scope.idcard,
                method: 'GET',
                success: function (response) {
                    if (response.headers('EntityClass') === 'Message') {
                        alert(response.data.msg);
                    } else {
                        alert(response.data.passenger.name + " 已取票");
                    }
                },
                error: function (response) {
                    if (response.data.msg === undefined) {
                        alert('取票失败，请重试');
                    } else {
                        alert(response.data.msg);
                    }
                }
            }, $http);
        }
    };
});
angular.bootstrap("#topNav", ['topNavApp']);
angular.bootstrap("#query", ['queryApp']);
