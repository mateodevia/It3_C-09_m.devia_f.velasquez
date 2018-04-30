(function (ng) {
    var mod = angular.module('RFC7Mod', ['ui.router']);

    mod.config(['$stateProvider', '$urlRouterProvider', function ($stateProvider, $urlRouterProvider) {

            $stateProvider.state('rfc7', {
                views: {
                    inputView: {
                        templateUrl: "RFC7/rfc7.input.html",
                        controller: "RFC7Ctrl"
                    }
                }
            }).state('rfc7Response', {

                params: {
                    unidadTiempo: null,
                    tipoAl: null
                },

                views: {
                    inputView: {
                        templateUrl: "RFC7/rfc7.input.html"
                    },
                    outputView: {
                        templateUrl: "RFC7/rfc7.output.html",
                        controller: "responseRFC7Ctrl"
                    }
                }
            });
        }]);

})(window.angular);