var Util = {};
Util.init = function () {
    Util.token = '';
    Util.agency = {};
    Util.company = {};

    Util.getSearch = function () {
        var s = location.search.substr(1);
        var args = s.split('&');
        var ret = {};
        args.forEach(function (v) {
            v = v.split('=');
            ret[decodeURI(v[0])] = decodeURI(v[1]);
        });
        return ret;
    };

    Util.ajax = function (config, $http) {
        if (config['method'] === undefined) {
            config['method'] = 'GET';
        }
        if (config['headers'] === undefined) {
            config['headers'] = {};
        }
        if (config['headers']['Accept'] === undefined) {
            config['headers']['Accept'] = 'application/json';
        }
        if (config['headers']['token'] === undefined) {
            config['headers']['token'] = Util.token;
        }
        if (config['headers']['Accept'] === undefined) {
            config['headers']['Accept'] = 'application/json';
        }
        if (config['headers']['Content-Type'] === undefined) {
            config['headers']['Content-Type'] = 'application/json';
        }
        if (config['dataType'] === undefined) {
            config['dataType'] = 'json';
        }
        if (config['url'].match(/^\/CXF\/REST\/\S*/) === null) {
            if (config['url'][0] === '/') {
                config['url'] = '/CXF/REST' + config['url'];
            } else {
                config['url'] = '/CXF/REST/' + config['url'];
            }
        }
        $http(config).then(config['success'], config['error']);
    };

    Util.checkNum = function (val, num) {
        if (num === undefined && isNaN(num)) {
            return val.match(/^[0-9]+$/) !== null;
        }
        return val.match('^[0-9]{'+num+'}$') !== null;
    };

    Util.checkCardId = function (socialNo){
        if(socialNo === "") {
            alert("输入身份证号码不能为空!");
            return (false);
        }
        if (socialNo.length !== 15 && socialNo.length !== 18) {
            alert("输入身份证号码格式不正确!");
            return (false);
        }
        var area={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"};
        if(area[parseInt(socialNo.substr(0,2))]===null) {
            alert("身份证号码不正确(地区非法)!");
            return (false);
        }
        if (socialNo.length === 15) {
            pattern= /^\d{15}$/;
            if (pattern.exec(socialNo)===null){
                alert("15位身份证号码必须为数字！");
                return (false);
            }
            var birth = parseInt("19" + socialNo.substr(6,2));
            var month = socialNo.substr(8,2);
            var day = parseInt(socialNo.substr(10,2));
            switch(month) {
                case '01':
                case '03':
                case '05':
                case '07':
                case '08':
                case '10':
                case '12':
                    if(day>31) {
                        alert('输入身份证号码不格式正确!');
                        return false;
                    }
                    break;
                case '04':
                case '06':
                case '09':
                case '11':
                    if(day>30) {
                        alert('输入身份证号码不格式正确!');
                        return false;
                    }
                    break;
                case '02':
                    if((birth % 4 === 0 && birth % 100 !== 0) || birth % 400 === 0) {
                        if(day>29) {
                            alert('输入身份证号码不格式正确!');
                            return false;
                        }
                    } else {
                        if(day>28) {
                            alert('输入身份证号码不格式正确!');
                            return false;
                        }
                    }
                    break;
                default:
                    alert('输入身份证号码不格式正确!');
                    return false;
            }
            var nowYear = new Date().getYear();
            if(nowYear - parseInt(birth)<15 || nowYear - parseInt(birth)>100) {
                alert('输入身份证号码不格式正确!');
                return false;
            }
            return (true);
        }

        var Wi = [7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2,1];
        var   lSum        = 0;
        var   nNum        = 0;

        for (var i = 0; i < 17; ++i) {
            if ( socialNo.charAt(i) < '0' || socialNo.charAt(i) > '9' ) {
                alert("输入身份证号码格式不正确!");
                return (false);
            } else {
                nNum = socialNo.charAt(i) - '0';
            }
            lSum += nNum * Wi[i];
        }

        if( socialNo.charAt(17) === 'X' || socialNo.charAt(17) === 'x') {
            lSum += 10*Wi[17];
        } else if ( socialNo.charAt(17) < '0' || socialNo.charAt(17) > '9' ) {
            alert("输入身份证号码格式不正确!");
            return (false);
        } else {
            lSum += ( socialNo.charAt(17) - '0' ) * Wi[17];
        }
        if ( (lSum % 11) === 1 ) {
            return true;
        } else {
            alert("输入身份证号码格式不正确!");
            return (false);
        }
    };

    Util.checkAgency = function (agency) {
        if (agency['name'] === undefined || agency['name'] === '') {
            alert('请填写旅行社名称');
            return false;
        }
        if (agency['address'] === undefined || agency['address'] === '') {
            alert('请填写通信地址');
            return false;
        }
        if (agency['contacts'] === undefined || agency['contacts'] === '') {
            alert('请填写联系方式');
            return false;
        }
        if (!Util.checkNum(agency['contacts'])) {
            alert('联系方式格式错误');
            return false;
        }
        if (agency['phone'] === undefined || agency['phone'] === '') {
            alert('请填写手机号');
            return false;
        }
        if (agency['pwd'] !== undefined && agency['repeat'] !== undefined && agency['pwd'] !== agency['repeat']) {
            alert('两次密码输入不一致');
            return false;
        }
        return true;
    };

    Util.checkCompany = function (company) {
        if (company['name'] === undefined || company['name'] === '') {
            alert('请填写公司名称');
            return false;
        }
        if (company['username'] === undefined || company['username'] === '') {
            alert('请填写登录名');
            return false;
        }
        if (company['phone'] === undefined || company['phone'] === '') {
            alert('请填写联系方式');
            return false;
        }
        if (!Util.checkNum(company['phone'])) {
            alert('联系方式格式错误');
            return false;
        }
        if (company['pwd'] !== undefined && company['repeat'] !== undefined && company['pwd'] !== company['repeat']) {
            alert('两次密码输入不一致');
            return false;
        }
        return true;
    };

    Util.checkFlight = function (flight) {
        if (flight['id'] === undefined || flight['id'] === '') {
            alert('请填写航班号');
            return false;
        }
        if (flight['startTime'] === undefined || flight['startTime'] === '') {
            alert('请填写出发时间');
            return false;
        }
        if (flight['arriveTime'] === undefined || flight['arriveTime'] === '') {
            alert('请填写到达时间');
            return false;
        }
        if (flight['businessPrice'] === undefined || flight['businessPrice'] === '') {
            alert('请填写商务舱价格');
            return false;
        }
        if (flight['economyPrice'] === undefined || flight['economyPrice'] === '') {
            alert('请填写经济舱价格');
            return false;
        }
        if (flight['businessNum'] === undefined || flight['businessNum'] === '') {
            alert('请填写商务舱座位数');
            return false;
        }
        if (flight['economyNum'] === undefined || flight['economyNum'] === '') {
            alert('请填写经济舱座位数');
            return false;
        }
        if (flight['startAirport'] === undefined || flight['startAirport'] === '') {
            alert('请填写始发机场');
            return false;
        }
        if (flight['arriveAirport'] === undefined || flight['arriveAirport'] === '') {
            alert('请填写目的机场');
            return false;
        }
        if (flight['arriveAirport'] !== undefined && flight['arriveAirport']['name'] === flight['startAirport']['name']) {
            alert('始发机场和目的机场不能相同');
            return false;
        }
        return true;
    };

    Util.dateFormat = function (date) {
        if (date === undefined) {
            date = new Date();
        }
        var res = date.getFullYear() + '-';
        if (date.getMonth() < 9) {
            res += '0' + (date.getMonth() + 1);
        } else {
            res += (date.getMonth() + 1);
        }
        res += '-' + date.getDate();
        return res;
    };
};
angular.module('firstApp', []);
angular.module('topNavApp', ['ngCookies']).controller('topNav', function ($scope, $cookieStore) {
    function init() {
        Util.init();
        $scope.login = false;
        $scope.my = 'my';
        Util.agency = $cookieStore.get('agency');
        Util.company = $cookieStore.get('company');
        if (Util.agency instanceof Object) {
            $scope.login = true;
            Util.token = Util.agency.token;
        } else if (Util.company instanceof Object) {
            $scope.login = true;
            $scope.my = 'my2';
            Util.token = Util.company.token;
        }
    }

    init();

    $scope.logout = function () {
        $cookieStore.remove("company");
        $cookieStore.remove("agency");
        location.href = 'main.html';
    }
});
