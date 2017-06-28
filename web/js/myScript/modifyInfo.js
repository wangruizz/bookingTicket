angular.module('modifyApp', ['ngCookies']).controller('modify', function ($scope, $cookieStore, $http) {
    $scope.agency = $cookieStore.get('agency');
    $scope.msg = '';

    $scope.submit = function () {
        if (Util.checkAgency($scope.agency)) {
            Util.ajax({
                url: 'Agency/modifyAgency',
                method: 'POST',
                data: $scope.agency,
                success: function (response) {
                    $scope.agency['name'] = response['data']['name'];
                    $scope.agency['address'] = response['data']['address'];
                    $scope.agency['phone'] = response['data']['phone'];
                    $scope.agency['contacts'] = response['data']['contacts'];
                    $cookieStore.put('agency', $scope.agency);
                    window.location.href = './my.html';
                },
                error: function () {
                    $scope.msg = '修改失败';
                }
            }, $http);
        }
    }
});
angular.bootstrap("#topNav", ['topNavApp']);
angular.bootstrap("#modify", ['modifyApp']);
