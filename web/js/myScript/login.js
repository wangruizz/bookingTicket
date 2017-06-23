angular.module('loginApp', ['ngCookies']).controller("login", function ($scope, $http, $cookieStore) {
    function init() {
        $scope.phone = '';
        $scope.username = '';
        $scope.password = '';
        $scope.msg = '';
        $scope.msgStatus = '';
        $scope.agency = {};
        $scope.company = {};
    }
    init();

    $scope.login = function (flag) {
        if (flag) { //agency
            if ($scope.phone === '') {
                alert('请填写手机号');
                return false;
            }
        } else { //company
            if ($scope.username === '') {
                alert('请填写用户名');
                return false;
            }
        }
        if ($scope.password === '') {
            alert('请填写密码');
            return false;
        }
        if (flag) { //agency
            Util.ajax({
                url: '/Agency/doLogin/' + $scope.phone + '/' + $scope.password,
                success: function (response) {
                    if (response['headers']('EntityClass') === 'Message') {
                        $scope.msgStatus = 'danger';
                        $scope.msg = response['data']['msg'];
                    } else {
                        $scope.agency['name'] = response['data']['name'];
                        $scope.agency['address'] = response['data']['address'];
                        $scope.agency['phone'] = response['data']['phone'];
                        $scope.agency['contacts'] = response['data']['contacts'];
                        $scope.agency['token'] = response['data']['token'];
                        $scope.agency['id'] = response['data']['id'];
                        Util.token = response['data']['token'];
                        $cookieStore.put('agency', $scope.agency);
                        location.href = './my.html';
                    }
                },
                error: function () {
                    $scope.msgStatus = 'danger';
                    $scope.msg = '登录失败，请重试';
                }
            }, $http);
        } else { //company
            Util.ajax({
                url: 'Company/doLogin/' + $scope.username + '/' + $scope.password,
                success: function (response) {
                    if (response['headers']('EntityClass') === 'Message') {
                        $scope.msgStatus = 'danger';
                        $scope.msg = response['data']['msg'];
                    } else {
                        $scope.company['name'] = response['data']['name'];
                        $scope.company['phone'] = response['data']['phone'];
                        $scope.company['username'] = response['data']['username'];
                        $scope.company['token'] = response['data']['token'];
                        Util.token = response['data']['token'];
                        $cookieStore.put('company', $scope.company);
                        location.href = './my2.html';
                    }
                },
                error: function () {
                    $scope.msgStatus = 'danger';
                    $scope.msg = '登录失败，请重试';
                }
            }, $http);
        }

    };
});

angular.bootstrap("#topNav", ['topNavApp']);
angular.bootstrap("#login", ['loginApp']);