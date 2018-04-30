(function (ng) {
    var mod = angular.module('RFC5Mod', ['ui.router']);

    mod.config(['$stateProvider', '$urlRouterProvider', function ($stateProvider, $urlRouterProvider) {

            $stateProvider.state('rfc5', {
                views: {
                    inputView: {
                        templateUrl: "RFC5/rfc5.input.html",
                        controller: "RFC5Ctrl"
                    }
                }
            }).state('rfc5Response', {


                views: {
                    inputView: {
                        templateUrl: "RFC5/rfc5.input.html"
                    },
                    outputView: {
                        templateUrl: "RFC5/rfc5.output.html",
                        controller: "responseRFC5Ctrl"
                    }
                }
            });
        }]);

})(window.angular);