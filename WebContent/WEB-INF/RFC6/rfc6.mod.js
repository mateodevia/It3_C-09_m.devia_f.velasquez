(function (ng) {
    var mod = angular.module('RFC6Mod', ['ui.router']);

    mod.config(['$stateProvider', '$urlRouterProvider', function ($stateProvider, $urlRouterProvider) {

            $stateProvider.state('rfc6', {
                views: {
                    inputView: {
                        templateUrl: "RFC6/rfc6.input.html",
                        controller: "RFC6Ctrl"
                    }
                }
            }).state('rfc6Response', {

            	params: {
                    id: null
                },
                views: {
                    inputView: {
                        templateUrl: "RFC6/rfc6.input.html"
                    },
                    outputView: {
                        templateUrl: "RFC6/rfc6.output.html",
                        controller: "responseRFC6Ctrl"
                    }
                }
            });
        }]);

})(window.angular);