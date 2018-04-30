(function (ng) {
    var mod = angular.module('RFC4Mod', ['ui.router']);

    mod.config(['$stateProvider', '$urlRouterProvider', function ($stateProvider, $urlRouterProvider) {

            $stateProvider.state('rfc4', {
                views: {
                    inputView: {
                        templateUrl: "RFC4/rfc4.input.html",
                        controller: "RFC4Ctrl"
                    }
                }
            }).state('rfc4Response', {

            	params: {
                    fechaInicio: null,
                    fechaFin: null,
                    servicios: null
                },
                views: {
                    inputView: {
                        templateUrl: "RFC4/rfc4.input.html"
                    },
                    outputView: {
                        templateUrl: "RFC4/rfc4.output.html",
                        controller: "responseRFC4Ctrl"
                    }
                }
            });
        }]);

})(window.angular);