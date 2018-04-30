(function (ng) {
    var mod = angular.module('RFC3Mod', ['ui.router']);

    mod.config(['$stateProvider', '$urlRouterProvider', function ($stateProvider, $urlRouterProvider) {

            $stateProvider.state('rfc3', {
                views: {
                    inputView: {
                        templateUrl: "RFC3/rfc3.input.html",
                        controller: "RFC3Ctrl"
                    }
                }
            }).state('rfc3Response', {


                views: {
                    inputView: {
                        templateUrl: "RFC3/rfc3.input.html"
                    },
                    outputView: {
                        templateUrl: "RFC3/rfc3.output.html",
                        controller: "responseRFC3Ctrl"
                    }
                }
            });
        }]);

})(window.angular);