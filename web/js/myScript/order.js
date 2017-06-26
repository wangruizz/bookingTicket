var module = angular.module('orderApp', ['ngRoute', 'ngCookies']);
module.controller('order', function ($scope, $http, $cookieStore) {
    function init() {
        $scope.agency = $cookieStore.get('agency');
        if ($scope.agency === undefined) {
            location.href = 'login.html';
            return ;
        }
        $scope.unfinish = [];
        $scope.finish = [];
        $scope.cancel = [];

        Util.ajax({
            url: 'Agency/queryBookByAID/' + $scope.agency.id + '/0',
            method: 'GET',
            success: function (response) {
                $scope.unfinish = response.data;
            }
        }, $http);

        Util.ajax({
            url: 'Agency/queryBookByAID/' + $scope.agency.id + '/1',
            method: 'GET',
            success: function (response) {
                $scope.finish = response.data;
            }
        }, $http);

        Util.ajax({
            url: 'Agency/queryBookByAID/' + $scope.agency.id + '/-1',
            method: 'GET',
            success: function (response) {
                $scope.cancel = response.data;
            }
        }, $http);
    }

    init();
    
    $scope.pay = function (id, index) {
        Util.ajax({
            url: 'Agency/payTicket/' + $scope.agency.id + '/' + id,
            method: 'GET',
            success: function (response) {
                if (response.headers('EntityClass') === 'Book'){
                    $scope.finish.push($scope.unfinish.splice(index, 1)[0]);
                } else {
                    alert(response.data.msg);
                }
                $scope.cancel = response.data;
            },
            error: function (response) {
                if (response.data.msg === undefined) {
                    alert('付款失败，请重试');
                } else {
                    alert(response.data.msg);
                }
            }
        }, $http);
    };

    $scope.cancelBook = function (id, type, index) {
        Util.ajax({
            url: 'Agency/cancelBook/' + $scope.agency.id + '/' + id,
            method: 'GET',
            success: function (response) {
                if (response.headers('EntityClass') === 'Book'){
                    if (type === 1) {
                        $scope.cancel.push($scope.unfinish.splice(index, 1)[0]);
                    } else {
                        $scope.cancel.push($scope.finish.splice(index, 1)[0]);
                    }
                } else {
                    alert(response.data.msg);
                }
            },
            error: function (response) {
                if (response.data.msg === undefined) {
                    alert('取消失败，请重试');
                } else {
                    alert(response.data.msg);
                }
            }
        }, $http);
    };
});
angular.bootstrap("#topNav", ['topNavApp']);
angular.bootstrap("#order", ['orderApp']);