var module = angular.module('passengerApp', ['ngRoute', 'ngCookies']);

module.config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when("/", {
        controller: passenger,
        templateUrl: "html/passenger/main.html"
    }).when("/add", {
        controller: modify,
        templateUrl: "html/passenger/modify.html"
    }).when('/:id', {
        controller: modify,
        templateUrl: "html/passenger/modify.html"
    }).otherwise({
        redirectTo: '/'
    });
}]);

function passenger($scope, $http, $cookieStore) {
    function init() {
        $scope.agency = $cookieStore.get('agency');
        $scope.msg = '';
        Util.ajax({
            url: 'Agency/searchPassenger/' + $scope.agency.id,
            success: function (response) {
                $scope.passengers = response['data'];
                $scope.passengers.forEach(function (v, i, p) {
                    p[i]['gender'] = v['sex'] == 0 ? '女' : '男';
                })
            }
        }, $http);
    }

    init();

    $scope.delete = function (index) {
        Util.ajax({
            url: 'Agency/deletePassenger/' + $scope.agency.id + '/' + $scope.passengers[index]['id'],
            success: function () {
                $scope.passengers.splice(index, 1);
            },
            error: function () {
                $scope.msg = '删除失败';
            }
        }, $http);
    }
}

function modify($scope, $http, $routeParams, $cookieStore) {
    function init() {
        $scope.id = $routeParams.id;
        $scope.msg = '';
        $scope.flag = false; //提示文字状态
        $scope.passenger = {};
        $scope.agency = $cookieStore.get('agency');
        if (!($scope.agency instanceof Object)) {
            location.href = 'login.html';
        }
        if ($scope.id === undefined) { //add
            $scope.title = '添加旅客信息';
            $scope.button = '添加';
            $scope.passenger.sex = 1;
        } else {
            $scope.title = '修改旅客信息';
            $scope.button = '提交';
            Util.ajax({
                url: 'Agency/searchPassenger/' + $scope.agency.id + '/' + $scope.id,
                success: function (response) {
                    $scope.passenger = response['data'];
                    $scope.passenger.idcard = response['data']['idcard'] + '';
                },
                error: function (response) {
                    $scope.msg = response.data.msg;
                }
            }, $http);

        }
    }

    init();

    $scope.submit = function () {
        if ($scope.passenger.name === undefined || $scope.passenger.name === '') {
            alert('请填写姓名');
            return false;
        }
        if ($scope.passenger.phone === undefined || $scope.passenger.phone === '') {
            alert('请填写手机号');
            return false;
        }
        if (!Util.checkCardId($scope.passenger.idcard)) {
            return false;
        }
        var url = '';
        if ($scope.id === undefined) {
            url = 'Agency/addPassenger/' + $scope.agency.id;
        } else {
            url = 'Agency/modifyPassenger/' + $scope.agency.id;
        }
        Util.ajax({
            url: url,
            method: 'POST',
            data: $scope.passenger,
            success: function (response) {
                if (response.headers('EntityClass') === 'Message') {
                    $scope.msg = response.data.msg;
                    $scope.flag = false;
                } else {
                    $scope.passenger = {};
                    if ($scope.id === undefined) {
                        $scope.msg = '添加成功';
                    } else {
                        $scope.msg = '修改成功';
                    }
                    $scope.flag = true;
                }

            },
            error: function () {
                if ($scope.id === undefined) {
                    $scope.msg = '添加失败';
                } else {
                    $scope.msg = '修改失败';
                }
                $scope.flag = false;
            }
        }, $http)
    }
}
angular.bootstrap("#topNav", ['topNavApp']);
angular.bootstrap("#passenger", ['passengerApp']);
