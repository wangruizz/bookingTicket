var labels = new Array();
labels ['A-C'] = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19];
labels ['D-G'] = [20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35];
labels ['H-J'] = [36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55];
labels ['K-N'] = [56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79];
labels ['O-V'] = [80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98];
labels ['W-Z'] = [99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122];

var module = angular.module('flightInfoApp', ['ngRoute', 'ngCookies']);
module.config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when("/", {
        controller:flight,
        templateUrl:"html/flight/main.html"
    }).when('/modify/:id', {
        controller: modify,
        templateUrl: "html/flight/modify.html"
    }).when('/add', {
        controller: modify,
        templateUrl: "html/flight/modify.html"
    }).when('/detail/:id', {
        controller: detail,
        templateUrl: "html/flight/detail.html"
    }).otherwise({
        redirectTo: '/'
    });
}]);

function modify($scope, $http, $cookieStore, $routeParams) {
    function init() {
        $scope.company = $cookieStore.get('company');
        if ($scope.company === undefined) {
            location.href = 'loginAirport.html';
            return ;
        }
        $scope.msg = '';
        $scope.flag = '';
        $scope.id = $routeParams.id;
        $scope.flight = {};
        $scope.cities = cities;


        if ($scope.id === undefined || $scope.id === 'add') { //add
            $scope.title = '添加航班信息';
            $scope.button = '添加';
            $scope.url = 'Company/addFlight/' + $scope.company.username;
        } else { //modify
            $scope.title = '修改航班信息';
            $scope.button = '修改';
            $scope.url = 'Company/modifyFlight/' + $scope.company.username;
            Util.ajax({
                url: 'Company/queryFlight/' + $scope.company.username + '/' + $scope.id,
                success: function (response) {
                    $scope.flight = response['data'];
                    $scope.flight['startAirportName'] = $scope.flight['startAirport']['name'];
                    $scope.flight['arriveAirportName'] = $scope.flight['arriveAirport']['name'];
                },
                error: function () {
                    $scope.msg = '未知错误，请刷新重试';
                }
            }, $http);
        }
    }
    init();

    $scope.submit = function () {
        $scope.flight['startAirportName'] = $('#startAirport').val();
        $scope.flight['arriveAirportName'] = $('#arriveAirport').val();
        $scope.flight['arriveTime'] = $('#arriveTime').val();
        $scope.flight['startTime'] = $('#startTime').val();
        $scope.flight['startAirport'] = {};
        for (var i = 0; i < cities.length; i++) {
            if (cities[i][1] === $scope.flight['startAirportName']) {
                $scope.flight['startAirport']['name'] = cities[i][1];
                $scope.flight['startAirport']['id'] = cities[i][0];
                break;
            }
        }
        $scope.flight['arriveAirport'] = {};
        for (i = 0; i < cities.length; i++) {
            if (cities[i][1] === $scope.flight['arriveAirportName']) {
                $scope.flight['arriveAirport']['name'] = cities[i][1];
                $scope.flight['arriveAirport']['id'] = cities[i][0];
                break;
            }
        }
        Util.ajax({
            url: $scope.url,
            method: 'POST',
            data: $scope.flight,
            success: function (response) {
                if (response.headers('EntityClass') === 'Flight') {
                    if ($scope.id === undefined || $scope.id === 'add') {
                        $scope.msg = '添加成功';
                        $scope.flight = {};
                    } else {
                        $scope.msg = '修改成功';
                    }
                    $scope.flag = true;
                } else {
                    $scope.msg = response.data.msg;
                    $scope.flag = false;
                }
            },
            error: function () {
                if ($scope.id === undefined || $scope.id === 'add') {
                    $scope.msg = '添加失败';
                } else {
                    $scope.msg = '修改失败';
                }
                $scope.flag = false;
            }
        }, $http);
    }

    $scope.checkInt = function (v) {
        return v.match(/^\d+$/) !== null;
    };

    $scope.checkFloat = function (v) {
        return v.match(/^\d+(\.\d+)?$/) !== null;
    }
}

function flight($scope, $http, $cookieStore) {
    function init() {
        $scope.company = $cookieStore.get('company');
        $scope.flights = [];
        Util.ajax({
            url: 'Company/queryFlight/' + $scope.company.username,
            success: function (response) {
                $scope.flights = response['data'];
            }
        }, $http);
    }

    init();
}

function detail($scope, $http, $cookieStore, $routeParams) {
    function init() {
        $scope.company = $cookieStore.get('company');
        if ($scope.company === undefined) {
            location.href = 'loginAirport.html';
            return ;
        }
        $scope.id = $routeParams.id;
        $scope.postponeId = '';
        $scope.postponeInput = false;
        $scope.postponeButton = false;
        $scope.cancelButton = false;
        Util.ajax({
            url: 'Company/queryFlight/' + $scope.company.username + '/' + $scope.id,
            success: function (response) {
                $scope.flight = response['data'];
                $scope.flight['startAirportName'] = $scope.flight['startAirport']['name'];
                $scope.flight['arriveAirportName'] = $scope.flight['arriveAirport']['name'];

                var d = new Date();
                var d1 = new Date();
                var d2 = new Date();
                d.setHours(0);
                d.setMinutes(0);
                d.setSeconds(0);
                d.setMilliseconds(0);
                d1.setMilliseconds(0);
                d1.setSeconds(0);
                d2.setMilliseconds(0);
                d2.setSeconds(0);

                d1.setHours(parseInt($scope.flight.startTime.split(":")[0]));
                d2.setHours(parseInt($scope.flight.arriveTime.split(":")[0]));
                d1.setMinutes(parseInt($scope.flight.startTime.split(':')[1]));
                d2.setMinutes(parseInt($scope.flight.arriveTime.split(':')[1]));
                if (d1 > d2) {
                    d2.setDate(d.getDate()+1);
                }
                var t = Math.floor((d2 - d1) / 60000); //转成分钟
                $scope.flight['during'] = (Math.floor(t/60) < 10 ? '0'+Math.floor(t/60): Math.floor(t/60)) +':'+(t%60);
            },
            error: function () {
                $scope.msg = '未知错误，请刷新重试';
            }
        }, $http);
    }

    init();

    $scope.delete = function () {
        Util.ajax({
            url: 'Company/flightCancel/' + $scope.company.username + '/' + $scope.flight.id,
            success: function (response) {
                if (response.data.code == 1) {
                    $scope.flights.splice(id, 1);
                } else {
                    alert(response.data.msg);
                }
            },
            error: function (response) {
                if (response.data.msg === undefined) {
                    alert('删除失败');
                } else {
                    alert(response.data.msg);
                }
            }
        }, $http)
    };

    $scope.postpone = function () {
        var time = $("#deltaTime").val();
        if (time.match(/^\d\d:\d\d$/) === null) {
            alert("时间格式不对");
            return false;
        }
        Util.ajax({
            url: 'Company/flightDelay/' + $scope.company.username + '/' + $scope.flight.id + '/' + Util.dateFormat() + '/',
            success: function (response) {
                if (response.data.code == 1) {

                } else {
                    alert(response.data.msg);
                }
            },
            error: function (response) {
                if (response.data.msg === undefined) {
                    alert('操作失败');
                } else {
                    alert(response.data.msg);
                }
            }
        }, $http)
    };

    $scope.cancel = function () {
        Util.ajax({
            url: 'Company/cancelFlightSomeday/' + $scope.company.username + '/' + $scope.flight.id + '/' + Util.dateFormat(),
            success: function (response) {
                if (response.data.code == 1) {
                    $scope.flights.splice(id, 1);
                } else {
                    alert(response.data.msg);
                }
            },
            error: function (response) {
                if (response.data.msg === undefined) {
                    alert('操作失败');
                } else {
                    alert(response.data.msg);
                }
            }
        }, $http)
    };
}


angular.bootstrap("#topNav", ['topNavApp']);
angular.bootstrap("#flightInfo", ['flightInfoApp']);