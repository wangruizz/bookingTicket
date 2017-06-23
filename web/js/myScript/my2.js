angular.module('myApp', ['ngCookies']).controller('my', function ($scope, $http, $cookieStore) {
    $scope.company = $cookieStore.get('company');

});
angular.bootstrap("#topNav", ['topNavApp']);
angular.bootstrap("#my", ['myApp']);
