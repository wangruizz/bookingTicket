angular.module('registerApp', ['ngCookies']).controller('register', function ($scope, $http) {
    function init() {
        $scope.agency = {};
        $scope.company = {};
        $scope.flag = true; //手机号是否通过检查
        $scope.msg = '';
        $scope.msgStatus = '';
        $scope.phoneStatus = 'init';
        $scope.nameStatus = 'init';
        $scope.usernameStatus = 'init';
    }

    init();
    
    $scope.submit = function (flag) {
        if (flag) { //agency
            if ( $scope.phoneStatus !== 'success') {
                alert('请换一个手机号');
                return false;
            }
            $scope.agency.pwd = $scope.agency.password;
            if (Util.checkAgency($scope.agency)) {
                Util.ajax({
                    url: 'Agency/AgencyRegister',
                    data: $scope.agency,
                    method: 'POST',
                    success: function (response) {
                        $scope.agency = response['data'];
                        $scope.msg = '注册成功';
                        $scope.msgStatus = 'success';
                        location.href = '/login.html';
                    },
                    error: function (response) {
                        $scope.msg = response.data.msg;
                        $scope.msgStatus = 'danger';
                    }
                }, $http);
            }
        } else {//company
            $scope.company.pwd = $scope.company.password;
            if (Util.checkCompany($scope.company)){
                Util.ajax({
                    url: 'Company/register',
                    data: $scope.company,
                    method: 'POST',
                    success: function (response) {
                        $scope.company = response['data'];
                        $scope.msg = '注册成功';
                        $scope.msgStatus = 'success';
                        location.href = '/loginAirport.html';
                    },
                    error: function () {
                        $scope.msg = '注册失败，请重试！';
                        $scope.msgStatus = 'danger';
                    }
                }, $http)
            }
        }
    };

    $scope.checkUsername = function () {
        Util.ajax({
            url: 'Company/checkUserName/' + $scope.company.username,
            success: function (response) {
                if (response.data.code == "1") {
                    $scope.usernameStatus = 'success';
                }else {
                    $scope.usernameStatus = 'error';
                }
            },
            error: function () {
                $scope.usernameStatus = 'error';
            }
        }, $http)
    };

    $scope.checkPhone = function () {
        Util.ajax({
            url: 'Agency/checkPhone/'+$scope.agency.phone,
            success: function (response) {
                if (response.data.code == "1") {
                    $scope.phoneStatus = 'success';
                }else {
                    $scope.phoneStatus = 'error';
                }
            },
            error: function () {
                $scope.phoneStatus = 'error';
            }
        }, $http)
    };

    $scope.checkName = function () {
        $scope.nameStatus = 'success';
    };
    
});
angular.bootstrap("#topNav", ['topNavApp']);
angular.bootstrap("#register", ['registerApp']);