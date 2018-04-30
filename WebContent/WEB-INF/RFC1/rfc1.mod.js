(function (ng) {
    var mod = angular.module('RFC1Mod', ['ui.router']);

    mod.config(['$stateProvider', '$urlRouterProvider', function ($stateProvider, $urlRouterProvider) {

            $stateProvider.state('rfc1', {
                views: {
                    inputView: {
                        templateUrl: "RFC1/rfc1.input.html",
                        controller: "RFC1Ctrl"
                    }
                }
            }).state('rfc1Response', {


                views: {
                    inputView: {
                        templateUrl: "RFC1/rfc1.input.html"
                    },
                    outputView: {
                        templateUrl: "RFC1/rfc1.output.html",
                        controller: "responseRFC1Ctrl"
                    }
                }
            });
        }]);

})(window.angular);