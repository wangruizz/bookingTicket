angular.module('myApp', ['ngCookies']).controller('my', function ($scope, $http, $cookieStore) {
    $scope.agency = $cookieStore.get('agency');
});
angular.bootstrap("#topNav", ['topNavApp']);
angular.bootstrap("#my", ['myApp']);
