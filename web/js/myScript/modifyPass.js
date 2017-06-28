angular.module('modifyPassApp', ['ngCookies']).controller('modifyPass', function ($scope, $http, $cookieStore) {
    function init() {
        $scope.oldPassword = '';
        $scope.password = '';
        $scope.repeat = '';
        $scope.msg = '';
        $scope.agency = $cookieStore.get('agency');
        $scope.company = $cookieStore.get('company');
        if ($scope.agency === undefined && $scope.company === undefined) {
            location.href = './login.html';
        }
    }
    init();
    
    $scope.submit = function (flag) {
        if ($scope.oldPassword === '') {
            alert('请先输入原密码');
            return false;
        }
        if ($scope.password === '') {
            alert('请输入新密码');
            return false;
        }
        if ($scope.password !== $scope.repeat) {
            alert('两次密码输入不一致');
            return false;
        }
        var old = $.md5($scope.oldPassword);
        var now = $.md5($scope.password);
        if (flag) { //agency
            $scope.agency.pwd = $scope.password;
            Util.ajax({
                url: 'Agency/modifyPwd/' + $scope.agency.phone + '/' + old + '/' + now,
                success: function (response) {
                    if (response.headers('EntityClass') === 'Agency') {
                        $cookieStore.remove('agency');
                        $cookieStore.remove('company');
                        window.location.href = './login.html'
                    } else {
                        $scope.msg = response.data.msg;
                    }
                },
                error: function () {
                    $scope.msg = '修改失败';
                }
            }, $http)
        } else { //company
            Util.ajax({
                url: 'Company/modifyPwd/' + $scope.company.username + '/' + old + '/' + now,
                success: function (response) {
                    if (response.headers('EntityClass') === 'Company') {
                        $cookieStore.remove('agency');
                        $cookieStore.remove('company');
                        window.location.href = './loginAirport.html'
                    } else {
                        $scope.msg = response.data.msg;
                    }
                },
                error: function () {
                    $scope.msg = '修改失败';
                }
            }, $http)
        }
    }
});
angular.bootstrap("#topNav", ['topNavApp']);
angular.bootstrap("#modifyPass", ['modifyPassApp']);
